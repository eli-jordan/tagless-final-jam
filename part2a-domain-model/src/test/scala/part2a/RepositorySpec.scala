package part2a


import cats._
import cats.implicits._
import org.scalatest.{MustMatchers, WordSpec}
import part2a.domain._

// Exercise -
// 1. Implement InMemoryBookRepository
// 2. Implement a test that adds a book, then lists the books, and asserts the added book is listed.

class InMemoryUserRepository extends UserRepository[Id] {
    private var users = Map.empty[UserId, User]

    override def getUser(id: UserId): Id[Option[domain.User]] =
        users.get(id)

    override def addUser(user: User): Id[Unit] =
        users = users + (user.id.get -> user)

    override def updateUser(user: User): Id[Unit] =
        users = users + (user.id.get -> user)
}

class InMemoryBookRepository extends BookRepository[Id] {
    override def listBooks(): Id[List[Book]] = ???
    override def getBook(id: BookId): Id[Option[Book]] = ???
    override def addBook(book: Book): Id[Unit] = ???
}

class RepositorySpec extends WordSpec with MustMatchers {
    val userId = UserId("1")
    val user = User(
        id = Some(userId),
        firstName = "Eli",
        lastName = "Jordan",
        books = List.empty
    )

    val bookId = BookId("1")
    val book = Book(
        id = Some(bookId),
        title = "A Game of Thrones",
        author = "George R. R. Martin"
    )

    "UserRepository" should {
        "added user can be retrieved" in {
            val repository = new InMemoryUserRepository
            val userOpt = for {
                _ <- repository.addUser(user)
                user <- repository.getUser(user.id.get)
            } yield user

            userOpt.isDefined mustBe true
        }

        "user can be updated" in {
            val repository = new InMemoryUserRepository
            val userOpt: Id[Option[User]] = for {
                _ <- repository.addUser(user)
                _ <- repository.updateUser(user.copy(firstName = "Fred"))
                user <- repository.getUser(userId)
            } yield user

            userOpt.isDefined mustBe true
            userOpt.get.firstName mustBe "Fred"
        }
    }

    "BookRepository" should {
        "added book can be retrieved" in {
            val repository = new InMemoryBookRepository
            val bookOpt = for {
                _ <- repository.addBook(book)
                book <- repository.getBook(bookId)
            } yield book

            bookOpt.isDefined mustBe true
        }

        "added book can be listed" in {
            // Define this test case
            ???
        }
    }
}
