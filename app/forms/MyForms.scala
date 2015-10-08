package forms

import models.{PersonalDetails, TonyDate}
import org.joda.time.{DateTime, IllegalFieldValueException}
import play.api.data.Form
import play.api.data.Forms._


object MyForms {

  val tonyDate = mapping(
    "year" -> number.verifying("completepersonaldetails.dob.year", year => year >= 1900 && year <= 2200),
    "month" -> number.verifying("completepersonaldetails.dob.month", month => month >= 1 && month <= 12),
    "day" -> number.verifying("completepersonaldetails.dob.day", day => day >= 1 && day <= 31)
  )(TonyDate.apply)(TonyDate.unapply).verifying("completepersonaldetails.dob", theDate => isValidDate(theDate.day, theDate.month, theDate.year))

  def isValidDate(day: Int, month: Int, year: Int): Boolean = {
    try {
      new DateTime().withYear(year).withMonthOfYear(month).withDayOfMonth(day)
      true
    }
    catch {
      case e: IllegalFieldValueException => false
    }
  }

  val personalDetailsForm = Form(
    mapping(
      "firstName" -> text.verifying("completepersonaldetails.firstname", text => (!text.trim.equals("") && text.length > 0 && text.length < 31)),//ToDo think about using minLength / maxLength
      "lastName" -> text.verifying("completepersonaldetails.lastname", text => (!text.trim.equals("") && text.length > 0 && text.length < 51)),
      "DOB" -> tonyDate
    )(PersonalDetails.apply)(PersonalDetails.unapply)
  )


}

