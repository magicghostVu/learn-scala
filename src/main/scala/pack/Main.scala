package pack

import java.util

case class Person(name: String, age: Int)


object Main {


    //r là hàm reduce, chưa logic về việc 2 tham số sẽ reduce với nhau thế nào
    // m là hàm map, nó chứa logic về việc số đầu vào sẽ biến thành gì sau hàm này
    def mapReduce(r: (Int, Int) => Int,
                  i: Int,
                  m: Int => Int,
                  a: Int, b: Int): Int = {
        // cách cài đặt sử dụng đệ quy
        /*def iter(a: Int, result: Int): Int = {
            if (a > b) {
                result
            } else {
                iter(a + 1, r(m(a), result))
            }
        }
        iter(a, i)*/

        // cách cài đặt sử không sử dụng đệ quy
        var rr: Int = i
        if (a > b) return rr
        for (ii <- a to b) {
            //thực hiện map
            val am = m(ii)
            //thực hiện reduce
            rr = r(rr, am)
        }
        rr
    }

    //sử dụng cách khai báo hàm với nhiều nhóm tham số để có thể sử dụng curry function
    def loopTill(cond: => Boolean)(body: => Unit): Unit = {
        if (cond) {
            body
            loopTill(cond)(body)
        }
    }


    // thử với type parameter
    def useTypeParam[A](a: A): List[A] = {
        List[A](a)
    }

    def main(args: Array[String]): Unit = {
        val reduceFunc: (Int, Int) => Int = (a, b) => {
            a + b
        }
        val mapFunc: Int => Int = a => 2 * a
        val originVal = 0

        def reduceFunDef(a: Int, b: Int): Int = {
            a + b
        }

        val r = mapReduce(reduceFunDef, originVal, mapFunc, 1, 5)
        println(r)

        var t = 0
        // sử dụng curry function
        val g = loopTill(t < 9) _
        g {
            t += 1
        }
        println("done g")

        val ll: List[Double] = useTypeParam(6)

        val list: List[Double] = List[Double](1.0, 2, 3, 54, 6, 7)

        val sumDouble: (Double, Double) => Double = (a, b) => {
            a + b
        }

        val myName: String = "Vu Hong Phu"

        val containUpper: Boolean = myName.exists(_.isUpper)

        val sum = list.foldLeft[Double](0.0)(_ + _)

        def breakableFunc(op: => Unit): Unit = {
            try {
                op
            } catch {
                case _ => println
            }
        }

        val ex: Exception = new RuntimeException("err break")

        //val jj = ::

        def break: Unit = {
            throw ex
        }

        var ii = 0

        def myFunc = {
            for (i <- 1 to 10) {
                ii += 1
                if (i > 5) break
            }
        }

        breakableFunc {
            myFunc
            println("done")
        }

        def aa(i: Int, b: => Int): Int = {
            b
        }

        /*val k: Int = aa(1, {
            1
        })*/


        val l1: List[Int] = List[Int](1, 3, 4, 5)

        val l2: List[Int] = List[Int](6, 7, 8, 9)

        val l3: List[Int] = for (a <- l1; b <- l2; if a + b > 10) yield a + b



        //println("k is ", k)
    }

}
