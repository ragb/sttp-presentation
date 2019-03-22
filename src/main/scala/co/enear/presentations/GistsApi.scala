package co.enear.presentations

class GistsAPI[F[_]](implicit apiUrls: GithubApiUrls,
                     httpClient: HttpClient[F])
    extends Encoders
    with Decoders {
  val gistsUri = apiUrls.baseUrl.path("gists")

  def createGist(description: String,
                 public: Boolean,
                 files: Map[String, GistFile]): F[Gist] =
    httpClient.post(gistsUri, NewGistRequest(description, public, files))

  def getGist(gistId: String): F[Gist] =
    httpClient.get(gistsUri.path("gists", gistId))
}

object GistsAPI {
  def apply[F[_]]()(implicit urls: GithubApiUrls, client: HttpClient[F]) =
    new GistsAPI
}
