package functional.type_class.understand_type_class


// Type Class definition
trait Num[A] {
    def +(l: A, r: A): A

    def *(l: A, r: A): A
}

object instances {
    // instance for Int
    implicit val intNum: Num[Int] = new Num[Int] {
        override def +(l: Int, r: Int): Int = l + r

        override def *(l: Int, r: Int): Int = l * r
    }
    // instance for Float
    implicit val floatNum: Num[Float] = new Num[Float] {
        override def +(l: Float, r: Float): Float = l + r

        override def *(l: Float, r: Float): Float = l * r
    }
}

object Num {
    // polymorphic functions to be used by end user
    def +[A: Num](l: A, r: A): A = implicitly[Num[A]].+(l, r)

    def *[A: Num](l: A, r: A): A = implicitly[Num[A]].*(l, r)

    def mPlus[A](l: A, r: A)(implicit na: Num[A]): A = {
        na.+(l, r)
    }
}

object RunUnderStand {

    import instances._

    def main(args: Array[String]): Unit = {
        Num.*(1, 6)
        //Num.+(1.4, 5)

        Num.mPlus(2, 3)
    }
}
