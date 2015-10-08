package models

import play.api.libs.json.Json

case class TonyDate(
                     year: Int,
                     month: Int,
                     day: Int
                     )

object TonyDate {
  implicit val formats = Json.format[TonyDate]
}
