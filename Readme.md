# Tagless Final and Abstract Effects

## Introduction

One of the core tenants of functional programming, is that the effects our programs use should be expressed in the type signatures of our functions. 

For example 

- `scala.Option` is an effect that indicates a value may, or may not be present.
- `scala.util.Try` is an effect that represents a computation that may fail, with an exception

As with all aspects of functional programming, we strive for composability. In Haskell a common approach to composing these effect types in **monad transformers**. However, in Scala this approach suffers from poor performance, and poor type inference.

One solution to this that has recently gained popularity is the **Free Monad**, which is an effect absent of interpretation. i.e. it has no inherent effect, but its effect can be defined in an arbitrary way by evaluating it using an interpreter. The free monad also has some draw backs. Performance can be an issue and there is a lot of boiler plate.

An alternative is to use **Tagless Final** which can be used in most of the same scenarios as the Free Monad, but achieves better performance, less boiler plate and is more simple.

## The Workshop

The workshop is made up of practical exercises that introduce the tagless final style, and demonstrate how this style can be used in real world use cases.

#### Part 1

The first is a practical introduction to the concept of **Tagless Final Interpreters** with a simple first order language that 
can express integer arithmetic. Attendees will implement a simple evaluator for this language in the *tagless final* style, 
and show that the final and initial encodings are equivalent.

#### Part 2

The second part consists of several exercises that build out a simple reading list application using the tagless final style.

The exercises will demonstrate;

- The use of higher-order tagless final encoding
- Composing tagless final algebras
- Using typeclass constraints to abstract over what capabilities are needed in different scenarios. Including, sequential computations with `cats.Monad`, 
  error handling with `cats.MonadError` and parallelism with `cats.Parallel`

## Setup

**Building**

- [Java 8](https://java.com/en/download/) & [SBT](http://www.scala-sbt.org/1.0/docs/Setup.html) must be installed. 
- Clone this repository `git clone https://github.com/eli-jordan/tagless-final-jam-2018`
- Run `sbt update` in the root of the project to download and cache all the dependencies.
- Import the project into your favorite editor, see below for options.

**Editors**

There are several editors for scala

- [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/) + scala plugin is a good option. 
- You can also use [Eclipse Scala IDE](http://scala-ide.org/), or
- [Ensime](https://github.com/ensime) 

## Exercises

- [Part1](./part1-arithmetic/Readme.md) &ndash; Interpreting integer arithmetic
- [Part2a](./part2a-domain-model/Readme.md) &ndash; Defining the domain model for the reading list application
- [Part2b](./part2b-composing-algebras/Readme.md) &ndash; Composing algebras with the tagless final encoding
- [Part2c](./part2c-error-handling) &ndash; Adding error handling
- [Part2d](./part2d-parallelism) &ndash; Adding parallelism
- [Part2e](./part2e-real-repository) &ndash; Adding a real repository implementation, using the file system.

## Answers

There are solutions to each exercise in the the `/answers` subdirectory. 

## References

- [Lecture notes](http://okmij.org/ftp/tagless-final/course/lecture.pdf) by Oleg Kiselyov that describe tagless final in detail, 
  using Haskell.
- The [cats](https://typelevel.org/cats/) documentation
- [Advanced Scala with Cats](https://underscore.io/training/courses/advanced-scala/) 
- A description of the [Cats IO monad](https://typelevel.org/blog/2017/05/02/io-monad-for-cats.html) and the effect abstractions provided by `cats-effect`

