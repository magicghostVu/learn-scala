package functional.use_monad.io_monad

import useoop.MLogger

import scala.io.StdIn


sealed trait IO[A] {
    self =>
    def run: A

    def map[B](fMap: A => B): IO[B] = {
        new IO[B] {
            override def run: B = fMap(self.run)
        }
    }

    def flatMap[B](functionBind: A => IO[B]): IO[B] = {
        new IO[B] {
            override def run: B = functionBind(self.run).run
        }
    }
}

object IO {
    def apply[A](a: => A): IO[A] = {
        new IO[A] {
            override def run: A = a
        }
    }
}


object RunIoMonad {

    def mreadLine: IO[String] = {
        new IO[String] {
            override def run: String = StdIn.readLine()
        }
    }

    def mprintLine(string: String): IO[Unit] = {
        new IO[Unit] {
            override def run: Unit = {
                //MLogger.generalLogger.error("call from ", new Exception())
                println(string)
                // č
            }
        }
    }

    def main(args: Array[String]): Unit = {
        val h = for {
            _ <- mprintLine("type degree in C")
            d <- mreadLine.map(_.toDouble)
            _ <- mprintLine(s"d is $d")
        } yield ()

        h.run
    }
}
