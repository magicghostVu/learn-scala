package uselazy


/// type class cho việc tính sum
trait Add[T] {
    def add(t1: T, t2: T): T

    val initValue: T
}

object Add {
    implicit val addForInt: Add[Int] = new Add[Int] {
        override def add(t1: Int, t2: Int): Int = t1 + t2
        override val initValue: Int = 0
    }
}