package functional.type_class.solve_expr_problem

import useoop.MLogger


object NewPayrollSystemWithTypeClass {

    import PayrollSystemWithTypeClass._

    case class JapanPayroll[TypePayee](seq: Seq[TypePayee]) {
        def processPay(implicit processor: PayrollProcessor[JapanPayroll, TypePayee]): Either[String, Throwable] = {
            processor.processPayroll(seq)
        }
    }

    // giả sử rằng ở Nhật người ta không trả lương bình thường mà chúng nó trả lương kiểu hợp đồng


    case class ContractorEmpl(id: Int)

}


object NewProcessorPayroll {

    import PayrollSystemWithTypeClass._
    import NewPayrollSystemWithTypeClass._

    implicit object JapanPayrollProcessor extends PayrollProcessor[JapanPayroll, ContractorEmpl] {
        override def processPayroll(payees: Seq[ContractorEmpl]): Either[String, Throwable] = {
            Left[String, Throwable]("Japan Pay ok")
        }

    }

}

object ExtForPayrollSystem {
    def main(args: Array[String]): Unit = {
        import NewPayrollSystemWithTypeClass._
        import NewProcessorPayroll._
        val p = JapanPayroll(Vector(ContractorEmpl(1920)))
        val rPay = p.processPay

        MLogger.generalLogger.info("rPay is {}", rPay)
    }
}
