package controllers

import models.TonyDate
import org.joda.time.LocalDate
import org.scalatest.Matchers._
import play.api.i18n.Messages
import play.api.test.PlaySpecification
import forms.MyForms

class PersonalDetailsFormSpec extends PlaySpecification {

  val personalDetails = Map("firstName" -> "Tony", "lastName" -> "Smith", "DOB.year" -> "1972", "DOB.month" -> "07", "DOB.day" -> "01")
  val personalDetailsLeapyear = Map("firstName" -> "Tony", "lastName" -> "Smith", "DOB.year" -> "2012", "DOB.month" -> "02", "DOB.day" -> "30")
  val maxlengthFirstName = (for (c <- 1 to 30 + 1) yield { "a" }).mkString
  val maxlengthLastName = (for (c <- 1 to 50 + 1) yield { "a" }).mkString
  val invalidDatePersonalDetails = Map("firstName" -> "Tony", "lastName" -> "Smith", "DOB.year" -> "2012", "DOB.month" -> "02", "DOB.day" -> "30")

  "PersonalDetails Form" should {

    "not error when valid data is provided" in {
      MyForms.personalDetailsForm.bind(personalDetails).fold(
        formWithErrors => fail("The PersonalDetails form has failed to submit " + personalDetails),
        success => {
          success.firstName shouldBe "Tony"
          success.lastName shouldBe "Smith"
          success.DOB shouldBe TonyDate(1972,7,1)
        }
      )
    }

    "error when firstName is not provided" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("firstName" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message shouldBe ("completepersonaldetails.firstname")
          formWithErrors.errors.length shouldBe (1)
        },
        success => fail("The firstName was not blank")
      )
    }

    "error when lastName is not provided" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("lastName" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message shouldBe ("completepersonaldetails.lastname")
          formWithErrors.errors.length shouldBe (1)
        },
        success => fail("The lastName was not blank")
      )
    }

    "error when more then 30 caricatures in entered into the first name field" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("firstName" -> maxlengthFirstName)).fold(
        formWithErrors => {
          formWithErrors.errors.head.message shouldBe ("completepersonaldetails.firstname")
          formWithErrors.errors.length shouldBe (1)
        },
        f => {
          fail("The first name field validation has failed and let too caricatures pass in the form")
        }
      )
    }

    "error when more then 50 caricatures in entered into the last name field" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("lastName" -> maxlengthLastName)).fold(
        formWithErrors => {
          formWithErrors.errors.head.message shouldBe ("completepersonaldetails.lastname")
          formWithErrors.errors.length shouldBe (1)
        },
        f => {
          fail("The first name field validation has failed and let too caricatures pass in the form")
        }
      )
    }

    "error when no Date od Birth day fiels not is provided" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("DOB.day" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.head.key shouldBe ("DOB.day")//TODO need to check against error message
          formWithErrors.errors.length shouldBe (1)
        },
        success => fail("The DOB day was not blank")
      )
    }

    "error when no Date od Birth month fiels not is provided" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("DOB.month" -> "")).fold(
        formWithErrors => {
          //println(s"\n\n\n personal details = ${personalDetails}\n\n\n\n error = ${formWithErrors.errors.head.message}\n\n\n")
          formWithErrors.errors.head.key shouldBe ("DOB.month")//TODO need to check against error message
          formWithErrors.errors.length shouldBe (1)
        },
        success => fail("The DOB month was not blank")
      )
    }

    "error when no Date od Birth year fiels not is provided" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("DOB.year" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.head.key shouldBe("DOB.year")
          formWithErrors.errors.length shouldBe (1)
        },
        success => fail("The DOB year was not blank")
      )
    }

    "error when an invalid day of 32 is entered into the day field for DOB" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("DOB.day" -> "32")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message shouldBe("completepersonaldetails.dob.day")
          formWithErrors.errors.length shouldBe (1)
        },
        success => fail("Error, 32 has successfully been passed into the DOB day field")
      )
    }

    "error when an invalid month of 13 is entered into the month field for DOB" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("DOB.month" -> "13")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message shouldBe ("completepersonaldetails.dob.month")
          formWithErrors.errors.length shouldBe (1)
        },
        success => fail("Error, 13 has successfully been passed into the DOB month field")
      )
    }

    "error when an invalid year of leap year is entered into the DOB" in {
      MyForms.personalDetailsForm.bind(personalDetailsLeapyear).fold(
        formWithErrors => formWithErrors.errors.head.message shouldBe ("completepersonaldetails.dob"),
        success => {
          fail("Error, a leap year date has been allowed to be passed into the form")
        }
      )
    }

    "error when an invalid year of leap year is entered into the DOB" in {
      MyForms.personalDetailsForm.bind(invalidDatePersonalDetails).fold(
        formWithErrors => {
          formWithErrors.errors.head.message shouldBe ("completepersonaldetails.dob")
        },
        success => {
          fail("Error, a leap year date has been allowed to be passed into the form")
        }
      )
    }

    "error when an invalid year of 1899 is entered into the DOB" in {
      MyForms.personalDetailsForm.bind(personalDetails + ("DOB.year" -> "1899")).fold(
        formWithErrors => {
          formWithErrors.errors.head.key shouldBe("DOB.year")
          formWithErrors.errors.length shouldBe (1)
        },
        success => fail("The DOB year was not blank")
      )
    }

    val listOfValidDates = List (
      MyForms.isValidDate(28, 2, 2011),
      MyForms.isValidDate(28, 2, 2011)
    )

    def listOfInvalidDates = List(
      MyForms.isValidDate(32, 2, 2012),
      MyForms.isValidDate(29, 2, 2011),
      MyForms.isValidDate(32, 12, 2013),
      MyForms.isValidDate(35, 5, 2015)
    )

    "isValidDate should return true for a valid date" in {
      listOfValidDates.foreach ( isValidDate =>
        isValidDate shouldBe(true)
      )
    }

    "isValidDate should return false for a invalid date" in {
      listOfInvalidDates.foreach ( isValidDate =>
        isValidDate shouldBe(false)
      )
    }


    //println(s"\n\n\n personal details = ${invalidDatePersonalDetails}\n\n\n\n isValidDate = ${MyForms.isValidDate(28,2,2011)}\n\n\n")


    //ToDo test ther following dates
    //    28/02/2011 should generate theDate as 28/02/2011
    //    29/02/2011 should generate theDate as 01/03/2011 as this year is NOT a leap year and Java/Scala is being kind to you by not throwing an exception
    //    29/02/2012 should generate theDate as 29/02/2011 as 2012 is a leap year
    //    32/12/2012 should generate theDate as 01/01/2013 as there are never 32 days in December and Java/Scala just roll on the days into the next month
    //    35/05/2012 should generate theDate as 04/06/2012 (ish) as there are only 31 days in May and Java/Scala etc etc
  }
}
