package pack

case class Person(name: String, age: Int)


object Main {


    def mapReduce(r: (Int, Int) => Int,
                  i: Int,
                  m: Int => Int,
                  a: Int, b: Int): Int = {
        /*def iter(a: Int, result: Int): Int = {
            if (a > b) {
                result
            } else {
                iter(a + 1, r(m(a), result))
            }
        }

        iter(a, i)*/

        var rr: Int = i
        if (a > b) return rr

        for (ii <- a to b) {
            //thực hiện map
            val am = m(ii)
            //println("am is ", am)

            //thực hiện reduce
            rr = r(rr, am)
        }
        rr

    }

    //sử dụng cách khai báo hàm với nhiều nhóm tham số
    def loopTill(cond: => Boolean)(body: => Unit): Unit = {
        if (cond) {
            body
            loopTill(cond)(body)
        }
    }

    def main(args: Array[String]): Unit = {
        val reduceFunc: (Int, Int) => Int = (a, b) => {
            a + b
        }
        val mapFunc: Int => Int = a => 2 * a
        val originVal = 0
        val r = mapReduce(reduceFunc, originVal, mapFunc, 1, 5)
        println(r)


        var t = 0

        loopTill(t < 10) {
            t += 1
            println("i is ", t)
        }

        val g = loopTill(t < 9) _

        g {
            t += 1
        }

        println("done g")

        //println(t.getClass)
    }

}
