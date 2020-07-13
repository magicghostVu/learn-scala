package uselazy

object MainLazy {

    def useLazy(a: => Int): Unit = {
        val i = a
        println("2a is " + (i + i))
    }

    def main(args: Array[String]): Unit = {


        val mList = List(3, 6, 7, 89, 9, 4, 3)

        val ii = MStream(2, 5, 6, 7, 9)

        /*
        val mStream = MStream(1, 3, 4, 56, 68, 78)

        mStream.forEach(i => {
            println(s"i is $i")
        })*/


        val p = ii.headOption2

        println(s"p is $p")


        //println()

        /*val io = mStream.headOption.get

        println(s"io is $io")*/

        /*val p = mStream.toListFast()
        println(mStream.takeWhile(a => a <= 3).toListFast())*/

    }
}
