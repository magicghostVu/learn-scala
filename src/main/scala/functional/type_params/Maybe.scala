package functional.type_params

sealed abstract class Maybe[+A] {
    def isEmpty: Boolean

    def get: A

    def getOrElse[B >: A](default: B): B = {
        if (isEmpty) default else get
    }

    def map[B](mapper: A => B): Maybe[B]
}

case class Just[A](v: A) extends Maybe[A] {
    override def isEmpty: Boolean = false

    override def get: A = v

    override def map[B](mapper: A => B): Maybe[B] = {
        Maybe.just(mapper(v))
    }

    override def toString: String = {
        "(just " + v.toString + ")"
    }
}

object Nil extends Maybe[Nothing] {
    override def isEmpty: Boolean = true

    override def get: Nothing = throw new IllegalArgumentException("can not get val from Nil")

    override def map[B](mapper: Nothing => B): Maybe[B] = Nil

    override def toString: String = "Nil"
}

object Maybe {
    def just[A](a: A): Just[A] = {
        Just[A](a)
    }

    def nil: Nil.type = Nil

}
