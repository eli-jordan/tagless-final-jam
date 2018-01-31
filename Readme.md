# Tagless Final and Abstract Effects

## Introduction

The workshop is made up of practical exercises that introduce the tagless final style, and demonstrate how 
this style can be used in real world use cases.

`cats` and `cats-effect` are the foundational functional programming libraries used in the exercises.

The workshop is in two parts;

#### Part 1

The first is a practical introduction to the concept of **Tagless Final Interpreters** with a simple first order language that 
can express integer arithmetic. Attendees will implement a simple evaluator for this language in the *tagless final* style, 
and show that the final and initial encodings are equivalent.

#### Part 2

The second part consists of several exercises that build out a simple reading list application using the tagless final style.

The exercises will demonstrate;

- The use of higher-order tagless final encoding
- Composing tagless final algebras
- Using typeclass constraints to abstract over what capabilities are needed in different scenarios. Including, sequential computations with `cats.Monad`, error handling with `cats.MonadError` and parallelism with `cats.Parallel`

## Setup

- SBT must be installed
- Clone this repository
- Run `sbt update` to pull down all dependencies

## Exercises

- [Part1](./part1-arithmetic/Readme.md) &ndash; Interpreting integer arithmetic
- [Part2a](./part2a-domain-model/Readme.md) &ndash; Defining the domain model for the reading list application
- [Part2b](./part2b-composing-algebras/Readme.md) &ndash; Composing algebras with the tagless final encoding
- [Part2c](./part2c-error-handling) &ndash; Adding error handling
- [Part2d](./part2d-parallelism) &ndash; Adding parallelism
- Part2e &ndash; No finished yet

## References

- [Lecture notes](http://okmij.org/ftp/tagless-final/course/lecture.pdf) by Oleg Kiselyov that describe tagless final in detail, 
  using Haskell.
- A description of the [Cats IO monad](https://typelevel.org/blog/2017/05/02/io-monad-for-cats.html) and the effect abstractions provided by `cats-effect`
- The [cats](https://typelevel.org/cats/) documentation

