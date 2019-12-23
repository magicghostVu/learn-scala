package higher_kined_type

trait Mapper[T[_]] {
    def map[A, B](originValue: T[A], fMap: A => B): T[B]
}

object ListMapper extends Mapper[List] {
    override def map[A, B](originValue: List[A], fMap: A => B): List[B] = {
        originValue.map(fMap)
    }
}


class EitherMapper[X] extends Mapper[({type E[A] = Either[X, A]})#E] {
    override def map[A, B](originValue: Either[X, A], fMap: A => B): Either[X, B] = {
        originValue match {
            case Left(x) => Left(x)
            case Right(a) => Right(fMap(a))
        }
    }
}
