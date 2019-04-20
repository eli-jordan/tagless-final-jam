package reading

import io.circe.generic.extras.semiauto._
import io.circe.{Decoder, Encoder}

import scala.language.higherKinds

/**
  * Here we define the domain model for our reading list application.
  * The model is not ideal, but is simple and demonstrative.
  *
  * The application allow users to add and remove books from their reading list.
  */
object domain {

  /**
    * Here we define the data types that represent the entities on our application.
    *
    * User - represents a user and the list of books on their reading list
    * Book - represents the details of a book
    * ReadingList - represents the combination of a user and the book details
    */
  final case class UserId(id: String) extends AnyVal
  object UserId {
    implicit val circeEncoder: Encoder[UserId] = deriveUnwrappedEncoder
    implicit val circeDecoder: Decoder[UserId] = deriveUnwrappedDecoder
  }
  final case class User(
      id: Option[UserId], // the unique id of a user
      firstName: String, // the users first name
      lastName: String, // the users last name
      books: List[BookId] // the books the user has on their reading list
  )

  final case class BookId(id: String) extends AnyVal
  object BookId {
    implicit val circeEncoder: Encoder[BookId] = deriveUnwrappedEncoder
    implicit val circeDecoder: Decoder[BookId] = deriveUnwrappedDecoder
  }

  final case class Book(
      id: Option[BookId], // the unique id of the book
      title: String, // the books title
      author: String // the author of the book
  )

  final case class ReadingList(user: User, books: List[Book])

  /**
    * The user repository defines an algebra for manipulating users
    */
  trait UserRepository[F[_]] {

    /** Lookup a user based on its unique id */
    def getUser(id: UserId): F[Option[User]]

    /** Add a new user */
    def addUser(user: User): F[Unit]

    /** Update a user */
    def updateUser(user: User): F[Unit]
  }

  /**
    * The book repository defines an algebra for manipulating books
    */
  trait BookRepository[F[_]] {

    /** Get the list of all books */
    def listBooks(): F[List[Book]]

    /** Get a book based on its id. Resulting in None if the book doesn't exist*/
    def getBook(id: BookId): F[Option[Book]]

    /** Add a a book */
    def addBook(book: Book): F[Unit]
  }

  /**
    * The reading list service defines the core domain operations of our application.
    *
    * 1. Get the reading list for a user
    * 2. Add a book to a users reading list
    * 3. Remove a book from a users reading list
    */
  trait ReadingListService[F[_]] {
    def getReadingList(user: UserId): F[ReadingList]
    def addToReadingList(userId: UserId, bookId: BookId): F[Unit]
    def removeFromReadingList(userId: UserId, bookId: BookId): F[Unit]
  }
}
