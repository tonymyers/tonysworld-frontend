package controllers


import play.api.mvc.{Action, Controller}
import views.html._

class Home extends Controller {
  def present = Action {
    Ok(home())
  }

}