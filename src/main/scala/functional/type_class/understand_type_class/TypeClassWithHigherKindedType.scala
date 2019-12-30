package functional.type_class.understand_type_class

import useoop.MLogger

// w đóng vai trò như một wrapper cho các kiểu mà mình sẽ bỏ vào trong
// C sau này sẽ thành formatter, còn A sẽ thành các class mà formatter sẽ xử lý
trait Wrapper[Formatter[_]] {
    type A
    val a: A
    val fa: Formatter[A]
}


// nó sẽ đóng gói lại một thằng và cái formatter(cái mà sẽ xử lý logic sau này) cho thằng đó,
// cái xử lý logic nên được truyền implicit
object Wrapper {

    // trong trường hợp này F chính là Formatter
    // A0 chính là kiểu truyền vào cho formatter
    // evn chính là formatter cho tương ứng cho kiểu A0
    // sử dụng pattern này mình có thể sử dụng được ad-hoc polymorphism
    def apply[Formatter[_], A0](a0: A0)(implicit evn: Formatter[A0]): Wrapper[Formatter] = {
        new Wrapper[Formatter] {
            override type A = A0
            override val a: A0 = a0
            override val fa: Formatter[A0] = evn
        }
    }
}

object WAllFormatter {

    // ở đây không biết a kiểu gì, nhưng nó biết cách để format a thông qua hàm fa của w
    implicit val evn: MFormatter[Wrapper[MFormatter]] = new MFormatter[Wrapper[MFormatter]] {
        override def format(w: Wrapper[MFormatter]): String = {
            w.fa.format(w.a)
        }
    }

}


object TypeClassWithHigherKindedType {

    import WAllFormatter._

    import AllFormatters._

    def main(args: Array[String]): Unit = {

        val o = Wrapper(1)

        val l = List(o, Wrapper(1.2F))

        val f = UApi.format(l)
        MLogger.generalLogger.debug("f is {}", f)


    }
}
