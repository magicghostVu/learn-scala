package functional.type_params

sealed abstract class Maybe[+A] {
    def isEmpty: Boolean

    def get: A
}

case class Just[A](v: A) extends Maybe[A] {
    override def isEmpty: Boolean = false

    override def get: A = v
}

object Nil extends Maybe[Nothing] {
    override def isEmpty: Boolean = true

    override def get: Nothing = throw new IllegalArgumentException("can not get val from Nil")
}

object Maybe {
    def just[A](a: A): Just[A] = {
        Just[A](a)
    }

    def nil: Nil.type = Nil
}
