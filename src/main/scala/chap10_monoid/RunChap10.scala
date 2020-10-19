package chap10_monoid

import chap10_monoid.MMonoid.{foldMap, foldMapV, intAddition}

object RunChap10 {
    def main(args: Array[String]): Unit = {
        val lString = List("phuvh", "haonc", "khangvt")
        val acc = foldMap(lString, intAddition)(str => str.length)
        println(s"acc is $acc")


        val ff: (Int, Double) => Double = (a, b) => {
            a + b
        }


        val ff2 = ff.curried

        val f3 = ff2(5)

        val b = f3(6)

        val seq = IndexedSeq(1, 3, 45, 6, 7)

        val m = foldMapV(seq, intAddition)(a => a)

        println(s"m is $m")



        /*import learn_fp_in_scala.chap7.Par
        Par.parMap()*/

    }


}
