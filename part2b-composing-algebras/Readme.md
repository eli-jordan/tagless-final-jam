# Implementing ReadingListService

In this part we will implement the `ReadingListService` that we defined previously. To do this we will leverage
the capabilities of both `UserRepository` and `BookRepository`. 

For those familiar with the free monad, you will know that combining algebras uses the "Datatype a la carte" approach, 
which involves some complex implicit machinery to get working. In the tagless final approach it is trivial, simply 
define a function or class that takes other algebras as parameters. 

## Read The Code

- You will notice there is a new file [interpreters.scala](./src/main/scala/interpreters.scala) where there is a partial
  implementation of `ReadingListService` called `ReadingListServiceCompiler`
  
- It is termed a compiler, because it can be viewed as compiling the `ReadingListService` instruction set into
  the `UserRepository + BookRepository` instruction set.
      
- Notice that there is a context bound on the `F` type parameter i.e. `F[_] : Monad`. This means that what ever `F` we choose when
  instantiating the compiler, it must have a Monad instance defined. Having an instance of `Monad` means that we can use `map`
  and `flatMap` etc to transform and sequence computations in the context of the effect type. Having these operations available also
  means that we can use the type in a for-comprehension which makes for very readable code.
  
## Exercise

- `ReadingListService.addToReadingList` and `ReadingListService.removeFromReadingList` are already implemented. Notice that
   they are using a for-comprehension. This is possible because there is a `Monad` constraint on `F` 

- Implement `ReadingListService.getReadingList`

- Run `ReadingListServiceCompilerSpec` to ensure your implementation is correct
