package learn_fp_in_scala.chap7

import java.util.concurrent.{Callable, ExecutorService, Executors, Future, TimeUnit}

import useoop.MLogger


// một container cho việc tính toán song song
// thực ra nó không phải là một container mà đó là ghi lại mô tả về việc sẽ tính toán song song như thế nào
/*class Par[A] {

}*/

object Par {


    private case class UnitFuture[A](get: A) extends Future[A] {

        override def cancel(mayInterruptIfRunning: Boolean): Boolean = false

        override def isCancelled: Boolean = false

        override def isDone: Boolean = true

        override def get(timeout: Long, unit: TimeUnit): A = get
    }


    private case class Map2Future[A, B, C](futureA: Future[A], futureB: Future[B], f: (A, B) => C) extends Future[C] {

        @volatile
        private var optionC: Option[C] = Option.empty

        override def cancel(mayInterruptIfRunning: Boolean): Boolean = {
            futureA.cancel(mayInterruptIfRunning) || futureB.cancel(mayInterruptIfRunning)
        }

        override def isCancelled: Boolean = {
            futureA.isCancelled || futureB.isCancelled
        }

        override def isDone: Boolean = {
            optionC.isDefined
        }


        // hàm này có thể bị gọi 2 lần trong 2 thread khác nhau, và dòng 51 có thể chạy 2 lần
        // tuy nhiên điều này có thể chấp nhận được
        override def get(): C = {
            /*optionC match {
                case None => {
                    val c: C = compute(Long.MaxValue)
                    c
                }
                case Some(value) => value
            }*/
            compute(Long.MaxValue)
        }

        override def get(timeout: Long, unit: TimeUnit): C = {
            optionC match {
                case None => compute(TimeUnit.NANOSECONDS.convert(timeout, unit))
                case Some(value) => value
            }
        }

        private def compute(timeoutInNanos: Long): C = {
            optionC match {
                case None => {
                    val startGetA = System.nanoTime
                    val ar = futureA.get(timeoutInNanos, TimeUnit.NANOSECONDS)
                    val stopGetA = System.nanoTime
                    val aTime = stopGetA - startGetA
                    val br = futureB.get(timeoutInNanos - aTime, TimeUnit.NANOSECONDS)
                    val retC = f(ar, br)
                    optionC = Some(retC)
                    retC
                }
                case Some(value) => {
                    value
                }
            }
        }
    }


    //type alias cho một function từ một executor service trả ra một Future[A]
    type Par[A] = ExecutorService => Future[A]


    // call by name
    // nhập vào một giá trị và trả về một container (ở đây là một function)
    def unit[A](a: => A): Par[A] = _ => UnitFuture[A](a)


    // thực hiện tính toán và trả ra giá trị, đây là một hàm có side-effect
    def run[A](par: Par[A])(implicit executorService: ExecutorService): Future[A] = {
        //MLogger.generalLogger.error("call from ", new Exception)
        par(executorService)
    }


    // thực ra hàm này có vai trò như hàm combine, chứ nó không phải là map
    // nó là một hình thái của flatMap, => một phép trừu tượng được giải nghĩa rất đơn giản
    def map2[A, B, C](pa: Par[A], pb: Par[B])(functionCombine: (A, B) => C): Par[C] = {
        es: ExecutorService => {

            // chỗ này đã gọi run
            //tuy nhiên gọi run lại được thực thi ở bước cuối cùng khi ta tính kết quả
            val futureA = pa(es)
            val futureB = pb(es)


            Map2Future(futureA, futureB, functionCombine)
        }
    }


    // trả về một folk cho par A nếu như có một par A hoặc là một hàm trả ra par A
    // ngụ ý rằng việc evaluate cái kết quả trả ra của hàm này sẽ được chạy trên luồng khác
    def folk[A](a: => Par[A]): Par[A] = {
        es: ExecutorService => {
            //submit một callable cho es
            //callable đó lại thực ra là lấy thực thi parA và lấy ra A

            //MLogger.generalLogger.debug("call from", new Exception)

            es.submit(() => a(es).get())
        }
    }


