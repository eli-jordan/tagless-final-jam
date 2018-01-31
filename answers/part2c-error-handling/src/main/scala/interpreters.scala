import cats.implicits._
import domain._
import service.Throwing

import scala.language.higherKinds

object interpreters {

    case class NoSuchUserException(userId: UserId) extends Exception
    class ReadingListServiceCompiler[F[_]: Throwing](users: UserRepository[F], books: BookRepository[F]) extends ReadingListService[F] {

        override def getReadingList(userId: UserId): F[ReadingList] = {
            for {
                userOpt <- users.getUser(userId)
                list <- userOpt match {
                    case Some(user: User) =>
                        user.books.traverse(books.getBook).map { books => ReadingList(user, books.flatten) }

                    // Exercise - raise an error
                    // Tip: Remember that typeclasses are implicits, how do we access the MonadError.raiseError function?
                    case None => Throwing[F].raiseError(NoSuchUserException(userId))
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
}
