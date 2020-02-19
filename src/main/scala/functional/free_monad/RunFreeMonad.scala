package functional.free_monad

import useoop.MLogger


// free monad sẽ wrap cái gì tùy thuộc nó thuộc loại nào trong 3 class bên dưới
// S[A] ở đây là một ADT (kiểu dữ liệu đại số) 

sealed trait Free[S[_], A] {
    def map[B](fmap: A => B): Free[S, B] = flatMap(a => Free.unit(fmap(a)))

    def flatMap[B](bindingFunction: A => Free[S, B]): Free[S, B] = ???

}


object Free {
    def unit[S[_], A](a: A): Free[S, A] = Return[S, A](a)

    //def lift[S[_], A](sa: S[A]): Free[S, A] = Suspend(sa)
}

// wrap một giá trị, tương đương với hàm unit
case class Return[S[_], A](a: A) extends Free[S, A]


// wrap một wrapper khác
case class Suspend[S[_], A](sfa: S[Free[S, A]]) extends Free[S, A]


// wrap một một free monad và một binding function
//case class FlatMapped[S[_], A, B](fsa: Free[S, A], f: A => Free[S, B]) extends Free[S, B]


//ase class Map[S[_], A, B](fa: Free[S, A], fmap: A => B) extends Free[S, B]


object RunFreeMonad {
    def main(args: Array[String]): Unit = {
        MLogger.generalLogger.debug("ok, run")
    }

}
