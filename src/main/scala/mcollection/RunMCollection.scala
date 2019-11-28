package mcollection

import useoop.MLogger

import scala.collection.mutable.ListBuffer

import scala.collection.SeqView

object RunMCollection {


    def useListBuffer(): Unit = {
        val listBuf: ListBuffer[String] = new ListBuffer[String]()
        listBuf.append("phuvh", "vint")
        listBuf(1) = "phuvh1"

        val mm: Map[Int, String] = Map(1 -> "phuvh", 2 -> "vint")


    }


    def useStream(): Unit = {

        lazy val fib: Stream[Int] = Stream.cons(0, Stream.cons(1, fib.zip(fib.tail).map(t => {
            MLogger.generalLogger.info("t1 is {}, t2 is {}", t._1, t._2)
            t._1 + t._2
        })))

        lazy val fact: Stream[Int] = 1 #:: fact.zipWithIndex.map { case (p, x) => {
            print(s" p is $p, x is $x");
            p * (x + 1)
        }
        }


        MLogger.generalLogger.info("fib is {}", fact(5))
    }

    def testView(): Unit = {
        val sep: Seq[String] = Seq[String]("Java", "Scala", "Python", "JavaScript")

        val sum = sep.foldLeft[Int](0)((int, str) => int + str.length)

        MLogger.generalLogger.info("sum is {}", sum)

        val i: SeqView[String, Seq[String]] = sep.view

        MLogger.generalLogger.info("i is {}", i)

        val res = i.map(str => str.length).map(int => {
            val f: Int => Int = arg => int + arg
            f
        })
        val u = res(1)(4)

        MLogger.generalLogger.info("u is {}", u)

    }

    def main(args: Array[String]): Unit = {

        /*val sep = Seq[String]("Java", "Python", "Scala")

        MLogger.generalLogger.info("class is {}", sep)
        val pFun: PartialFunction[Int, String] = sep

        val g: PartialFunction[Int, String] = {
            case i: Int => "phuvh"
        }

        val pp = pFun.orElse(g)

        MLogger.generalLogger.info("class is {}", sep)
        val s2 = sep.withFilter(_.contains("J"))


        val partialFunction: PartialFunction[Int, String] = {
            case x: Int => "phuvh"
        }

        val seq2 = sep.orElse(partialFunction)

        val ss = seq2(4)
        //ss


        MLogger.generalLogger.info("ss is {}", ss)*/

        //testView()
        /*val o = 0
        println(s"o $o")*/

        //useStream()
        testView()

    }

}
