package part2e

import cats.Parallel
import cats.implicits._
import org.scalatest.{MustMatchers, WordSpec}
import part2e.domain._
import part2e.interpreters.{NoSuchUserException, ReadingListServiceCompiler}

import scala.util.{Failure, Try}

class ReadingListServiceCompilerSpec extends WordSpec with MustMatchers {

    private implicit val parallelInstanceForTry: Parallel[Try, Try] = Parallel.identity[Try]

    private def fixture = new {
        val userRepository = new InMemoryUserRepository
        val bookRepository = new InMemoryBookRepository
        val books = List(
            Book(id = Some(BookId("1")), title = "A Game of Thrones", author = "George R. R. Martin"),
            Book(id = Some(BookId("2")), title = "A Clash of Kings", author = "George R. R. Martin")
        )

        val user = User(
            id = Some(UserId("1")),
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


    "getReadingList" should {
        "Return the reading list for a given user" in {
            val f = fixture
            import f._

            val readingList = readingListService.getReadingList(UserId("1"))
            readingList.get.user mustBe user
            readingList.get.books mustBe books
        }

        "Result in NoSuchUserException if the specified user doesn't exist" in {
            val f = fixture
            import f._

            val Failure(e) = readingListService.getReadingList(UserId("2"))

            e mustBe a[NoSuchUserException]
        }
    }
}