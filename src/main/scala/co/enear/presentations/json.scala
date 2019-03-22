package co.enear.presentations

import io.circe.generic.extras.Configuration

trait JsonConfiguration {
  implicit val conf: Configuration =
    Configuration.default.withSnakeCaseMemberNames
}
