package reusable_conponent

trait PayrollSystem {

    type P <: Payroll

    case class Employee(name: String, id: Long)

    trait Payroll {
        def processEmployees(allEmployees: Vector[Employee]): Either[String, Throwable]
    }

    def processPayroll(p: P): Either[String, Throwable]

}
