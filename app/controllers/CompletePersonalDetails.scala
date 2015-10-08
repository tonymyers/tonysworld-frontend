package controllers


import com.google.inject.Inject
import connector.TonyworldConnector
import forms.MyForms.personalDetailsForm
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CompletePersonalDetailsTrait {
  def connector: TonyworldConnector
}

class CompletePersonalDetails @Inject() (ws: WSClient) extends Controller with CompletePersonalDetailsTrait {
  override val connector: TonyworldConnector = new TonyworldConnector(ws)

  def present = Action.async {
    Future.successful(Ok(views.html.inputPersonalDetails(personalDetailsForm)))
  }

  def submitPersonalDetails() = Action.async { implicit request =>
    personalDetailsForm.bindFromRequest.fold(
      personalDetailsFormWithErrors => {
        Future.successful(BadRequest(views.html.inputPersonalDetails(personalDetailsFormWithErrors)))
      },
      successfullyPostedData => {
        connector.sendPersonalDetails(successfullyPostedData).map { wsResponse =>
          wsResponse.status match {
            case 200 => Ok(views.html.personalDetailsSummary(successfullyPostedData))
            case _ => BadRequest(views.html.inputPersonalDetails(personalDetailsForm))
          }
        }.recover {
          case _ => BadRequest(views.html.inputPersonalDetails(personalDetailsForm))///TODO sent to an error page
        }
      }
    )
  }
}