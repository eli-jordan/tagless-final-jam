import cats.implicits._
import play.api.libs.json.{Json, OFormat}

object serialization {

    import domain._

    implicit lazy val UserIdFormat: OFormat[UserId] = Json.format[UserId]
    implicit lazy val UserFormat: OFormat[User] = Json.format[User]

    implicit lazy val BookIdFormat: OFormat[BookId] = Json.format[BookId]
    implicit lazy val BookFormat: OFormat[Book] = Json.format[Book]

    trait Codec[T] {
        def encode(v: T): Either[Throwable, String]

        def decode(s: String): Either[Throwable, T]
    }

    object BookCodec extends Codec[Book] {
        override def encode(v: Book): Either[Throwable, String] = Either.catchNonFatal {
            Json.stringify(Json.toJson(v))
        }

        override def decode(s: String): Either[Throwable, Book] = Either.catchNonFatal {
            Json.parse(s).as[Book]
        }
    }

    object UserCodec extends Codec[User] {
        override def encode(v: User): Either[Throwable, String] = Either.catchNonFatal {
            Json.stringify(Json.toJson(v))
        }

        override def decode(s: String): Either[Throwable, User] = Either.catchNonFatal {
            Json.parse(s).as[User]
        }
    }
}
