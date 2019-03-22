/*
 * Copyright 2016-2019 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.enear.presentations

import cats.data.NonEmptyList
import cats.instances.either._

import cats.syntax.list._

import io.circe.Decoder.Result
import io.circe._

import io.circe.generic.extras.semiauto.deriveDecoder

/** Implicit circe decoders of domains objects */
trait Decoders extends JsonConfiguration {

  implicit val decodeGistFile: Decoder[GistFile] = Decoder.instance { c ⇒
    for {
      content ← c.downField("content").as[String]
    } yield
      GistFile(
        content = content
      )
  }

  implicit val decodeGist: Decoder[Gist] = Decoder.instance { c ⇒
    for {
      url ← c.downField("url").as[String]
      id ← c.downField("id").as[String]
      description ← c.downField("description").as[String]
      public ← c.downField("public").as[Boolean]
      files ← c.downField("files").as[Map[String, GistFile]]
    } yield
      Gist(
        url = url,
        id = id,
        description = description,
        public = public,
        files = files
      )
  }

  implicit val decodeUser: Decoder[User] = deriveDecoder

  implicit def decodeNonEmptyList[T](
      implicit D: Decoder[T]): Decoder[NonEmptyList[T]] = {

    def decodeCursors(cursors: List[HCursor]): Result[NonEmptyList[T]] =
      cursors.toNel
        .toRight(DecodingFailure("Empty Response", Nil))
        .flatMap(nelCursors => nelCursors.traverse(_.as[T]))

    Decoder.instance { c ⇒
      c.as[T] match {
        case Right(r) => Right(NonEmptyList(r, Nil))
        case Left(_) => c.as[List[HCursor]] flatMap decodeCursors
      }
    }
  }

  implicit val decodeErrorDescription: Decoder[ErrorDescription] =
    deriveDecoder
  implicit val decodeClientError: Decoder[ClientError] = deriveDecoder
}

object Decoders extends Decoders
