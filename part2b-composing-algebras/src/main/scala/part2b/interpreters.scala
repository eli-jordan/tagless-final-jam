package part2b

import cats._
import cats.implicits._
import part2b.domain._

import scala.language.higherKinds

object interpreters {

    /**
     * Here we define an implementation of the [[ReadingListService]] in terms of the [[UserRepository]] and [[BookRepository]]
     */
    class ReadingListServiceCompiler[F[_]: Monad](users: UserRepository[F], books: BookRepository[F]) extends ReadingListService[F] {

        /**
         * Exercise - Implement 'getReadingList' in terms of the operation on 'UserRepository' and 'BookRepository'
         *
         *  Tip: Remember that the User object has a list of 'BookId' that are the ids of the books on their reading list.
         */
        override def getReadingList(userId: UserId): F[ReadingList] = ???

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
