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

case class User(
    id: Int,
    login: String,
    avatar_url: String,
    html_url: String,
    name: Option[String] = None,
    email: Option[String] = None,
    company: Option[String] = None,
    blog: Option[String] = None,
    location: Option[String] = None,
    bio: Option[String] = None,
    followers_url: Option[String] = None,
    following_url: Option[String] = None,
    `type`: String = "User", // I think this can be either "User" or "Organization"
    hireable: Option[Boolean] = None,
    public_repos: Option[Int] = None
)
