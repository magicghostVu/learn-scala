package functional.type_class.understand_type_class

import useoop.MLogger

// w đóng vai trò như một wrapper cho các kiểu mà mình sẽ bỏ vào trong
// C sau này sẽ thành formatter, còn A sẽ thành các class mà formatter sẽ xử lý
trait W[C[_]] {
    type A
    val a: A
    val fa: C[A]
}

object W {

    // trong trường hợp này F chính là Formatter
    // A0 chính là kiểu truyền vào cho formatter
    // evn chính là formatter cho tương ứng cho kiểu a
    def apply[F[_], A0](a0: A0)(implicit evn: F[A0]): W[F] = {
        new W[F] {
            override type A = A0
            override val a: A0 = a0
            override val fa: F[A0] = evn
        }
    }
}

object WAllFormatter {

    implicit object e extends MFormatter[W[MFormatter]] {
        override def format(w: W[MFormatter]): String = {
            w.fa.format(w.a)
        }
    }
}


object TypeClassWithHigherKindedType {

    import WAllFormatter._

    import AllFormatters._

    def main(args: Array[String]): Unit = {
        val k = List(W(1), W(1.3f))
        val h = UApi.format(k)

        MLogger.generalLogger.debug("h is {}", h)
    }
}
