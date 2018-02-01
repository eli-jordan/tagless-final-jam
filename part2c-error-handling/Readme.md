# Handling Errors

In your implementation of `getReadingList`, you may have noticed that it is not clear what to do if there is
no user returned when the user is looked up by id. We would like to indicate that this is an error.

To do that, in this exercise we change the constraint on the `F[_]` parameter to `MonadError`, which essentially means that it must
be a monad with error handling capabilities. This allows us to use `raiseError`

## Read The Code

`Throwing` is a type alias defined in [service.scala](./src/main/scala/part2c/service.scala) for `MonadError[F[_], Throwable]` allowing it to be 
used as a context bound. This is placing a constraint on our effect type, indicating that it must have a Monad instance that supports
error handling.

Having an instance of `MonadError` means that in addition to the monad operators, there are also error handing operators
such as `recover`, `recoverWith`, `ensure`, `rethrow` etc. The operators on `Try` and `Future` that support error handing 
provide a good intuition for what can be done with a `MonadError` instance.

Also notice that since we have added this stronger constraint, we can no longer use `Id` as our effect type in the test
interpreters. We require a type that has a `MonadError` instance, and `Try` is a simple example that is useful in testing.

## Exercise

- Raise a `NoSuchUserException` in `ReadingListServiceCompiler.getReadingList` when there is no user for the specified `UserId`.

- Run `sbt part2c-error-handling/test` to test your implementation