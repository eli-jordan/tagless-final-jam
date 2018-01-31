import cats.Id
import cats.implicits._
import domain._
import interpreters._
import org.scalatest.{MustMatchers, WordSpec}

import scala.util.Failure

class ReadingListServiceCompilerSpec extends WordSpec with MustMatchers {

    val userId = UserId("1")
    private def fixture = new {
        val userRepository = new InMemoryUserRepository
        val bookRepository = new InMemoryBookRepository

        val book1 = Book(id = Some(BookId("1")), title = "A Game of Thrones", author = "George R. R. Martin")
        val book2 = Book(id = Some(BookId("2")), title = "A Clash of Kings", author = "George R. R. Martin")
        val books = List(book1, book2)

        val user = User(
            id = Some(userId),
            firstName = "Eli",
            lastName = "Jordan",
            books = List(
                BookId("1"),
                BookId("2")
            )
        )

        userRepository.addUser(user)
        books.traverse(bookRepository.addBook)

        val readingListService = new ReadingListServiceCompiler(userRepository, bookRepository)
    }

    "ReadingListService" should {
        "Return the reading list for a given user" in {
            val f = fixture
            import f._

            val readingList = readingListService.getReadingList(UserId("1"))
            readingList.get.user mustBe user
            readingList.get.books mustBe books
        }

        "Add a book to the reading list" in {
            val f = fixture
            import f._

            val book = Book(id = Some(BookId("3")), title = "Fake Book", author = "Fake Author")
            val readingList = for {
                _ <- bookRepository.addBook(book)
                _ <- readingListService.addToReadingList(userId, book.id.get)
                list <- readingListService.getReadingList(userId)
            } yield list

            readingList.get.books must contain(book)
        }

        "Remove a book from the reading list" in {
            val f = fixture
            import f._

            val readingList = for {
                _ <- readingListService.removeFromReadingList(userId, book1.id.get)
                list <- readingListService.getReadingList(userId)
            } yield list

            readingList.get.books must not contain book1
        }

        "Result in NoSuchUserException if the specified user doesn't exist" in {
            val f = fixture
            import f._

            val Failure(e) = readingListService.getReadingList(UserId("2"))

            e mustBe a[NoSuchUserException]
        }
    }
}