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

    @tailrec
    final def exist(condition: A => Boolean): Boolean = {
        this match {
            case Empty => false
            case Cons(head, tail) => {
                /*if (condition(head())) true
                else {
                    tail().exist(condition)
                }*/
                condition(head()) || tail().exist(condition)
            }
        }
    }


    // hàm này ngu ở chỗ nó phải duyệt hết cả stream,
    // tuy nhiên diểm sáng là nó sẽ không tính toán lại các biểu thức cond(a) sau khi đã gặp giá trị true
    def exist2(cond: A => Boolean): Boolean = {
        foldRight[Boolean](false)((a, b) => cond(a) || b)
    }

    // sẽ trả về giá trị tích lũy ngay khi gặp empty
    // hàm fold này chưa được tối ưu do sử dụng đệ quy đầu
    def foldRight[B](currentAcc: => B)(f: (A, => B) => B): B = {
        this match {
            case Empty => currentAcc
            case Cons(head, tail) => {
                f(head(), tail().foldRight(currentAcc)(f))
            }
        }
    }


    def forAll(condition: A => Boolean): Boolean = {
        foldRight[Boolean](false)((a, b) => condition(a) && b)
    }

}

case object Empty extends MStream[Nothing]

case class Cons[+A](head: () => A, tail: () => MStream[A]) extends MStream[A]

object MStream {
    def empty[A]: MStream[A] = Empty


    // sử dụng call by name để delay evaluation
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

        @tailrec
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


    //cài đặt hiện tại đang bị ngược, cần sửa lại
    def takeN[A](streamOrigin: MStream[A], n: Int): MStream[A] = {
        if (n <= 0) empty
        else {
            @tailrec
            def takeRecursive[A](count: Int, target: Int,
                                 streamCr: MStream[A], streamAcc: MStream[A]): MStream[A] = {
                streamCr match {
                    // trong trường hợp n vượt quá size của origin stream
                    case Empty => streamAcc
                    case Cons(head, tail) => {
                        if (count == target) streamAcc
                        else {
                            takeRecursive(count + 1, target, tail(), cons(head(), streamAcc))
                        }
                    }
                }
            }

            takeRecursive(0, n, streamOrigin, empty)
        }
    }

    def takeN2[A](stream: MStream[A], n: Int): MStream[A] = {
        def take2Recursive[A](count: Int, stream: MStream[A]): MStream[A] = {
            if (count == 0) {
                empty
            } else {
                stream match {
                    case Cons(head, tail) => {
                        if (n == 1) {
                            cons(head(), empty)
                        }
                        // nếu vẫn lớn hơn 1
                        else {
                            cons(head(), take2Recursive(count - 1, tail()))
                        }
                    }
                    case Empty => {
                        empty
                    }
                }
            }
        }

        take2Recursive(n, stream)
    }


    def dropN[A](stream: MStream[A], n: Int): MStream[A] = {

        @tailrec
        def dropNRecursive[A](target: Int, dropCount: Int, currentStream: MStream[A]): MStream[A] = {
            currentStream match {
                case Empty => currentStream
                case Cons(_, tail) => {
                    if (dropCount == target) {
                        currentStream
                    } else {
                        dropNRecursive(target, dropCount + 1, tail())
                    }
                }
            }
        }

        dropNRecursive(n, 0, stream)
    }

    def takeWhile[A](predicate: A => Boolean, stream: MStream[A]): MStream[A] = {
        def funTakeWhileRecursive[A](continue: Boolean, stream: MStream[A],
                                     predicate: A => Boolean): MStream[A] = {
            stream match {
                case Empty => empty
                case Cons(head, tail) => {
                    // continue có nghĩa là có xét phần tử sau phần tử này hay không
                    if (continue) {
                        val newContinue = tail().headOption match {
                            case None => false
                            case Some(value) => predicate(value)
                        }
                        cons(head(), funTakeWhileRecursive(newContinue, tail(), predicate))
                    }
                    else {
                        empty
                    }
                }
            }
        }

        stream match {
            case Empty => empty
            case Cons(head, _) => {
                val initContinue = predicate(head())
                funTakeWhileRecursive(initContinue, stream, predicate)
            }
        }
    }

    def takeWhile2[A](condition: A => Boolean, stream: MStream[A]): MStream[A] = {
        stream.foldRight[MStream[A]](empty)((currentVal, currentAcc) => {

            // nếu
            if (condition(currentVal)) {
                //println(s"a is $a")
                cons(currentVal, currentAcc)
            } else {
                empty
            }
        })
    }


    implicit class MStreamOps[A](val stream: MStream[A]) extends AnyVal {
        def toList(): List[A] = {
            MStream.toList(stream).reverse
        }

        def toListFast(): List[A] = {
            MStream.toListFast(stream)
        }

        def takeN(n: Int): MStream[A] = {
            MStream.takeN(stream, n)
        }

        def dropN(n: Int): MStream[A] = {
            MStream.dropN(stream, n)
        }

        def take2N(n: Int): MStream[A] = {
            MStream.takeN2(stream, n)
        }

        def takeWhile(predicate: A => Boolean): MStream[A] = {
            //MStream.takeWhile(predicate, stream)
            MStream.takeWhile2(predicate, stream)
        }
    }

}
