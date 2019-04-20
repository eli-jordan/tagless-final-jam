# Putting It All Together

So far we have seen how several of the major elements required to build an application can be expressed in a purely
functional manner using the tagless-final style.

- In section 1, we covered the basics of the tagless-final style.
- In section 2, we saw how to model our application domain.
- In section 3, we saw how application sub-systems can be integrated by composing tagless-final algebras.
- In section 4, we covered raising and handing errors.
- In section 5, we covered parallelism.

These elements, along with IO and concurrency, are the core building blocks of any real world application.
At this point in my personal studies of the topic, I had an ah-ha moment, and I could see how a real world
application can be built using these techniques.

In this section we use all the techniques we have learned in the previous sections, and demonstrate the use of the 
to create a HTTP API for our reading list application using [http4s](https://http4s.org/) and [circe](https://circe.github.io/circe/).

We will use the [cats-effect](https://typelevel.org/cats-effect/datatypes/io.html) IO datatype to represent an IO actions,
and [http4s](https://http4s.org/) will handle concurrency for us, by exposing a HTTP server that can handle many concurrent requests.
Note: `cats.effect.IO` also has support for explicitly working with concurrency, but we won't be covering it here.

## Prerequisites

- The previous exercises
- Familiarity with `http4s` and `circe` is beneficial

## Read The Code

This step is mainly aimed at demonstrating how to integrate the techniques we have learned so far into a real application.
This is not workshop on `http4s` or `circe`, so the code that integrates with those libraries has been provided for you.

Two new files have been added.

### [`fs.scala`](../reading-list/src/main/scala/reading/fs.scala)

In this file we define a real-ish implementations of the `BookRepository` and `UserRepository` that
are backed by simple files on the filesystem. This gives us actual persistence, and also means we are
dealing with real effects (i.e. writing to the file system).

 > Note: In a real application you would want to use some kind of database to store the user and book records,
         not flat files on the filesystem.

- Since both `BookRepository` and `UserRepository` need to interact with the file system, an intermediate abstraction, `FileService`,
  has been extracted.

- The `FileService` trait has been implemented in terms of the JVMs file IO operations, and [`cats.effect.IO`](https://typelevel.org/cats-effect/datatypes/io.html).
  `cats.effect.IO` allows us to lift impure, side-effecting code such as the java file IO APIs, into a pure value (i.e. an instance
  of the `IO` data type). This is achieved using the `IO.delay` function. You can find lots of information about the `IO` datatype, and
  why its an important construct in [this](https://typelevel.org/blog/2017/05/02/io-monad-for-cats.html) blog post and on the [cats-effect](https://typelevel.org/cats-effect/datatypes/io.html)
  website.

### [`http.scala`](../reading-list/src/main/scala/reading/http.scala)

In this file we, define the HTTP endpoints in the reading list applications API, wire together the components of the application
and start the HTTP server in our `main` entry point.

- The HTTP endpoints are defined in the `ReadingListHttpService` class using the `http4s` pattern matching DSL.
  The API endpoint map directly to functions on the abstractions we have defined in previous sections.
  - `GET  /book` -> `BookRepository#listBooks`
  - `POST /book` -> `BookRepository#addBook`
  - `POST /user` -> `UserRepository#addUser`
  - `GET  /reading-list/${user-id}` -> `ReadingListServiceCompiler#getReadingList`

- The components of the application are wired together in the `Module` trait.
  
  If you have seen other examples of using tagless-final, you may have seen the service implementations
  passed as implicit parameters, rather than as explicit parameters. I prefer explicitly passing the service
  instances for the following reasons;
     1. Its easier to follow where a given instance is used when reading the code
     2. The instances are almost always not coherent (i.e. there are multiple implementations of the trait for a given F[_])
     3. It uses first class constructs of the language, that can be composed in more flexible ways than implicits.

  Having said that, both approaches work so use what you personally prefer.

## Exercise

Start the HTTP server, and try the APIs.
  - Start the HTTP server by running `sbt 'reading-list/runMain reading.Server'`
  - Run `curl -X GET 'http://localhost:8080/book' | jq` or open `http://localhost:8080/book` in your browser
    to see the currently available books.
  - Run `curl -X GET 'http://localhost:8080/reading-list/1' | jq` or open `http://localhost:8080/reading-list/1` in
    your browser to see the reading list for the user with id `1`

> Note: The pre-canned books / users you see in the above examples are just files in this git repository. You can find them
>       in the [`resources/data`](../reading-list/src/main/resources/data) directory.
    
At this point there are no prescribed exercises, but I encourage you to look at some improvements to the reading list application.

Some options are:

- Complete the HTTP API, so that operations such as listing all the users or updating books are exposed.
- Allow looking up the reading list for a user based on their name, rather than by their user id.
- Use an effect type other than `cats.effect.IO` such as the [monix](https://monix.io/) `Task` data type.
- Use an actual database to store the user and book records. If we use a relational database, can `ReadingListServiceCompiler#getReadingList` 
  be implemented differently?