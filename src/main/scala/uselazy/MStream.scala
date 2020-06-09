package uselazy

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

sealed trait MStream[+A] {
    def headOption: Option[A] = {
        this match {
            case Empty => None
            case Cons(head, _) => Some(head())
        }
    }


}

case object Empty extends MStream[Nothing]

case class Cons[+A](head: () => A, tail: () => MStream[A]) extends MStream[A]

object MStream {
    def empty[A]: MStream[A] = Empty


    // sử dụng call by name
    def cons[A](head: => A, tail: => MStream[A]): MStream[A] = {
        lazy val h = head
        lazy val t = tail
        Cons(() => h, () => t)
    }

    def apply[A](a: A*): MStream[A] = {
        if (a.isEmpty) {
            empty
        } else {
            cons(a.head, apply(a.tail: _*))
        }
    }

    @tailrec
    private def toList[A](current: List[A], stream: MStream[A]): List[A] = {
        stream match {
            case Empty => current
            case Cons(head, tail) => {
                toList(head() :: current, tail())
            }
        }
    }

    private def toList[A](stream: MStream[A]): List[A] = {
        toList(List(), stream)
    }

    def toListFast[A](stream: MStream[A]): List[A] = {
        def funRecursive[B](buff: ListBuffer[B], stream: MStream[B]): ListBuffer[B] = {
            stream match {
                case Empty => buff
                case Cons(head, tail) => {
                    funRecursive(buff.appended(head()), tail())
                }
            }
        }

        funRecursive(ListBuffer.empty, stream).toList
    }


    def takeN[A](stream: MStream[A], n: Int): MStream[A] = {
        if (n <= 0) empty
        else {

        }
    }

    implicit class MStreamOps[A](val stream: MStream[A]) extends AnyVal {
        def toList(): List[A] = {
            MStream.toList(stream).reverse
        }

        def toListFast(): List[A] = {
            MStream.toListFast(stream)
        }
    }

}
