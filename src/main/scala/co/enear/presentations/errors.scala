package co.enear.presentations

final case class ClientError(
    message: String,
    errors: List[ErrorDescription] = List.empty
) extends Exception(message)

final case class ErrorDescription(
    resource: String,
    field: String,
    message: Option[String],
    errorCode: Option[String],
    custom: Option[String]
)
