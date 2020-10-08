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
        val r = stream1.append(stream2).takeBasedUnfold(3)
        r.forEach(println)
        r.sum

        val k = MStream.constantBasedUnfold(6).takeBasedUnfold(2)

        val g = k.zipAll(MStream.fibs().takeBasedUnfold(5))

        g.takeBasedUnfold(6).forEach(println)


        val stream3 = MStream(2, 5)
        val startWith3 = stream2.zipAll(stream3)
            .filter(po => {


                po._1.isDefined && po._2.isDefined
            })
            .map(p => {
                val r = for {
                    v1 <- p._1
                    v2 <- p._2
                } yield v1.equals(v2)
                r match {
                    case None => false
                    case Some(value) => value
                }
            })

        println(startWith3.forAll(_ == true))

        //println(s"k is $k")
    }
}
