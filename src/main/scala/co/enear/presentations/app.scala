package co.enear.presentations

import java.nio.file.Paths

import com.softwaremill.sttp._

import scala.io.Source

trait BackendModule[F[_], S] {
  implicit val backend: SttpBackend[F, S]
}

trait AppModule[F[_], S] {
  val backendModule: BackendModule[F, S]

  import backendModule.backend
  import GithubDefaultUrls.defaultUrls

  implicit val authorization = AuthorizationType.Basic(
    sys.env("GITHUB_USERNAME"),
    sys.env("GITHUB_TOKEN"))
  implicit val client = HttpClient()
  implicit val gists = GistsAPI()

}

object GistsApp extends App {

  lazy val _backendModule = new BackendModule[Id, Nothing] {
    val backend = HttpURLConnectionBackend()
  }

  val appModule = new AppModule[Id, Nothing] {
    lazy val backendModule = _backendModule
  }

  val gists = appModule.gists

  val description = args(0)
  val files = args.drop(1)

  println("creating gist contents")
  val gistFiles = files.map { path =>
    val contents = Source.fromFile(path).mkString
    val fileName = Paths.get(path).getFileName.toString()
    (fileName, GistFile(contents))
  }.toMap

  val gist = gists.createGist(description, true, gistFiles)
  println(s"Created gist ${gist.id}")

}
