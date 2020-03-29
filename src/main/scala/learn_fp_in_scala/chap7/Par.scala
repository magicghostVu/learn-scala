package learn_fp_in_scala.chap7

import java.util.concurrent.{Callable, ExecutorService, Future, TimeUnit}


// một container cho việc tính toán song song
// thực ra nó không phải là một container mà đó là gh lại mô tả về việc sẽ tính toán song song như thế nào
/*class Par[A] {

}*/

object Par {


    private case class UnitFuture[A](a: A) extends Future[A] {
        override def cancel(mayInterruptIfRunning: Boolean): Boolean = false

        override def isCancelled: Boolean = false

        override def isDone: Boolean = true

        override def get(): A = a

        override def get(timeout: Long, unit: TimeUnit): A = a
    }


    //type alias cho một function từ một executor service trả ra một future[A]
    type Par[A] = ExecutorService => Future[A]


    // call by name
    // nhập vào một giá trị và trả về một container (ở đây là một function)
    def unit[A](a: => A): Par[A] = _ => UnitFuture[A](a)


    // thực hiện tính toán và trả ra giá trị
    def run[A](par: Par[A])(implicit executorService: ExecutorService): Future[A] = par(executorService)


    // thực ra hàm này có vai trò như hàm combine, chứ nó không phải là map
    def map2[A, B, C](pa: Par[A], pb: Par[B])(functionMap: (A, B) => C): Par[C] = {
        es => {
            val fa = pa(es)
            val fb = pb(es)
            UnitFuture(functionMap(fa.get(), fb.get()))
        }
    }


    // trả về một folk cho par A nếu như có một par A hoặc là một hàm trả ra par A
    // ngụ ý rằng việc evaluate cái kết quả trả ra của hàm này sẽ được chạy trên luồng khác
    def folk[A](a: => Par[A]): Par[A] = {
        es: ExecutorService => {
            es.submit(() => a(es).get())
        }
    }


    // ngụ ý rằng hàm trả ra A này sẽ được chạy trên một thread khác
    def lazyUnit[A](a: A): Par[A] = {
        folk[A](unit[A](a))
    }

}

