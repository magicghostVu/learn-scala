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


    def headOption2(): Option[A] = {
        val o: Option[A] = None
        foldRight(o)((a, b) => {
            Some(a)
        })
    }

    @tailrec
    final def exist(condition: A => Boolean): Boolean = {
        this match {
            case Empty => {

                println("đã hết, exist")

                false
            }
            case Cons(head, tail) => {
                /*if (condition(head())) true
                else {
                    tail().exist(condition)
                }*/
                condition(head()) || tail().exist(condition)
            }
        }
    }

    // tuy nhiên diểm sáng là nó sẽ return ngay khi gặp được giá trị cần tìm mà k duyệt đến cuối
    def exist2(cond: A => Boolean): Boolean = {
        foldRight[Boolean](false)((currentElement, currentBool) => cond(currentElement) || currentBool)
    }


    // hàm fold này chưa được tối ưu do sử dụng đệ quy đầu nhưng trong fold function nó có để 1 tham số là lazy để tối ưu hóa performance

    def foldRight[B](currentAcc: => B)(foldFunction: (A, => B) => B): B = {
        this match {
            case Empty => { // nếu stream đã hết thì trả về giá trị tích lũy hiện tại

                ///println("đã hểt")
                currentAcc
            }
            case Cons(head, tail) => {
                // bất cứ khi nào hàm fold có thể trả về được kết quả, thì hàm foldRight này sẽ return mà k cần chạy đến cuối stream
                // do trong quá trình chạy có thể không cần evaluate tham số thứ 2 của foldFunction
                foldFunction(head(), tail().foldRight(currentAcc)(foldFunction))
            }
        }
    }


    def forAll(condition: A => Boolean): Boolean = {
        foldRight[Boolean](false)((a, b) => condition(a) && b)
    }


    def forEach(function: A => Unit): Unit = {
        this match {
            case Cons(head, tail) => {
                function(head())
                tail().forEach(function)
            }
            case _ =>
        }
    }


    def map[B](fMap: A => B): MStream[B] = {
        foldRight[MStream[B]](Empty: MStream[B])((a, b) => {
            Cons(() => fMap(a), () => b)
        })
    }

    // failed -> not lazy
    def mapv2[B](fMap: A => B): MStream[B] = {
        @tailrec
        def mapRecursive[E, F](currentStream: MStream[E], streamAccumulate: MStream[F], fMap: E => F): MStream[F] = {
            currentStream match {
                case Empty => streamAccumulate
                case Cons(head, tail) => {
                    mapRecursive(tail(), Cons[F](() => fMap(head()), () => streamAccumulate), fMap)
                }
            }
        }

        mapRecursive(this, Empty, fMap)
    }

    def filter(fFilter: A => Boolean): MStream[A] = {
        foldRight(Empty: MStream[A])((a, b) => {
            if (fFilter(a)) {
                Cons(() => a, () => b)
            } else {
                b
            }
        })
    }

    def appendElement[B >: A](b: => B): MStream[B] = {
        val streamB: MStream[B] = MStream.cons(b, Empty)
        foldRight(streamB)((a, b) => {

            //println("a is " + a)
            MStream.cons(a, b)
        })
    }

    def append[B >: A](streamB: => MStream[B]): MStream[B] = {
        foldRight(streamB)((a, b) => {
            MStream.cons(a, b)
        })
    }


}

case object Empty extends MStream[Nothing]

case class Cons[+A](head: () => A, tail: () => MStream[A]) extends MStream[A]

object MStream {
    def empty[A]: MStream[A] = Empty


    // sử dụng call by name để delay evaluation
    def cons[A](head: => A, tail: => MStream[A]): MStream[A] = {

        // tạo mới các biến lazy để delay evaluation các tham số bên trên trong trường hợp các tham số này là một hàm
        // và sẽ memorize giá trị này nếu đã được evaluate -> chỉ evaluate một lần
        lazy val h = head
        lazy val t = tail

        //tạo mới các function để delay việc tính toán giá trị của head và tail
        //giá trị bên trong head và tail chỉ thực sự được tính toán khi gọi đến
        Cons(() => h, () => t)
    }


    // tạo một stream vô tận với hằng số đầu vào
    def constant[A](a: A): MStream[A] = {
        lazy val y: MStream[A] = cons(a, y)
        y
    }

    def from(a: Int): MStream[Int] = {
        cons(a, from(a + 1))
    }

    def fibs(): MStream[Int] = {
        def fibsRecursive(a0: Int, a1: Int): MStream[Int] = {

            //sẽ k gây stack over flow tại thời điểm gọi hàm này thì cái kết quả của cái fib bên dưới chưa được evaluate
            cons(a0, fibsRecursive(a1, a0 + a1))
        }

        fibsRecursive(0, 1)
    }


    // lazy, trả về ngay lập tức
    def unfold[A, S](z: S)(f: S => Option[(A, S)]): MStream[A] = {
        f(z) match {
            case None => empty
            case Some(value) => cons(value._1, unfold(value._2)(f))
        }
    }


    def mapBasedUnfold[A, B](streamOrigin: MStream[A], f: A => B): MStream[B] = {
        MStream.unfold(streamOrigin) {
            case Empty => None
            case Cons(head, tail) => {
                Some((f(head()), tail()))
            }
        }
    }


    def apply[A](elements: A*): MStream[A] = {
        if (elements.isEmpty) {
            empty
        } else {
            //val h = elements.head
            //println(s"h is $h")
            cons(elements.head, apply(elements.tail: _*))
        }
    }


    @tailrec
    private def toList[A](current: List[A], stream: MStream[A]): List[A] = {
        stream match {
            case Empty => current
            case Cons(head, tail) => {
                // nối giá trị hiện tại vào đầu danh sách tích lũy
                toList(head() :: current, tail())
            }
        }
    }

    private def toList[A](stream: MStream[A]): List[A] = {
        toList(List(), stream)
    }

    def toListFast[A](stream: MStream[A]): List[A] = {
        @tailrec
        def funRecursive[B](buffAccumulate: ListBuffer[B], streamRemain: MStream[B]): ListBuffer[B] = {
            streamRemain match {
                case Empty => buffAccumulate
                case Cons(head, tail) => {
                    val h = head()
                    println(s"head is $h")
                    funRecursive(buffAccumulate.appended(head()), tail())
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
            def takeRecursive[X](count: Int, target: Int,
                                 streamCr: MStream[X], streamAcc: MStream[X]): MStream[X] = {
                streamCr match {
                    // trong trường hợp n vượt quá size của origin stream
                    case Empty => streamAcc
                    case Cons(head, tail) => {
                        if (count == target) streamAcc
                        else {

                            // nối vào đầu thì chả ngược
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
        stream.foldRight[MStream[A]](empty)((currentVal, currentStreamAcc) => {

            // nếu vẫn còn phần tử thỏa mãn thì nối giá trị hiện tại vào
            if (condition(currentVal)) {
                println(s"a is $currentVal")
                cons(currentVal, currentStreamAcc)
            }
            else {
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

        def sum(implicit add: Add[A]): A = {
            stream.foldRight[A](add.initValue)((b, c) => {
                add.add(b, c)
            })
        }
    }

}
