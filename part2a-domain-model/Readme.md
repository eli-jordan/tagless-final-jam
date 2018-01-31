# Defining Our Domain Model

If the remaining sections of this workshop, we will be building up a language to implement a reading list application.
The application allows;

1. Users to be added and removed
2. Books to be added and removed
3. Users to add / remove books from their reading list

## Read The Code

The data types used to represent this are already defined for you, take a look at the `User` `Book` and `ReadingList`
in data types in [domain.scala](./src/main/scala/domain.scala)
  
The operations we will support are also defined using the tagless final encoding. 

- `UserRepository` supports accessing and updating users.
- `BookRepository` supports accessing and updating books.
- `ReadingListService` exposes the core operations that our reading list application wants to expose.
     
Notice that each of these traits have a similar structure.
     
1. They each take a higher-kinded (i.e. `F[_]`) type argument that represents the effect type the operations will be run within.
   Effect types are things like `Future`, `Task`, `IO`, `Id` etc, and are a way to express that there is an effect present at 
   the type level.
   
   Aside: If you are not familiar with higher-kinded types. They provide the ability to abstract over the shape of types, for example
          `F[_]` is a type parameter, that must be filled by a type that takes *one* parameter such as `List` or `Option` or `Future` 
          or `Task` etc.
     
2. Rather than simply returning a value e.g. `getUser(id: UserId): User` they return a value wrapped in an effect type 
   e.g.`getUser(id: UserId): F[User]` to indicate that there is some kind of effect required to perform the operation.
   For example to get a user by id, we would generally go to a database or other persistent storage, this fact is captured 
   by the effect.
   
## Exercise

- Implement a fake `BookRepository` that simply saves the book instance in a scala `Map`. 
  In [RepositorySpec.scala](./src/test/scala/RepositorySpec.scala) a fake `UserRepository[Id]` has already been defined.
  Note that, since we are now implementing the interpreter, we must pick a concrete type for `F[_]`, here we choose `Id`
  which means *no effect* and is defined as `type Id[A] = A`
  
- Define a test case that adds a book, then verifies that it is listed
     

