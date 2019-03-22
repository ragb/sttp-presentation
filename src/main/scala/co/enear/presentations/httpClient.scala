package co.enear.presentations

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._

import cats.syntax.either._
import io.circe.parser.{decode => decodeJson}
import io.circe.{Decoder, Encoder, Error => CirceError}

class HttpClient[F[_]](implicit authorization: AuthorizationType,
                       backend: SttpBackend[F, Nothing]) {

  import Decoders._

  implicit private val M = backend.responseMonad

  implicit private val baseRequest = addAuthorization(sttp)
  import monadSyntax._

  def get[T: Decoder](uri: Uri): F[T] = {
    baseRequest
      .response(asJson[T])
      .get(uri)
      .send()
      .flatMap(decodeResponse[T] _)
  }

  def post[B: Encoder, T: Decoder](uri: Uri, body: B): F[T] = {
    baseRequest
      .response(asJson[T])
      .body(body)
      .post(uri)
      .send()
      .flatMap(decodeResponse _)
  }

  private def decodeResponse[T](
      response: Response[Either[DeserializationError[CirceError], T]])
    : F[T] = {

    val errorOrResult = response.body
      .leftMap { errorBody =>
        // try to parse error body
        decodeJson[ClientError](errorBody)
          .leftMap(_ => new UnparsabbleErrorException(errorBody))
          .merge
      }
      .map(_.leftMap(_.error))
      .joinRight

    errorOrResult.fold(M.error _, M.unit[T] _)

  }

  private def addAuthorization[U[_], T, S2](request: RequestT[U, T, S2]) =
    authorization match {
      case AuthorizationType.Basic(user, token) =>
        request.auth.basic(user, token)
      case AuthorizationType.NoAuthorization => request
    }
}

object HttpClient {
  def apply[F[_]]()(implicit authorization: AuthorizationType,
                    backend: SttpBackend[F, Nothing]) = new HttpClient
}

class UnparsabbleErrorException(body: String) extends Exception(body)
