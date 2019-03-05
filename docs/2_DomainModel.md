# Defining Our Domain Model

In the remaining sections of this workshop, we will be building up a language to implement a reading list application.
The application allows;

1. Users to be added and removed
2. Books to be added and removed
3. Users to add / remove books from their reading list

## Prerequisite

- This section requires that you have a basic understanding of higher-kinded types. Types variables that look like 
  `F[_]` or `F[_, _]` etc. If you are not familiar with this syntax, [this](https://typelevel.org/blog/2016/08/21/hkts-moving-forward.html)
  blog post is a good introduction to the syntax, and why this concept is import when using functional programming.

- In short, higher-kinded types provide the ability to abstract over the shape of types. 
  - `F[_]` is a type parameter, that must be filled by a type that takes *one* parameter such as 
    `List` or `Option` or `Future` or `IO` and many others.
  - `F[_]` is sometimes referred as a *type constructor* because it can be passed a type, and will generate a new types.
     e.g. When `F[_]` is set to `IO`, then `F[String]` is equivalent to `IO[String]` which is a first-order type.

## Read The Code

The data types used to represent out reading list application are already defined for you. Take a look at the 
`User` `Book` and `ReadingList` data types in [domain.scala](../reading-list/src/main/scala/reading/domain.scala)
  
The operations we will support are also defined using the tagless final encoding. 

- `UserRepository` supports accessing and updating users.
- `BookRepository` supports accessing and updating books.
- `ReadingListService` exposes the core operations that our reading list application wants to expose.
     
Notice that each of these traits have a similar structure.
     
1. They each take a higher-kinded (i.e. `F[_]`) type argument that represents the effect type the operations will be run 
   within. Effect types are things like `Future`, `Task`, `IO`, `Id` etc, and are a way to express that there is an 
   effect present at the type level.
     
2. Rather than simply returning a value e.g. `getUser(id: UserId): User` they return a value wrapped in an effect type 
   e.g.`getUser(id: UserId): F[User]` to indicate that there is some kind of effect required to perform the operation. 
   For example to get a user by id, we would generally go to a database or other persistent storage, this fact is 
   captured by the effect.
   
## Exercise

The goal of this exercise is to get some experience implementing interpreters for the tagless-final a tagless-final 
language. We will implement a fake interpreter that can be used when writing tests.

There are two options for this exercise

### Part A

**Option 1** &ndash; If you are familiar with the [state monad](https://typelevel.org/cats/datatypes/state.html) you 
  should proceed with this option.
  
- Implement a fake `BookRepository` that simply saves the book instance in a scala `Map` and uses the `State` monad. 
- In [RepositorySpec.scala](../reading-list/src/test/scala/reading/RepositorySpec.scala) a fake `UserRepository[UserStateM]` 
  named `StateUserRepository` has already been defined to illustrate the implementation techniques.
- Since we are now implementing the interpreter, we must pick a concrete type for `F[_]`, here we choose 
 `type UserStateM[A] = State[Map[UserId, User], A]` which means that our *effect* is stateful computation.
  
**Option 2** &ndash; If you are not familiar with the [state monad](https://typelevel.org/cats/datatypes/state.html) you
  should proceed with this option.
  
- Implement a fake `BookRepository` that simply saves the book instance in a scala `Map` and uses the `Id` monad. 
- In [RepositorySpec.scala](../reading-list/src/test/scala/reading/RepositorySpec.scala) a fake `UserRepository[Id]` 
  named `InMemoryUserRepository` has already been defined to illustrate the implementation techniques.
- Since we are now implementing the interpreter, we must pick a concrete type for `F[_]`, here we choose `Id`
  which means *no effect* and is defined as `type Id[A] = A`
  
### Part B

- Define a test case that adds a book, then verifies that it is listed     
- Run the tests using `sbt reading-list/test` to verify your implementation.

## Next

- Run `groll next` to reveal the solution.
- Run `groll next` once more to move on to the next exercise.