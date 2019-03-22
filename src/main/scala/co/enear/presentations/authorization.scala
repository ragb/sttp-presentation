package co.enear.presentations

// could be authentication but since tokens have scopes...
sealed trait AuthorizationType extends Product with Serializable

/**
  * Basic authorization, token can be password or personal access token (recomended)
  *
  */
object AuthorizationType {
  final case class Basic(username: String, token: String)
      extends AuthorizationType

  final case object NoAuthorization extends AuthorizationType
// TODO OAUTH access tokens
}
