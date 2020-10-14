package chap10_monoid

trait MMonoid[A] {
    def op(a1: A, a2: A): A

    val zero: A
}

object MMonoid {
    val intAddition: MMonoid[Int] = new MMonoid[Int] {
        override def op(a1: Int, a2: Int): Int = a1 + a2

        override val zero: Int = 0
    }

    val intMultiplication: MMonoid[Int] = new MMonoid[Int] {
        override def op(a1: Int, a2: Int): Int = a1 * a2

        override val zero: Int = 1
    }

    val booleanOr: MMonoid[Boolean] = new MMonoid[Boolean] {
        override def op(a1: Boolean, a2: Boolean): Boolean = a1 || a2

        override val zero: Boolean = true
    }

    val booleanAnd: MMonoid[Boolean] = new MMonoid[Boolean] {
        override def op(a1: Boolean, a2: Boolean): Boolean = a1 && a2

        override val zero: Boolean = false
    }


    val stringMonoid: MMonoid[String] = new MMonoid[String] {
        override def op(a1: String, a2: String): String = a1 + a2

        override val zero: String = ""
    }


    def concatList[A](la: List[A])(monoid: MMonoid[A]): A = {
        la.foldRight(monoid.zero)(monoid.op)
    }


    def endoMonoid[A]: MMonoid[A => A] = new MMonoid[A => A] {
        override def op(a1: A => A, a2: A => A): A => A = a1.andThen(a2)

        override val zero: A => A = (a: A) => a
    }


    def foldMap[A, B](listA: List[A], monoIdB: MMonoid[B])(f: A => B): B = {
        concatList(listA.map(f))(monoIdB)
    }



    // ý tưởng chính là biến List[A] và monoid[B] và cái f thành một function dạng B=> B, sau đó áp function này với startVal
    def foldRight[A, B](la: List[A], monoid: MMonoid[B])(startVal:B)(f: (A,B) => B) = {
        val fCurried= f.curried


        //    foldMap()

        val newF= foldMap()

    }


    //tuy là monoid này vẫn thỏa mãn monoid law nhưng mà nó sẽ
    // không có tính chất giao hoán với đối với 2 tham số trong op
    def optionMonoid[A]: MMonoid[Option[A]] = new MMonoid[Option[A]] {
        override def op(a1: Option[A], a2: Option[A]): Option[A] = a1.orElse(a2)

        override val zero: Option[A] = None
    }

    // biến đổi một monoid thành một monoid có op được truyền tham số theo thứ tự ngược lại
    def dual[A](origin: MMonoid[A]): MMonoid[A] = {
        new MMonoid[A] {
            override def op(a1: A, a2: A): A = origin.op(a2, a1)

            override val zero: A = origin.zero
        }
    }
}
