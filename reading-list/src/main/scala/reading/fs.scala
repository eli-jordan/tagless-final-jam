package reading

import java.nio.file.{Files, Path, Paths}
import java.util.UUID

import cats.Monad
import cats.effect.{IO, Resource}
import cats.implicits._
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}
import reading.domain._

import scala.io.Source

/**
  * Define real-ish implementations of the [[BookRepository]] and [[UserRepository]]
  */
object fs {

  /**
    * A simple abstraction over the file system using the tagless-final style.
    * This is by no means a complete interface to the filesystem, but it serves our purpose.
    */
  trait FileService[F[_]] {
    def exists(path: Path): F[Boolean]
    def listDir(path: Path): F[List[Path]]
    def getFileData(path: Path): F[String]
    def writeFileData(path: Path, data: String): F[Unit]
  }

  /**
    * Implements the [[FileService]] abstraction in a pure-fp way, using [[IO]] to suspend side effects.
    */
  class FileServiceIO extends FileService[IO] {

    override def exists(path: Path): IO[Boolean] = IO.delay {
      path.toFile.exists()
    }

    override def listDir(path: Path): IO[List[Path]] = IO.delay {
      path.toFile.listFiles().toList.map(_.toPath)
    }

    override def getFileData(path: Path): IO[String] = {
      val acquire = IO.delay(Source.fromFile(path.toFile))
      val use     = (source: Source) => IO.delay(source.getLines().mkString)
      Resource.fromAutoCloseable(acquire).use(use)
    }

    override def writeFileData(path: Path, data: String): IO[Unit] = IO.delay {
      if (!path.getParent.toFile.exists()) {
        path.getParent.toFile.mkdirs()
      }
      Files.write(path, data.getBytes)
    }
  }

  /**
    * Save some data type [[T]] in a file specified by the provided [[Path]] encoded as JSON
    * using a circe [[Encoder]]
    */
  def save[F[_]: Monad, T: Encoder](files: FileService[F])(path: Path, data: T): F[Unit] = {
    for {
      encoded <- Encoder[T].apply(data).toString().pure[F]
      _       <- files.writeFileData(path, encoded)
    } yield ()
  }

  /**
    * Read the specified file, and decode it using the circe [[Decoder]] for type [[T]]
    */
  def read[F[_]: Throwing, T: Decoder](files: FileService[F])(path: Path): F[Option[T]] = {
    for {
      exists <- files.exists(path)
      result <- if (exists) {
        files
          .getFileData(path)
          .flatMap(string => io.circe.parser.parse(string).liftTo[F])
          .flatMap(json => Decoder[T].decodeJson(json).liftTo[F])
          .map(_.some)
      } else {
        none[T].pure[F]
      }
    } yield result
  }

  /**
    * An implementation of [[UserRepository]] that is backed by the filesystem.
    */
  class FileBackedUserRepository[F[_]: Throwing](root: Path, files: FileService[F]) extends UserRepository[F] {
    override def getUser(id: UserId): F[Option[User]] =
      read(files)(pathFor(id))

    override def addUser(user: User): F[Unit] =
      save(files)(pathFor(user.id.get), user)

    override def updateUser(user: User): F[Unit] =
      save(files)(pathFor(user.id.get), user)

    private def pathFor(key: UserId): Path = {
      Paths.get(root.toString, key.id)
    }
  }

  /**
    * An implementation of [[BookRepository]] that is backed by the filesystem.
    */
  class FileBackedBookRepository[F[_]: Throwing](root: Path, files: FileService[F]) extends BookRepository[F] {

    override def listBooks(): F[List[Book]] =
      for {
        bookFiles <- files.listDir(root)
        values    <- bookFiles.traverse(read[F, Book](files))
      } yield values.flatten

    override def getBook(id: BookId): F[Option[Book]] =
      read(files)(pathFor(id))

    override def addBook(book: Book): F[Unit] = {
      val id = book.id.getOrElse(BookId(UUID.randomUUID().toString))
      save(files)(pathFor(id), book.copy(id = Some(id)))
    }

    private def pathFor(key: BookId): Path = {
      Paths.get(root.toString, key.id)
    }
  }
}
