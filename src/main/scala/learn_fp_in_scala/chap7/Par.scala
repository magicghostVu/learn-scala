package learn_fp_in_scala.chap7


// một container cho việc tính toán song song
// thực ra nó không phải là một container mà đó là 
class Par[A] {

}

object Par {
    // call by name
    // nhập vào một giá trị và trả về một container
    def unit[A](a: => A): Par[A] = ???


    // thực hiện tính toán và trả ra giá trị
    def run[A](pa: Par[A]): A = ???


    // thực ra hàm này có vai trò như hàm combine, chứ nó không phải là map
    def map2[A, B, C](pa: Par[A], pb: Par[B])(functionMap: (A, B) => C): Par[C] = ???


    // trả về một folk cho par A nếu như có một par A hoặc là một hàm trả ra par A
    // ngụ ý rằng việc evaluate cái kết quả trả ra của hàm này sẽ được chạy trên luồng khác
    def folk[A](a: => Par[A]): Par[A] = ???


    def lazyUnit[A](a: A): Par[A] = {
        folk[A](unit[A](a))
    }

}

