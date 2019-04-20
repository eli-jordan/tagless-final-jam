package reading

import java.nio.file.{Path, Paths}

import cats.effect._
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpApp, HttpRoutes}
import reading.domain._
import reading.fs._
import reading.interpreters.ReadingListServiceCompiler

/**
  * This "module" is where we wire up the various repositories and services that make up the
  * reading list application.
  *
  * If you have seen other examples of using tagless-final, you may have seen the service implementations
  * passed as implicit parameters, rather than as explicit parameters. I prefer explicitly passing the service
  * instances for the following reasons;
  *
  * 1. Its easier to follow where a given instance is used when reading the code
  * 2. The instances are almost always not coherent (i.e. there are multiple implementations of the trait for a given F[_])
  * 3. It uses first class constructs of the language, that can be composed in more flexible ways than implicits.
  *
  * Having said that, both approaches work so use what you personally prefer.
  */
trait Module {
  def dataDir: Path
  implicit def contextShift: ContextShift[IO]
  lazy val FileService                = new FileServiceIO
  lazy val BookRepository             = new FileBackedBookRepository[IO](Paths.get(dataDir.toString, "books"), FileService)
  lazy val UserRepository             = new FileBackedUserRepository[IO](Paths.get(dataDir.toString, "users"), FileService)
  lazy val ReadingListServiceCompiler = new ReadingListServiceCompiler[IO](UserRepository, BookRepository)

  lazy val ReadingListHttpService =
    new ReadingListHttpService[IO](UserRepository, BookRepository, ReadingListServiceCompiler)

  lazy val HttpApp: HttpApp[IO] = ReadingListHttpService.service.orNotFound
}

/**
  * This is the main entry point of our application. It instantiates the [[Module]] providing runtime values
  * such as the directory where data is stored, then starts a http4s server.
  */
object Server extends IOApp { self =>
  override def run(args: List[String]): IO[ExitCode] = {
    val dataDirValue = Paths.get(sys.env.getOrElse("DATA_DIR", "./reading-list/src/main/resources/data"))
    val module = new Module {
      override def dataDir: Path                           = dataDirValue
      override implicit def contextShift: ContextShift[IO] = self.contextShift
    }

    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(module.HttpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}

/**
  * This is where we define the HTTP endpoints that are available in the ReST API for our reading list application.
  */
class ReadingListHttpService[F[_]: Sync](
    userRepository: UserRepository[F],
    bookRepository: BookRepository[F],
    readingListService: ReadingListServiceCompiler[F])
  extends Http4sDsl[F] {

  val service: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "book" =>
      for {
        books    <- bookRepository.listBooks()
        response <- Ok(books.asJson)
      } yield response

    case req @ POST -> Root / "book" =>
      for {
        book     <- req.decodeJson[Book]
        _        <- bookRepository.addBook(book)
        response <- Ok(book.asJson)
      } yield response

    case req @ POST -> Root / "user" =>
      for {
        user     <- req.decodeJson[User]
        _        <- userRepository.addUser(user)
        response <- Ok(user.asJson)
      } yield response

    case GET -> Root / "reading-list" / userId =>
      for {
        list     <- readingListService.getReadingList(UserId(userId))
        response <- Ok(list.asJson)
      } yield response
  }
}
