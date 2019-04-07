# Parallelism

We are fetching many books based on a list of ids, so it would generally make sense to perform the lookups in parallel.

## Prerequisites

A **Monad** expresses sequential computations that may depend on each other, i.e. do this, then do that, then do the 
other thing, in that order. Additionally, the result of each step can be used in the following steps.

e.g.

```scala
// Dependent computations in an imperative style
val x = 1
val y = x + 1 // the value of y, depends on the value of x

// Dependent computations in a monadic style
for {
  x <- Some(1)
  y <- Some(x + 1)
} yield y
```

An **Applicative** expresses independent computations, i.e. do this, that and the other thing, but they must be 
independent and can be done in any order.

e.g.

```scala
// Independent computations in an imperative style
val x = 3 * 2
val y = 10 + 1

// Independent computations using an Applicative
(Some(1), Some(2), Some(3)).mapN(_ + _ + _) // = Some(6)
(Some(1), Some(2), None).mapN(_ + _ + _)    // = None
```

A `Monad` is strictly more powerful than an `Applicative`, which means that the `Applicative` operations can be defined in 
terms of the `Monad` operations. However, since the primary combining operation defined on `Applicative` is implemented 
in terms of `flatMap` for any type that also forms a `Monad`, the independent operations are actually run sequentially.

Such monadic types sometimes have a different Applicative interpretation, that allows the independent operations to be 
evaluated in parallel. To allow this alternate `Applicative` definition to be used, cats defines the 
[Parallel](https://typelevel.org/cats/typeclasses/parallel.html) typeclass.

## Read The Code

- Notice that we have added a dependency on [cats-par](https://christopherdavenport.github.io/cats-par/) to the build 
  configuration. This is a micro-library, that make it possible to use the `cats.Parallel` typeclass in context bounds,
  similar to how we defined the `Throwing` type alias previously.

- We have also discarded our `EitherT + State` monad transformer test implementation, because it is quite complicated and
  cumbersome to add parallelism to a monad transformer stack using the `Parallel` type class.

## Exercise
  
- **Add** a `cats.temp.par.Par` constraint to `F` in `ReadingListServiceCompiler`, so that we can use the Applicative 
  instance that will execute the operations in parallel. 
   - Tip: You can add multiple constraints like this `F[_] : Monad : SomethingElse`

- Change the traverse call in `ReadingListServiceCompiler.getReadingList` to use the parallel variant called `parTraverse`

- Notice that we now have compilation errors in our test cases. Use `Parallel#identity` to fix the issue in the tests.
   - We are creating a `Parallel` instance for a type, `Try`, that doesn't support parallel composition. 
   - This is useful when testing, but should not be used in production code.
   - In the next section we will use a data type that supports actual parallelism.
   
- Run `sbt reading-list/test` to test your implementation

## Next

- Run `groll next` to reveal the solution.
- Run `groll next` once more to move on to the next exercise.