    // sử dụng fold, tuy nhiên cách này sẽ không chạy song song được
    private def simpleSequence[A](ps: List[Par[A]]): Par[List[A]] = {
        ps.foldRight[Par[List[A]]](unit(List[A]()))((pa, parListA) => {
            map2(pa, parListA)(_ :: _)
        })
    }


    private def sequenceRight[A](ps: List[Par[A]]): Par[List[A]] = {
        ps match {
            case Nil => unit(Nil)
            case ::(head, tail) => {
                val parTail = sequenceRight(tail)
                map2(head, parTail)((h, t) => {
                    h :: t
                })
            }

        }
    }

    /*def findMax(listInt: List[Int]): Par[Int] = {

    }*/

    private def balanceSequence[A](ps: IndexedSeq[Par[A]]): Par[IndexedSeq[A]] = {
        if (ps.isEmpty) {
            unit(Vector[A]())
        }
        else if (ps.size == 1) {
            val pa = ps.head
            map(pa)(Vector(_))
        }
        else {
            val (l, r) = ps.splitAt(ps.size / 2)
            val pl = balanceSequence(l)
            val pr = balanceSequence(r)
            map2(pl, pr)((a, b) => a.concat(b))
        }
    }


    // đếm tất cả các từ trong một đoạn văn một cách song song
    // thử xem ta làm được gì... :)))
    def wordCount(listString: List[String]): Par[Int] = {

        // chạy song song các map
        val parListInt: Par[List[Int]] = parMap(listString)(str => {
            MLogger.generalLogger.debug("str is {}", str)
            str.split(" ").length
        })

        val nn = map(parListInt)(listInt => {
            listInt.sum
        })
        nn
    }

    def parFilter[A](listA: List[A])(fFilter: A => Boolean): Par[List[A]] = {
        //map list ban đầu thành một list các Par[List[A]]
        val uu: List[Par[List[A]]] = listA.map(asyncF[A, List[A]](a => {
            if (fFilter(a)) List(a)
            else List()
        }))
        // biến list[par[list]] thành par[list[list]]
        val uu2 = sequence(uu)
        val uu3 = map(uu2)(listList => listList.flatten)
        uu3
    }


    // có thể dùng simpleSequence hoặc là balanceSequence
    def sequence[A](ps: List[Par[A]]): Par[List[A]] = {
        //simpleSequence(ps)
        //map(balanceSequence(ps.toIndexedSeq))(_.toList)
        sequenceRight(ps)
    }

    def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = {
        folk({
            val listParB = ps.map(a => {
                asyncF(f)(a)
            })
            sequence(listParB)
        })
    }


    def parForeach[A](listA: List[A])(f: A => Unit): Par[Unit] = {
        val v1 = parMap(listA)(f)

        map(v1)(_ => ())
    }


    // ngụ ý rằng hàm trả ra A này sẽ được chạy trên một thread khác
    def lazyUnit[A](a: => A): Par[A] = {
        folk[A](unit[A](a))
    }

    def map[A, B](parA: Par[A])(fMap: A => B): Par[B] = {
        map2(parA, unit(()))((a, _) => fMap(a))
    }


    /*def flatMap[A, B](par: Par[A])(f: A => Par[B]): Par[B] = {

    }*/


    // sử dụng lazyUnit để viết hàm này
    // async 1 hàm bất kỳ (từ 1 hàm A=>B sẽ trả ra 1 hàm A=> Par[B])
    // nó sẽ chạy hàm map trên một thread khác
    def asyncF[A, B](f: A => B): A => Par[B] = {
        a => lazyUnit(f(a))
    }

    def sortList(source: Par[List[Int]]): Par[List[Int]] = {
        map(source)(_.sorted)
    }


    implicit class syntax[A](val par: Par[A]) extends AnyVal {
        def run()(implicit executors: ExecutorService): Future[A] = {
            Par.run(par)
        }
    }

    def main(args: Array[String]): Unit = {

        implicit val executor: ExecutorService = Executors.newFixedThreadPool(4)

        val listString = List("Vũ Hồng Phú", "Vũ Văn Quý", "Nguyễn Thuỳ Vi")

        //val parInt = wordCount(listString)

        //parInt.run()

        val u = parForeach(listString)(str => {
            MLogger.generalLogger.debug("foreach str is {}", str)
        })


        u.run()

    }

}

