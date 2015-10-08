import org.specs2.execute.{Success, AsResult, ResultExecution}

package object controllers {
  implicit def unitAsResult: AsResult[Unit] = new AsResult[Unit] {
    def asResult(r: =>Unit) =
      ResultExecution.execute(r)(_ => Success())
  }
}
