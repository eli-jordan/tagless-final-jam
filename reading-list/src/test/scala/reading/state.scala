package reading
import cats.data.{EitherT, State}
import reading.domain._
import reading.interpreters._

object state {
  type BookState = Map[BookId, Book]
  type UserState = Map[UserId, User]

  /**
    * In the previous implementation of [[StateBookRepository]] and [[StateUserRepository]] we
    * used State[BookState, ?] for the StateBookRepository and State[UserState, ?] for
    * StateUserRepository.
    *
    * Now that we are composing these two algebras in [[ReadingListServiceCompiler]]
    * we need a unified state type. So we now use State[(BookState, UserState), ?]
    */
  type AppState = (BookState, UserState)

  /**
    * Since we now require the ability to raise and handle errors in [[ReadingListServiceCompiler]]
    * using just the state monad is no longer sufficient. We add error handling by composing
    * the EitherT monad transformer with the State monad.
    */
  type AppStateM[A] = EitherT[State[AppState, ?], Throwable, A]

  class StateBookRepository extends BookRepository[AppStateM] {
    override def listBooks(): AppStateM[List[Book]] = EitherT.right {
      State.get[(BookState, UserState)].map { state =>
        state._1.values.toList
      }
    }

    override def getBook(id: BookId): AppStateM[Option[Book]] = EitherT.right {
      State.get[(BookState, UserState)].map { state =>
        state._1.get(id)
      }
    }

    override def addBook(book: Book): AppStateM[Unit] = EitherT.right {
      State.modify[(BookState, UserState)] { state =>
        (state._1 + (book.id.get -> book), state._2)
      }
    }
  }

  class StateUserRepository extends UserRepository[AppStateM] {

    override def getUser(id: UserId): AppStateM[Option[User]] = EitherT.right {
      State.get[(BookState, UserState)].map(_._2.get(id))
    }

    override def addUser(user: User): AppStateM[Unit] = EitherT.right {
      State.modify[(BookState, UserState)] { state =>
        (state._1, state._2 + (user.id.get -> user))
      }
    }

    override def updateUser(user: User): AppStateM[Unit] = EitherT.right {
      State.modify[(BookState, UserState)] { state =>
        (state._1, state._2 + (user.id.get -> user))
      }
    }
  }
}
