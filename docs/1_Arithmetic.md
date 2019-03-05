
# Arithmetic Expressions 

Tagless final can be thought of as an alternative way to encode the [interpreter pattern](https://en.wikipedia.org/wiki/Interpreter_pattern). The
traditional encoding (is scala at least) is to define the set of operations in your language
as an Algebraic Data Type ([ADT](https://en.wikipedia.org/wiki/Algebraic_data_type))

For example, we can define a set of operation that allow a counter to be incremented or decremented.

```scala
sealed trait Counter
case class Increment(by: Int) extends Counter
case class Decrement(by: Int) extends Counter
```

The final encoding of this increment / decrement algebra is

```scala
trait Counter[A] {
   def increment(by: Int): A
   def decrement(by: Int): A
}
```

In this exercise we will implement an expression language for integer 
arithmetic, using the traditional ADT approach, and the tagless final approach. 

The language must support four operations;

1. `lit` - constructs a new integer literal
2. `add` - sums two integer expressions
3. `sub` - subtracts two integer expressions
4. `mul` - multiplies two integer expressions

## Exercise 1 &ndash; Define an evaluator for the ADT based encoding of arithmetic expressions.

- In [`Arithmetic.scala`](../arithmetic/src/main/scala/arithmetic/Arithmetic.scala) notice that the arithmetic operations are described
  as set of case classes that extend `IExp` (short for *initial expression*)

- Define the `evaulate` function, that evaluates an expression defined by `IExp`
  
- Run the tests using `sbt part1-arithmetic/test` to verify your implementation works. <br>
  Note: you will still have one failing test, that tests the next exercise.

## Exercise 2 &ndash; Define an evaluator for the final encoding of arithmetic expressions

- In [`Arithmetic.scala`](../arithmetic/src/main/scala/arithmetic/Arithmetic.scala) notice that the arithmetic operations are described 
  by the `FExp` trait (short for *final expression*)

- Define the appropriate methods in the `Evaluate` object, to allow an expression defined by `FExp` to be evaluated.

- Run the tests using `sbt arithmetic/test` to verify your implementation works. 

## Exercise 3 &ndash; Show that `IExp` and `FExp` are equivalent

Having implemented this expression language in both the initial (ADT based) encoding and the final (trait based) encoding. 
It should be relatively clear that they are equivalent to each other. The goal of this exercise is to write a test case
that demonstrates **directly** that they are equivalent.

To that end, notice that there is a function called `toFinal` that can convert an `IExp` into an `FExp` and also
an object called `Reify` that can convert an `FExp` into an `IExp`. 
Study the implementation of `toFinal` and `Reify` and convince yourself they are correct.
 
### Part A &ndash; `IExp` -> `FExp`

- There is an example expression called `initialExp` that is defined using the `IExp` algebra. 
  Evaluate it to get a value, convert it to the `FExp` encoding, evaluate the final encoding, and verify the values are the same. 
  There is a stub test in [`ArithmeticSpec.scala`](../arithmetic/src/test/scala/arithmetic/ArithmeticSpec.scala)

- Run the tests using `sbt arithmetic/test` to verify your implementation works. 

### Part B &ndash; `FExp` -> `IExp`

- There is an example expression called `finalExp` that is defined using the `FExp` algebra. Evaluate it to get a value, 
  convert it to the `IExp` encoding, evaluate it, and verify the values are the same. There is a stub test in `arithmetic.ArithmeticSpec.scala`
  
- Run the tests using `sbt arithmetic/test` to verify your implementation works. 

## Next

- Run `groll next` to reveal the solution.
- Run `groll next` once more to move on to the next exercise.
