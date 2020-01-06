package learning_scalaz

import useoop.MLogger

import scala.concurrent.{ExecutionContext, Future}


object AbstractionOverExecution {

    trait Terminal[C[_]] {
        def read: C[String]

        def write(string: String): C[Unit]
    }

    //ta không thể viết Terminal[String] nên phải viết như thế này
    //coi như kiểu Now[X] sẽ thay cho kiểu X, tuy nhiên được đối xử như là một type constructor
    type Now[X] = X

    implicit object TerminalNow extends Terminal[Now] {
        override def read: String = {
            "phuvh"
        }

        override def write(string: String): Unit = {
            MLogger.generalLogger.debug("str is {}", string)
        }
    }

    implicit object TerminalAsync extends Terminal[Future] {
        override def read: Future[String] = {
            Future.successful[String]("phuvh")
        }

        override def write(string: String): Future[Unit] = {
            MLogger.generalLogger.debug("write string from future {}", string)
            Future.unit
        }
    }

    implicit val terminalOption: Terminal[Option] = new Terminal[Option] {
        override def read: Option[String] = Option("phuvh option")

        override def write(string: String): Option[Unit] = {
            MLogger.generalLogger.debug("string from terminal option {}", string)
            Option.empty
        }
    }

    trait Execution[C[_]] {
        def chain[A, B](ca: C[A])(fFlatMap: A => C[B]): C[B]

        def create[B](b: B): C[B]
    }


    object Execution {

        // ngầm định cung cấp hàm map và flatMap cho các container
        implicit class Ops[A, C[_]](val containerOrigin: C[A]) extends AnyVal {
            def flatMap[B](f: A => C[B])(implicit execution: Execution[C]): C[B] = {
                execution.chain(containerOrigin)(f)
            }

            def map[B](f: A => B)(implicit execution: Execution[C]): C[B] = {
                execution.chain(containerOrigin)(f andThen execution.create)
            }
        }

    }

    def echo[C[_]](t: Terminal[C], e: Execution[C]) = {
        e.chain(t.read)(in => {
            e.chain(t.write(in))(_ => e.create(in))
        })
    }


    implicit object ExecutionNow extends Execution[Now] {
        override def chain[A, B](ca: A)(fmap: A => B): B = {
            fmap(ca)
        }

        override def create[B](b: B): B = b

    }

    implicit object ExecutionOption extends Execution[Option] {
        override def chain[A, B](ca: Option[A])(fFlatMap: A => Option[B]): Option[B] = {
            ca.flatMap(fFlatMap)
        }

        override def create[B](b: B): Option[B] = {
            Option(b)
        }
    }


    implicit object ExecutionFuture extends Execution[Future] {
        override def chain[A, B](ca: Future[A])(fFlatMap: A => Future[B]): Future[B] = {
            ca.flatMap(fFlatMap)(ExecutionContext.global)
        }

        override def create[B](b: B): Future[B] = {
            Future.successful(b)
        }
    }


    // implicit cả 2 tham số
    def echoFlatMap[C[_]](implicit terminal: Terminal[C], execution: Execution[C]): Unit = {
        import Execution._
        val outStr = for {
            in <- terminal.read
            i2 <- terminal.write(in)
        } yield in

        MLogger.generalLogger.debug("out is {}", outStr)
    }

    def echo2[C[_]](implicit terminal: Terminal[C], execution: Execution[C]): Unit = {
        import Execution._
        terminal.read.flatMap(in => {
            terminal.write(in).map(_ => in)
        })
    }


    def main(args: Array[String]): Unit = {
        /* echoFlatMap[Future]
         echoFlatMap[Now]

         echoFlatMap[Option]


         val o = Option((1, 3))

         val ff = for {
             y <- o
             (first, _) = y
         } yield first*/


        val pf1: PartialFunction[String, Int] = {
            case s: String if s.length == 7 => {
                7
            }
        }
        val pf2: PartialFunction[String, Int] = {
            case s: String => 9
        }

        val r = pf1.orElse(pf2)("akjdnf")
        println(r)
    }
}
