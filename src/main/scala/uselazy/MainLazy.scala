package uselazy

object MainLazy {

    def useLazy(a: => Int): Unit = {
        val i = a
        println("2a is " + (i + i))
    }

    def main(args: Array[String]): Unit = {
        val mStream = MStream(1, 3, 4, 5)
        val p = mStream.toListFast()
        println(p)

    }
}
