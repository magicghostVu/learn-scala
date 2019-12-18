package higher_kined_type

trait MFoldable[F[_]] {
    def foldLeft[A](coll: F[A], m: Summable[A]): A
}
