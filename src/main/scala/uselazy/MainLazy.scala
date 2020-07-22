package uselazy

object MainLazy {

    def useLazy(a: => Int): Unit = {
        val i = a
        println("2a is " + (i + i))
    }

    def main(args: Array[String]): Unit = {
        val mList = List(3, 6, 7, 89, 9, 4, 3)
        val stream1 = MStream(mList: _*)
        val l2 = List(2, 5, 6, 78, 8)
        val stream2 = MStream(l2: _*)
        val r = stream1.append(stream2)


        //r.forEach(println)
        val infStream = MStream.fibs()

        import Add.addForInt
        val pp = infStream.takeN(10).sum

        println("pp is " + pp)


        val pp2 = MStream.unfold2(10)(i => {

            Some((i, i - 1))

        })


        val l = pp2.take2N(6).toList()
        println(s"l is $l")

        println("done")

    }
}
