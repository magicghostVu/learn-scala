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

    /*implicit def formatList[A](la: List[A])(implicit formatter: MFormatter[A]) = {
        la.map(formatter.format).mkString("::")
    }*/

    //implicit def list[A](implicit ev: MFormatter[A]): MFormatter[List[A]] = (a: List[A]) => a.map(e => ev.format(e)).mkString(" :: ")

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
