package functional.learn_cats

import java.util.Date

import cats._
import cats.instances.int._
import cats.instances.double._
import cats.instances.list._
import cats.instances.option._
import cats.syntax.show._
import cats.syntax.eq._
import cats.syntax.option._

import useoop.MLogger

object CatsRun {


    case class MyWrapper[A](a: A)

    object MyShowCatsExt {
        implicit val showDate: Show[Date] = Show.show[Date](date => {
            date.toString
        })

        implicit def showMyWrapper[A](implicit show: Show[A]): Show[MyWrapper[A]] = {
            Show.show[MyWrapper[A]](w => {
                val originStr = w.a.show
                s"wrapper ($originStr)"
            })
        }
    }


    object MyEqExt {
        implicit val equalDate: Eq[Date] = (x: Date, y: Date) => {
            x.equals(y)
        }
    }

    def main(args: Array[String]): Unit = {
        import MyShowCatsExt._
        import MyEqExt._

        val d = new Date(System.currentTimeMillis())
        val strDate = d.show
        MLogger.generalLogger.debug("str date is {}", strDate)

        //val u = Eq[Date]

        val d1 = new Date(1579137695000L)
        val d2 = new Date(1579137695000L)

        val e = d1 === d2

        val h = List(d1, d2).show

        MLogger.generalLogger.debug("e is {}, h is {}", e, h)
        d1.some === d2.some

        val k = MyWrapper(d1)
        val k2 = MyWrapper(d2)

        val h1 = MyWrapper(4)


        val ll1 = List(k, k2)

        ll1.show

        val p = k.show

        MLogger.generalLogger.debug("p is {}", p)

    }

}
