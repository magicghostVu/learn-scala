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
