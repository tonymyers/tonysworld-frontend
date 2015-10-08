package models

import play.api.libs.json.Json

case class PersonalDetails(
                            firstName: String,
                            lastName: String,
                            DOB: TonyDate
                            )

object PersonalDetails {
  implicit val formats = Json.format[PersonalDetails]
}