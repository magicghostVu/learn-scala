package learning_scalaz

import scalaz._
import scalaz.Scalaz._
import useoop.MLogger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object ForCompre {

    def transformMonad(future: Future[Int]): Future[(Int, Unit)] = {
        future.map(i => {
            (i, {
                println("i is " + i)
            })
        })
    }

    def testEarlyExitFuture: Unit = {

        //OptionT

        val f1 = Future.successful(4)
        val f2 = Future.successful(1)


        for {
            a <- transformMonad(f1)

            c <- if (a._1 < 0) Future.successful(0)
            else for {b <- f2} yield a._1 * b
        } yield c
    }

    def optionT(): Unit = {
        val f1 = Future.successful(Option(1))
        val f2 = Future.successful(Option(2))

        val tmp = f1.liftM[OptionT]


        val o = Option(0)


        val f3 = for {
            i <- OptionT(f1)
            j <- OptionT(f2)
        } yield i + j

        val f = f3.run


    }

    def main(args: Array[String]): Unit = {
        //testEarlyExitFuture
        optionT
    }

}
