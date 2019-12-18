package higher_kined_type

trait Summable[A] {

    def plus(a: A, b: A): A

    def init(): A
}
