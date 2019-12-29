package functional.type_class.understand_type_class

import useoop.MLogger

// w đóng vai trò như một wrapper cho các kiểu mà mình sẽ bỏ vào trong
// C sau này sẽ thành formatter, còn A sẽ thành các class mà formatter sẽ xử lý
trait W[Formatter[_]] {
    type A
    val a: A
    val fa: Formatter[A]
}

object W {

    // trong trường hợp này F chính là Formatter
    // A0 chính là kiểu truyền vào cho formatter
    // evn chính là formatter cho tương ứng cho kiểu A0
    def apply[Formatter[_], A0](a0: A0)(implicit evn: Formatter[A0]): W[Formatter] = {
        new W[Formatter] {
            override type A = A0
            override val a: A0 = a0
            override val fa: Formatter[A0] = evn
        }
    }
}

object WAllFormatter {

    // ở đây không biết a kiểu gì, nhưng nó biết cách để format a thông qua hàm fa của w
    implicit val e: MFormatter[W[MFormatter]] = new MFormatter[W[MFormatter]] {
        override def format(w: W[MFormatter]): String = {
            w.fa.format(w.a)
        }
    }

}


object TypeClassWithHigherKindedType {


    def main(args: Array[String]): Unit = {
        import WAllFormatter._

        import AllFormatters._
        val o = W[MFormatter, Int](1)

        val l = List(o, W[MFormatter, Float](1.2F))

        val f = UApi.format(l)
        MLogger.generalLogger.debug("f is {}", f)


    }
}
