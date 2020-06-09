package learn_fp_in_scala.chap7

import java.util.concurrent.{ExecutorService, Executors}

import learn_fp_in_scala.chap7.Par.Par


object RunR {


    implicit val exe: ExecutorService = Executors.newFixedThreadPool(1)

    def tryControlFlow(a: Int): Int = {
        if (a < 7) 8
        else
            5
    }

    def tryFoldRecursive(indexedSeq: IndexedSeq[Int]): Int = {

        println("size is " + indexedSeq.size)

        if (indexedSeq.size <= 1) {
            indexedSeq.headOption.getOrElse(0)
        } else {
            val sizeHalf = indexedSeq.size / 2
            val (l, r) = indexedSeq.splitAt(sizeHalf)
            tryFoldRecursive(l) + tryFoldRecursive(r)
        }

    }

    /*def sum(indexedSeq: IndexedSeq[Int]): Int = {
        if (indexedSeq.size <= 1) {
            indexedSeq.headOption.getOrElse(0)
        } else {
            val halfSize = indexedSeq.size / 2

            val (l, r) = indexedSeq.splitAt(halfSize)

            val sumL = Par.unit[Int](sum(l))
            val sumR = Par.unit[Int](sum(r))

            Par.run[Int](sumL) + Par.run[Int](sumR)

        }
    }*/


    /*def sum2(indexedSeq: IndexedSeq[Int]): Par[Int] = {
        if (indexedSeq.size <= 1) {
            Par.unit(indexedSeq.headOption.getOrElse(0))
        } else {
            val (l, r) = indexedSeq.splitAt(indexedSeq.size / 2)

            //
            val pl = sum2(l)
            val pr = sum2(r)

            // ở bước này ta sẽ thực thi 2 cái folk bên dưới riêng biệt rồi sau đó dùng map2 gộp lại
            Par.map2(Par.folk(pl), Par.folk(pr))(_ + _)
        }

    }*/


    def main(args: Array[String]): Unit = {
        val l: List[Int] = List(1, 2, 3, 4, 5, 6)

        /*l.foldRight(0)((a, b) => {

            val s = s"a is $a, b is $b"
            println(s)
            a + b
        })*/

        val sum = tryFoldRecursive(Vector[Int](1, 2, 3, 4, 5, 6, 7, 8))
        println("sum is " + sum)


        //Actor[String]

    }

}
