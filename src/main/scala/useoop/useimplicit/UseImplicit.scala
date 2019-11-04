package useoop.useimplicit

import useoop.MLogger

class RangeMaker(val left: Int) {
    def -->(right: Int): Range.Inclusive = {
        left to right
    }
}


object UseImplicit {

    //chỉ được khai báo implicit class ở trong 1 object hoặc 1 class
    implicit class MyClass(val u: Int) {
        def k() = {
            MLogger.generalLogger.info("u is {}", u)
        }
    }

    implicit def intToRangeMaker(int: Int): RangeMaker = {
        new RangeMaker(int)
    }

    implicit def dtoInt(double: Double): Int = {
        double.toInt
    }

    def main(args: Array[String]): Unit = {
        val r = 1 --> 8


        val u: Int = 0
        u.k()

        MLogger.generalLogger.info("class r is {}", r.getClass)

    }
}
