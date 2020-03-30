package learn_fp_in_scala.chap7

import java.util.concurrent.{Callable, ExecutorService, Future, TimeUnit}


// một container cho việc tính toán song song
// thực ra nó không phải là một container mà đó là ghi lại mô tả về việc sẽ tính toán song song như thế nào
/*class Par[A] {

}*/

object Par {


    private case class UnitFuture[A](get: A) extends Future[A] {
        override def cancel(mayInterruptIfRunning: Boolean): Boolean = false

        override def isCancelled: Boolean = false

        override def isDone: Boolean = true

        //override def get(): A = a

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


        // hàm này có thể bị gọi 2 lần trong 2 thread khác nhau, và dòng 50 có thể chạy 2 lần
        // tuy nhiên điều này có thể chấp nhận được
        override def get(): C = {
            optionC match {
                case None => {
                    val c: C = compute(Long.MaxValue)
                    c
                }
                case Some(value) => value
            }
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
                    val start = System.nanoTime
                    val ar = futureA.get(timeoutInNanos, TimeUnit.NANOSECONDS)
                    val stop = System.nanoTime
                    val aTime = stop - start
                    val br = futureB.get(timeoutInNanos - aTime, TimeUnit.NANOSECONDS)
                    val ret = f(ar, br)
                    optionC = Some(ret)
                    ret
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
    def run[A](par: Par[A])(implicit executorService: ExecutorService): Future[A] = par(executorService)


    // thực ra hàm này có vai trò như hàm combine, chứ nó không phải là map
    def map2[A, B, C](pa: Par[A], pb: Par[B])(functionMap: (A, B) => C): Par[C] = {
        es: ExecutorService => {
            val futureA = pa(es)
            val futureB = pb(es)
            UnitFuture(functionMap(futureA.get(), futureB.get()))
        }
    }


    // trả về một folk cho par A nếu như có một par A hoặc là một hàm trả ra par A
    // ngụ ý rằng việc evaluate cái kết quả trả ra của hàm này sẽ được chạy trên luồng khác
    def folk[A](a: => Par[A]): Par[A] = {
        es: ExecutorService => {

            //submit một callable cho es
            // callable đó lại thực ra là lấy thực thi parA và lấy ra A
            es.submit(() => a(es).get())
        }
    }


    // ngụ ý rằng hàm trả ra A này sẽ được chạy trên một thread khác
    def lazyUnit[A](a: => A): Par[A] = {
        folk[A](unit[A](a))
    }


    // sử dụng lazyUnit để viết hàm này
    // async 1 hàm
    def asyncF[A, B](f: A => B): A => Par[B] = {
        a: A => lazyUnit(f(a))
    }

}

