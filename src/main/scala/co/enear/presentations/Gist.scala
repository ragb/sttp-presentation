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

case class Gist(
    url: String,
    id: String,
    description: String,
    public: Boolean,
    files: Map[String, GistFile]
)

case class GistFile(
    content: String
)

case class NewGistRequest(
    description: String,
    public: Boolean,
    files: Map[String, GistFile]
)

case class EditGistFile(
    content: String,
    filename: Option[String] = None
)

case class EditGistRequest(
    description: String,
    files: Map[String, Option[EditGistFile]]
)
