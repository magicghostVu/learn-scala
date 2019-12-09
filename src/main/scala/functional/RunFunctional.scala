package functional

import useoop.MLogger

trait TaxStrategy {
    def taxIt(product: String): Double
}

class ATaxStrategy extends TaxStrategy {
    def taxIt(product: String): Double = {
        1.0
    }
}

class BTaxStrategy extends TaxStrategy {
    def taxIt(product: String): Double = {
        2.0
    }
}

object OO {

    /*implicit def kestrel[A](a: A) = new {
        def tap(sideEffect: A => Unit): A = {
            sideEffect(a)
            a
        }
    }*/

    implicit class MyImpli[A](a: A) {
        def tap(function1: A => Unit): A = {
            function1(a)
            a
        }
    }

}


class PP(var name: String)

object RunFunctional {

    def myTaxIt(taxStrategy: TaxStrategy): String => Double = {
        taxStrategy.taxIt _
    }

    def taxIt: TaxStrategy => String => Double = s => {
        val t: String => Double = p => s.taxIt(p)
        t
    }

    import OO._

    def main(args: Array[String]): Unit = {

        val pf: PartialFunction[String, Int] = {
            case str if str.length == 9 => 9
        }

        val orelse: PartialFunction[String, Int] = {
            case str: String => -1
        }

        val p = pf.orElse(orelse)("oiuo")


        MLogger.generalLogger.debug("p is {}", p)


        val pp = new PP("phuvh").tap(p2 => {
            val name = p2.name
            MLogger.generalLogger.debug(s"name is $name")
        })


        val ff: String => Int => String = str => {
            val s: Int => String = i => {
                "phuvh"
            }
            s
        }

        val f2: (String, String) => Int = (str1, str2) => {
            str1.length + str2.length
        }


        val f3 = f2.curried("phuvh")

        val res: Int = f3("vint")

        MLogger.generalLogger.debug("res is {}", res)

    }

    def sum(xs: List[Int]): Int = {
        def sumTailRecursive(list: List[Int], acc: Int): Int = {
            list match {
                case Nil => acc
                case head :: tail => sumTailRecursive(tail, acc + head)
            }
        }
        sumTailRecursive(xs, 0)
    }
}
