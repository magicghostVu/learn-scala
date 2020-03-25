package learn_fp_in_scala.chap7


// một container cho việc tính toán song song
class Par[A] {

}

object Par {
    // call by name
    // nhập vào một giá trị và trả về một container
    def unit[A](a: => A): Par[A] = ???


    // thực hiện tính toán và trả ra giá trị
    def get[A](pa: Par[A]): A = ???


    def map2[A, B, C](pa: Par[A], pb: Par[B])(functionMap: (A, B) => C): Par[C] = ???

}

