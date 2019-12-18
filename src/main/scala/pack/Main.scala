package pack

import useoop.MLogger


case class MyClassHadFlatMap(value: Int) {
    def map(f: Int => Int): MyClassHadFlatMap = {

        MLogger.generalLogger.debug("old v is {}, new v is {}", value, f(value))

        MyClassHadFlatMap(f(value))
    }

    def flatMap(f: MyClassHadFlatMap => MyClassHadFlatMap): MyClassHadFlatMap = {
        f(this)
    }
}

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


    // thực ra với khai báo function thì def và var không có tác dụng khác biệt lắm, hoặc là mình chưa biết
    def main(args: Array[String]): Unit = {
        /*val reduceFunc: (Int, Int) => Int = (a, b) => {
            a + b
        }*/
        /*val mapFunc: Int => Int = a => 2 * a
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
                case e: Exception => println(e)
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

        def aa(i: Int, b: Int => Int): Int = {
            b(9)
        }

        def bb(a: Int): Int = {
            a + 1
        }


        var rAa = aa(1, bb)*/

        useForComprehension()



        //val u = l1 :+ 3
    }

    def useForComprehension(): Unit = {

        /*def plusOne(i: Int): Int = i + 1

        def doubleInt(v: Int): Int = v * 2

        def plusTwo(int: Int): Int = int + 2

        val myC = MyClassHadFlatMap(0)

        def modifyMyObject(f: Int => Int): MyClassHadFlatMap = {
            //myC.flatMap()
        }

        val u = for {
            a <- modifyMyObject(plusOne)
            b <- modifyMyObject(doubleInt)
            c <- modifyMyObject(plusTwo)
        } yield 1

        u
        MLogger.generalLogger.debug("u is {}", u)*/


    }


}
