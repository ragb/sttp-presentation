%Flexible HTTP clients with sttp in Scala
%Rui Batista
%2019-03-22





# Introduction #


## About Me ##


- Rui Batista, 31, Scala Developer at e.Near
- @ragb on Github and Twitter 
- Typeful pure functional programer when life permits
- Accessibility advocate by design.

## The Plan (subject to change) ##

- sttp (http client library / Scala showcase)
- Requests, imuttability, implicits as domain invariants
- Implicits as dependencies (configuration, Effects)
- Typeclasses: sequencing, monads and errors
- Creating Gists (example)
- Other more well known patterns: Decorators, logging, metrics
- Testing
- Scala.js? Scala Native?





# sttp #

## What's that? ##


> sttp: the Scala HTTP client you always wanted!

...

- https://github.com/softwaremill/sttp
- https://sttp.readthedocs.io/en/latest/
- Author: SoftwareMill (company)

## Now, really... ##

- Simple interface to create http requests and read responses
- Abstracts over many HTTP backends
- Async, streaming,...
- Facilities for testing 


## How to get it? ##

`build.sbt`

```scala
val sttpVersion = "1.5.11" // or latest
libraryDependencies += "com.softwaremill.sttp" %% "core" % sttpVersion

// Circe json support (others available)
libraryDependencies = "com.softwaremill.sttp" %% "circe" % sttpVersion


// Also include backends etc in an application...

```



## How to use it? ##

// Usual imports


```tut:book

import com.softwaremill.sttp._
import quick._ // explain!

```


## Query ghithub user ##


```tut:book
def userUri(username: String) = 
    uri"https://api.github.com".path("users", username)

sttp.get(
    userUri("ragb")
    ).send().unsafeBody
```

## Query and Decode github user ###

```tut:invisible
import co.enear.presentations._
```

```tut:book
import com.softwaremill.sttp.circe._
import Decoders._ // our own

sttp.get(
    userUri("ragb")
    ).response(
        asJson[User]
    ).send().unsafeBody.right.get
```

## Very nice, but..? ##



```scala
case class RequestT[U[_], T, +S](
    method: U[Method],
    uri: U[Uri],
    body: RequestBody[S],
    headers: Seq[(String, String)],
    response: ResponseAs[T, S],
    options: RequestOptions,
    tags: Map[String, Any]
)
```

## Now gets crazier ##



```scala

// get
def get(uri: Uri): Request[T, S] =
    this.copy[Id, T, S](uri = uri, method = Method.GET)
  def head(uri: Uri): Request[T, S] =
    this.copy[Id, T, S](uri = uri, method = Method.HEAD)
    
// send method
def send[R[_]]()(implicit backend: SttpBackend[R, S], isIdInRequest: IsIdInRequest[U]): R[Response[T]]



```

- Implicits as domain invariants
- Implicits influence return type?!

## Let's go back to that Id thing ##


```scala
type Id[A] = A
```




- Fills in that `U[_]` type.
- Actualy in the `R[_]` too.


## Backends ##

- HttpURLConnectionBackend (the one we've been using)
- TryHttpURLConnectionBackend (wraps in try)
- Akka-http
- async-http-client
- LibCurl (for scala native)
- Fetch 
- play-ws (which I'm working one)


## Using a different backend ##

```tut:book
implicit val backend = TryHttpURLConnectionBackend()

val response = sttp.get(
    userUri("ragb")
).send()

```

## On unsafe body ##

> Running programs is unsafe

...


```tut:book
response.map(_.body)

response.map(_.unsafeBody)

```


- `map`?!

# Scala patterns #


## Higher kinds ##

> We need fancy names for simple things, probably they will pay us better.

...


- `F[_]` abstracts over `F[String]`, `F[Option[Int]]`,...
- `F` can be `Id`, `Future`, `IO`, `Future[Either[Error, ?]]`


## Type classes ##

```tut:book
traiT Show[-S] {
    def show(s: S): String
}

object Show {
implicit val intShow: Show[Int] = new Show[Int] {
def show(value: Int) = value.toString()
    }
}


```


## More interesting type classes #


Directly from sttp:

```scala
trait MonadError[R[_]] {
  def unit[T](t: T): R[T]
  def map[T, T2](fa: R[T])(f: T => T2): R[T2]
  def flatMap[T, T2](fa: R[T])(f: T => R[T2]): R[T2]

  def error[T](t: Throwable): R[T]

  def flatten[T](ffa: R[R[T]]): R[T] = flatMap[R[T], T](ffa)(identity)


// ...

}

```

# Example: Gists #

## Github API ##


- Restful
- Json
- Authentication / authorization


## Gists ##

- Create
- Get
- Edit 

## Code ##

- Http client
- Gists API
- Small tool to create gists from command line



# Other Conserns #


## Logging ##

```scala

class LoggingSttpBackend[R[_], S](delegate: SttpBackend[R, S]) extends SttpBackend[R, S]
  with StrictLogging {

  override def send[T](request: Request[T, S]): R[Response[T]] = {
    responseMonad.map(responseMonad.handleError(delegate.send(request)) {
      case e: Exception =>
        logger.error(s"Exception when sending request: $request", e)
        responseMonad.error(e)
    }) { response =>
      if (response.isSuccess) {
        logger.debug(s"For request: $request got response: $response")
      } else {
        logger.warn(s"For request: $request got response: $response")
      }
      response
    }
  }
  override def close(): Unit = delegate.close()
  override def responseMonad: MonadError[R] = delegate.responseMonad
}
```

## Metrics ##

**Ideas**?

- Request has tags support

...

- Existing backends for Brave, Prometheus.

## Pattern? ##

- Decorator,
- Delegate
- Whatever!


## Other examples ##

- Handling redirects
- Retries
- ...


## Tests ##

Backends, just that.

```scala
implicit val testingBackend = SttpBackendStub.synchronous
  .whenRequestMatches(_.uri.path.startsWith(List("a", "b")))
  .thenRespond("Hello there!")
  .whenRequestMatches(_.method == Method.POST)
  .thenRespondServerError()

```


# Conclusion #

## Introduction in the past ##


## Credits ##

- sttp library authors
- Github4s Scala library
- pandoc
- reveal.js


an

...

- e.Near (we're hiring!)

## questions? ##

- Twitter: @ragb
