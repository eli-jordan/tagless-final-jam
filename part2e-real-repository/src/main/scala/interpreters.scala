import java.nio.file.{Files, Path, Paths}

import cats.effect.Sync
import cats.implicits._
import domain._
import serialization.Codec
import service.{ParallelId, Throwing}

import scala.io.Source
import scala.language.higherKinds

object interpreters {

    case class NoSuchUserException(userId: UserId) extends Exception
    class ReadingListServiceCompiler[F[_]: Throwing : ParallelId](users: UserRepository[F], books: BookRepository[F]) extends ReadingListService[F] {

        override def getReadingList(userId: UserId): F[ReadingList] = {
            for {
                userOpt <- users.getUser(userId)
                list <- userOpt match {
                    case Some(user: User) =>
                        user.books.parTraverse(books.getBook).map { books => ReadingList(user, books.flatten) }
                    case None =>
                        Throwing[F].raiseError(NoSuchUserException(userId))
                }
            } yield list
        }

        override def addToReadingList(userId: UserId, bookId: BookId): F[Unit] = {
            for {
                list <- getReadingList(userId)
                books = bookId :: list.user.books
                _ <- users.updateUser(list.user.copy(books = books))
            } yield ()
        }

        override def removeFromReadingList(userId: UserId, bookId: BookId): F[Unit] = {
            for {
                list <- getReadingList(userId)
                books = list.user.books.filter(_ != bookId)
                _ <- users.updateUser(list.user.copy(books = books))
            } yield ()
        }
    }

    trait FileService[F[_]] {
        def exists(path: Path): F[Boolean]
        def listDir(path: Path): F[List[Path]]
        def getFileData(path: Path): F[String]
        def writeFileData(path: Path, data: String): F[Unit]
    }

    def save[F[_]: Throwing, T](codec: Codec[T], files: FileService[F])(path: Path, data: T): F[Unit] = {
        for {
            encoded <- codec.encode(data).pure[F].rethrow
            _ <- files.writeFileData(path, encoded)
        } yield ()
    }

    def read[F[_]: Throwing, T](codec: Codec[T], files: FileService[F])(path: Path): F[Option[T]] = {
        for {
            exists <- files.exists(path)
            result <- if (exists) {
                files.getFileData(path)
                        .map(codec.decode)
                        .rethrow
                        .map(Some.apply)
            } else {
                none[T].pure[F]
            }
        } yield result
    }

    class FileBackedUserRepository[F[_]: Throwing](root: Path, files: FileService[F], codec: Codec[User]) extends UserRepository[F] {
        override def getUser(id: UserId): F[Option[User]] =
            read(codec, files)(pathFor(id))

        override def addUser(user: User): F[Unit] =
            save(codec, files)(pathFor(user.id.get), user)

        override def updateUser(user: User): F[Unit] =
            save(codec, files)(pathFor(user.id.get), user)

        private def pathFor(key: UserId): Path = {
            Paths.get(root.toString, key.id)
        }
    }

    class FileBackedBookRepository[F[_]: Throwing](root: Path, files: FileService[F], codec: Codec[Book]) extends BookRepository[F] {

        override def listBooks(): F[List[Book]] = for {
            bookFiles <- files.listDir(root)
            values <- bookFiles.traverse(read(codec, files))
        } yield values.flatten

        override def getBook(id: BookId): F[Option[Book]] =
            read(codec, files)(pathFor(id))

        override def addBook(book: Book): F[Unit] =
            save(codec, files)(pathFor(book.id.get), book)

        private def pathFor(key: BookId): Path = {
            Paths.get(root.toString, key.id)
        }
    }


    class FileServiceSync[F[_]: Sync] extends FileService[F] {

        private val F = Sync[F]

        override def exists(path: Path): F[Boolean] = F.delay {
            path.toFile.exists()
        }

        override def listDir(path: Path): F[List[Path]] = F.delay {
            path.toFile.listFiles().toList.map(_.toPath)
        }

        override def getFileData(path: Path): F[String] = F.delay {
            Source.fromFile(path.toFile).getLines().mkString
        }

        override def writeFileData(path: Path, data: String): F[Unit] = F.delay {
            if (!path.getParent.toFile.exists()) {
                path.getParent.toFile.mkdirs()
            }
            Files.write(path, data.getBytes)
        }
    }
}
