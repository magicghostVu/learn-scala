package patternmatching

import useoop.MLogger

import scala.collection.TraversableOnce


object Run {

    def tryMatchingType(o: Any): Unit = {
        o match {
            case s: String => println("o is String")
            case l: List[_] => println("o is list")
            case _ => println("o is " + o.getClass)
        }
    }

    def try2(): Unit = {

        val suffixes = List("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
        val args: List[Int] = List(0, 1, 2, 3, 4)

        println(ordinal(args(0)))

        //chỗ này có sử dụng pattern matching with name
        def ordinal(number: Int) = number match {
            case tenTo20 if 10 to 20 contains tenTo20 => number + "th"
            case rest => rest + suffixes(number % 10)
        }
    }

    def userPatternMatchingWithName(i: Int): Unit = {
        i match {
            case in10 if in10 < 10 => println("nho hon 10")
            case _ => println("xong cmnr")
        }
    }


    def map[A, B](lOrigin: List[A], mapper: A => B): List[B] = {
        lOrigin match {
            case Nil => Nil
            case ::(head, tail) => {
                ::(mapper(head), map(tail, mapper))
            }
        }
    }

    def flatList[A](l: List[List[A]]): List[A] = {
        l match {
            case Nil => Nil
            case ::(head, tail) => {
                head ::: flatList(tail)
            }
        }
    }

    def mapUseFold[A, B](list: List[A], mapper: A => B): List[B] = {
        val startListB = List[B]()
        val foldF: (A, List[B]) => List[B] = (a, b) => {

            MLogger.generalLogger.info("a is {}", a)
            val tmpVal: B = mapper(a)
            tmpVal :: b
        }
        list.foldRight[List[B]](startListB)(foldF)
    }

    def mapUseFoldLeft[A, B](list: List[A], mapper: A => B): List[B] = {
        val startListB = List[B]()
        /*val foldF: (A, List[B]) => List[B] = (a, b) => {
            val tmpVal: B = mapper(a)
            tmpVal :: b
        }*/

        def funFoldLeft(list: List[B], a: A): List[B] = {
            val b: B = mapper(a)

            val arr = Array(list, a, a.hashCode)

            MLogger.generalLogger.info("list is {} , a is {} code is {}", arr.asInstanceOf[Array[AnyRef]]: _*)

            b :: list
        }

        list.foldLeft(startListB)(funFoldLeft)
    }

    def flatListTailRecursive[A](lOrigin: List[List[A]]): List[A] = {
        def privateFlat[B](ll: List[List[B]], acc: List[B]): List[B] = {
            ll match {
                case Nil => acc
                case ::(head, tail) => {

                    val h: List[B] = head

                    val newAcc: List[B] = head ::: acc
                    privateFlat(tail, newAcc)
                }
            }
        }

        privateFlat(lOrigin, List[A]())
        /*val tmp = privateFlat(lOrigin, List[A]())
        tmp.reverse*/
    }


    def main(args: Array[String]): Unit = {
        /*tryMatchingType(List("phuch", "vint"))
        tryMatchingType("phuvh")
        tryMatchingType(new Object)
        tryMatchingType(println(args))*/

        val l: List[String] = List[String]("phuvh", "vint", "quyvv")
        val l2: List[Int] = mapUseFold[String, Int](l, a => a.hashCode)
        /*val l3 = flatListTailRecursive[Char](l2)

        MLogger.generalLogger.info("l3 is {}", l3)


        val fFold: (Char, Int) => Int = (a, b) => {
            if (a > b) a else b
        }

        val p = l3.foldRight(0)(fFold)
        MLogger.generalLogger.info("p is {}", p)*/


        val a: Int => Int = a => a + 1

        val b: String => Int = a => a.hashCode

        val c = a.compose(b)

        val o = c("phuvh")


        MLogger.generalLogger.info("o is {}", o)

    }
}
