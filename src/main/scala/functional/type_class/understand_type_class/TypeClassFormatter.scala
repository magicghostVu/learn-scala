package functional.type_class.understand_type_class

import useoop.MLogger


// type class definition
trait MFormatter[A] {
    def format(a: A): String
}

object AllFormatters {

    implicit object IntFormatter extends MFormatter[Int] {
        override def format(a: Int): String = a.toString
    }

    implicit object FloatFormatter extends MFormatter[Float] {
        override def format(a: Float): String = a.toString
    }


    // hàm này như một cái cầu nối,
    // ở đây, nó chỉ cho compiler biết cách để tạo ra một formatter cho List[A] khi đã biết formatter của A
    implicit def mlist[A](implicit formatter: MFormatter[A]): MFormatter[List[A]] = {
        //println()
        new MFormatter[List[A]] {
            override def format(a: List[A]): String = {
                a.map(formatter.format).mkString("::")
            }
        }
    }

}




object UApi {
    def format[A](a: A)(implicit formatter: MFormatter[A]) = {
        formatter.format(a)
    }
}


object TypeClassFormatter {

    import AllFormatters._
    //import UApi._

    def main(args: Array[String]): Unit = {
        val i = 9
        val formated = UApi.format[Int](i)
        val l = List(1, 2, 5, 7)
        val ii = UApi.format(l)
        MLogger.generalLogger.debug("ii is {}", ii)
    }
}
