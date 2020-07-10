package uselazy

object MainLazy {

    def useLazy(a: => Int): Unit = {
        val i = a
        println("2a is " + (i + i))
    }

    def main(args: Array[String]): Unit = {
        val mStream = MStream(1, 3, 4, 56, 68, 78, 1)
        val p = mStream.toListFast()
        println(mStream.takeWhile(a => a <= 3).toListFast())

    }
}
