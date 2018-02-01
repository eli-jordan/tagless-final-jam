# Parallelism

We are fetching many books based on a list of ids, so it would generally make sense to perform the lookups in parallel.

## A Note On Monad, Applicative and Parallel

A **Monad** expresses sequential computations that may depend on each other, i.e. do this, then do that, then to the other thing, in that order. Additionally, the result of each step can be used in the following steps.

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

An **Applicative** expresses independent computations, i.e. do this, that and the other thing, but they must be independent and can be done in any order.

e.g.

```scala
// Independent computations in an imperative style
val x = 3 * 2
val y = 10 + 1

// Independent computations using an Applicative
(Some(1), Some(2), Some(3)).mapN(_ + _ + _) // = Some(6)
(Some(1), Some(2), None).mapN(_ + _ + _)    // = None
```

A Monad is strictly more powerful than an Applicative, which means that the Applicative operations can be defined in terms of the Monad operations. However, this definition means that the independent operations are actually evaluated sequentially.

Such monadic types sometimes have a different Applicative interpretation, that allows the independent operations to be evaluated in parallel. To allow this alternate Applicative definition to be used, cats defines the `Parallel` typeclass.

## Exercise
  
- **Add** a `cats.Parallel[F, F]` constraint to `F` in `ReadingListServiceCompiler`, so that we can use the Applicative instance that will execute the operations in parallel. 
   
   - There is a convenient type alias called `ParallelId` in `service.scala` that allows you to define the constraint using a context bound.
   - You can add multiple constraints like this `F[_] : Monad : SomethingElse`

- Change the traverse call in `ReadingListServiceCompiler.getReadingList` to use the parallel variant called `parTraverse`

- Notice that we now have compilation errors in our test cases. Use `Parallel#identity` to fix the issue.
