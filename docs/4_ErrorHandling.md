# Handling Errors

In your implementation of `getReadingList`, you may have noticed that it is not clear what to do if there is
no user returned when the user is looked up by id. We would like to indicate that this is an error.

To do that, in this exercise we change the constraint on the `F[_]` parameter to `MonadError`, which essentially means
that it must be a monad with error handling capabilities. This allows us to use `raiseError`

## Prerequisites

- Scala defines several error handling functions on the concrete effect types that support error handling, such as
  `scala.util.Try` and `scala.concurrent.Future`. Examples of error handling methods are `recover`, `recoverWith`, 
  `ensure` and `rethrow`. Cats effect abstracts over effects that support raising and handling errors, 
  using a typeclass called `MonadError`.

## Read The Code

- `Throwing` is a type alias defined in [reading.scala](../reading-list/src/main/scala/reading/package.scala) 
   for `MonadError[F[_], Throwable]` allowing it to be used as a context bound. This is can be used to place a 
   constraint on our effect type, requiring that it can handle errors.

- The implementation of `StateUserRepository` and `StateBookRepository` that use the `State` monad have been
  move to [state.scala](../reading-list/src/test/scala/reading/state.scala) and additionally have been extended 
  to support error handling using the `EitherT` monad transformer. Notice that the implementation has become
  significantly more complex to support error handling.

- The implementation of `InMemoryUserRepository` and `InMemoryBookRepository` have been modified to use `Try` instead of
  `Id` since `Id` does not have a `MonadError` instance.

## Exercise

- Raise a `NoSuchUserException` in `ReadingListServiceCompiler.getReadingList` when there is no user for the 
  specified `UserId`.

- Run `sbt reading-list/test` to test your implementation

## Next

- Run `groll next` to reveal the solution.
- Run `groll next` once more to move on to the next exercise.