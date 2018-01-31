# Parallelism

We are fetching many books based on a list of ids, so it would generally make sense to perform the lookups
in parallel.

## A Note On Monad, Applicative and Parallel

A Monad expresses sequential computations that may depend on each other, i.e. do this, then do that,
then to the other thing, in that order. Additionally, the result of each step can be used in the following steps.

e.g.
```scala
val x = 1
val y = x + 1 // the value of y, depends on the value of x
```

An Applicative expresses independent computations, i.e. do this, that and the other thing, but they must
be independent and can be done in any order.

e.g.
```scala
val x = 3 * 2
val y = 10 + 1
```

A Monad is strictly more powerful than an Applicative, which means that the Applicative operations can be
defined in terms of the Monad operations. However, this definition means that the independent operations
are actually evaluated sequentially.

Such monadic types sometimes have a different Applicative interpretation, that allows the independent operations
to be evaluated in parallel. To allow this alternate Applicative definition to be used, cats defines the `Parallel` typeclass.

## Exercise
  
- Add a `cats.Parallel[F, F]` constraint to `F`, so that we can fetch the books in parallel

- Change the traverse call to use the parallel variant

- Notice that we now have compilation errors in our test cases. Use `Parallel#identity` to fix the issue.
