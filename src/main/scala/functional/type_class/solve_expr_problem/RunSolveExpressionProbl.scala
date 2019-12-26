package functional.type_class.solve_expr_problem


case class Employee(id: Int, name: String)

case class ContractEmployee(contractId: Int, name: String)


// A ở đây thể hiên loại nhân viên gì, có thể là nhân viên hợp đồng hoặc nhân viên chính thức
case class USPayroll[TypePayee](seq: Seq[TypePayee]) {
    def processPay(implicit processor: PayrollProcessor[USPayroll, TypePayee]): Either[String, Throwable] = {
        processor.processPayroll(seq)
    }
}

case class CanadaPayroll[TypePayee](seq: Seq[TypePayee]) {
    def processPay(implicit processor: PayrollProcessor[CanadaPayroll, TypePayee]): Either[String, Throwable] = {
        processor.processPayroll(seq)
    }
}


// đây là một type class
trait PayrollProcessor[Payroll[_], TypePayee] {
    def processPayroll(payees: Seq[TypePayee]): Either[String, Throwable]
}


//tất cả các kiểu pay được khai báo implicit ở một nơi và sẽ được import theo ngữ cảnh sau này để trình biên dịch tự
// match với các đối tượng tương ứng
// object này gióng như một ma trận mà ở đó ta có thể mở rộng cả về 2 phía
// ta có thể thêm processor mới bất cứ khi nào với Payroll mới và/hoặc là kiểu payee mới đều không có vấn đề
object AllPayrollProcessor {

    implicit object USPayrollProcessor extends PayrollProcessor[USPayroll, Employee] {

        // todo: impl logic trả lương cho từng case bên trong này
        // logic là hoàn toàn xác định vì quốc gia đã biết và kiểu payee đã biết
        override def processPayroll(payees: Seq[Employee]): Either[String, Throwable] = {
            Left("US pay for all")
        }
    }

}


object RunSolveExpressionProbl {



    def main(args: Array[String]): Unit = {

        /*kịch bản trong trường hợp này đó là ta cần cài đặt một hệ thống chi trả lương cho nhân
          viên chính thức ở các quốc gia khác nhau,
          tuy nhiên sau này khi được mở rộng thì hệ thống còn phải chi trả cho các nhân viên không chính thức (tạm gọi
          là nhân viên hợp đồng)
          yêu cầu đưa ra là không được sửa lại tất cả các base class mà vẫn giữ được type-safety trong run-time (tất
          nhiên không có ép kiểu lúc runtime vì làm như thế sẽ không đảm bảo type-safety nữa)
        */

        /* việc trả lương hiện tại phụ thuộc vào 2 yếu tố đó là quốc gia và loại nhân viên
        */
    }
}
