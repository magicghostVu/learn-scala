package mcollection

import useoop.MLogger

import scala.collection.SeqView

object RunMCollection {


    def useStream(): Unit = {


        def fib: Stream[Int] = Stream.cons(0,
            Stream.cons(1, fib.zip(fib.tail).map(t => t._1 + t._2)))

        val u = fib(10)
        MLogger.generalLogger.info("u is {}", u)


    }

    def testView(): Unit = {
        val sep: Seq[String] = Seq[String]("Java", "Scala", "Python", "JavaScript")

        val i: SeqView[String, Seq[String]] = sep.view

        MLogger.generalLogger.info("i is {}", i)

        val res = i.map(str => str.length).map(int => {
            val f: (Int) => Int = arg => int + arg
            f
        })


        val u = res(1)(4)

        MLogger.generalLogger.info("u is {}", u)

    }

    def main(args: Array[String]): Unit = {
        /*val sep: Seq[String] = Seq[String]("Java", "Scala", "Python")
        MLogger.generalLogger.info("sep is {}", sep)
        val s2 = sep.withFilter(_.contains("J"))


        val partialFunction: PartialFunction[Int, String] = {
            case x: Int => "phuvh"
        }

        val seq2 = sep.orElse(partialFunction)

        val ss = seq2(4)
        //ss


        MLogger.generalLogger.info("ss is {}", ss)*/

        //testView()
        useStream()

    }

}
