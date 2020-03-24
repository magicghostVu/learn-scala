package useoop.useimplicit

import useoop.MLogger

class RangeMaker(val left: Int) {
    def -->(right: Int): Range.Inclusive = {
        left to right
    }
}


object UseImplicit {

    //chỉ được khai báo implicit class ở trong 1 object hoặc 1 class
    implicit class MyClass(val u: Int) extends AnyVal {
        def k() = {
            MLogger.generalLogger.info("u is {}", u)
        }
    }

    /*implicit def intToRangeMaker(int: Int): RangeMaker = {
        new RangeMaker(int)
    }

    implicit def dtoInt(double: Double): Int = {
        double.toInt
    }*/

    implicit val o: Int = 9


    class OO {
        val x: Int = implicitly[Int]
    }


    trait Sumable[T] {
        def sum(t1: T, t2: T): T

    }

    object Sumable {
        implicit val yy: Sumable[Int] = (t1: Int, t2: Int) => t1 + t2

        implicit def aa[A]: Sumable[List[A]] = (t1: List[A], t2: List[A]) => {
            t1.++(t2)
        }
    }

    def useContextBound[T: Sumable](t1: T, t2: T): T = {
        implicitly[Sumable[T]].sum(t1, t2)
    }


    def main(args: Array[String]): Unit = {
        ///val r = 1 --> 8

        val u2 = implicitly[Int]


        val p: OO = new OO()

        val pp = p.x

        MLogger.generalLogger.debug("pp.x is {}", pp)

        useContextBound(2, 5)

        val l1 = List(1, 3, 4)
        val l2 = List(5, 7, 8)

        val p2 = useContextBound(l1, l2)


        MLogger.generalLogger.debug("p2 is {}", p2)
        /* val u: Int = 0
         u.k()

         MLogger.generalLogger.info("class r is {}", u.getClass)*/

    }
}
