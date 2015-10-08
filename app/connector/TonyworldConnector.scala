package connector

import com.google.inject.Inject
import models.PersonalDetails
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future

class TonyworldConnector @Inject() (ws: WSClient) {

  def data(personalDetails: PersonalDetails) = Json.obj(
    "firstName" -> personalDetails.firstName,
    "lastName" -> personalDetails.lastName,
    "DOB" -> personalDetails.DOB  // TODO: Removed because we know we'll need an implicit converter from TonyDate to String
  )

  def serviceUrl: String = "http://localhost:9001/change-personal-details"

  def sendPersonalDetails(personalDetails: PersonalDetails): Future[WSResponse] = {
    ws.url(serviceUrl).post(data(personalDetails))
  }

}

//object TonyworldConnector extends TonyworldConnector {
//
//}