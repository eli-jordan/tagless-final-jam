
# Arithmetic Expressions 

Tagless final can be thought of as an alternative way to encode the interpreter pattern. The
traditional encoding (is scala at least) is to define the set of operations in your language
as an ADT (Algebraic Data Type)

e.g.

```scala
sealed trait Operations
case class Increment(by: Int) extends Operations
case class Decrement(by: Int) extends Operations
```

The final encoding of this increment / decrement algebra is

```scala
trait Operations[A] {
   def increment(by: Int): A
   def decrement(by: Int): A
}
```

To illuminate this intuition, in this exercise we will implement an expression language for integer 
arithmetic, using the traditional ADT approach, and the tagless final approach. 

The language must support four operations;

1. `lit` - constructs a new integer literal
2. `add` - sums two integer expressions
3. `sub` - subtracts two integer expressions
4. `mul` - multiplies two integer expressions

## Exercise 1 &ndash; Define an evaluator for the ADT based encoding of arithmetic expressions.

- In [`Arithmetic.scala`](./src/main/scala/Arithmetic.scala) notice that the arithmetic operations are described
  as set of case classes that extend `IExp` (short for *initial expression*)

- Define the `evaulate` function, that evaluates an expression defined by `IExp`
  
- Run the tests using `sbt part1-arithmetic/test` to verify your implementation works. <br>
  Note: you will still have one failing test, that tests the next exercise.

## Exercise 2 &ndash; Define an evaluator for the final encoding of arithmetic expressions

- In [`Arithmetic.scala`](./src/main/scala/Arithmetic.scala) notice that the arithmetic operations are described 
  by the `FExp` trait (short for *final expression*)

- Define the appropriate methods in the `Evaluate` object, to allow an expression defined by `FExp` to be evaluated.

- Run the tests using `sbt part1-arithmetic/test` to verify your implementation works. 

## Exercise 3 &ndash; Show that `IExp` and `FExp` are equivalent

Having implemented this expression language in both the initial (ADT based) encoding and the final (trait based) encoding. It should be relatively clear that these encodings are equivalent.
 
To prove this we implement the `toFinal` evaluation function for the `IExp` data type, that translates from the `IExp` (initial) encoding to the `FExp` (final) encoding. 

Notice that the `toFinal` function takes an `IExp` value that it is translating, and also an `FExp` value to interpret into. 
 
Additionally, we implement the `Reify` evaluator for `FExp` that translates in the other direction.

- Study the implementation of `toFinal` and `Reify` and convince yourself they are correct.
 
- **`IExp` -> `FExp`** : There is an example expression called `initialExp` that is defined using the `IExp` algebra. Evaluate it to get a value, convert it to the `FExp` encoding, evaluate the final encoding, and verify the values are the same. There is a stub test in `ArithmeticSpec.scala`

- **`FExp` -> `IExp`** : There is an example expression called `finalExp` that is defined using the `FExp` algebra. Evaluate it to get a value, convert it to the `IExp` encoding, evaluate it, and verify the values are the same. There is a stub test in `ArithmeticSpec.scala`
