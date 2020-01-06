package functional.use_monad


// trong quá trình biến đổi thì MState sẽ được tạo mới nhiều lần,
// cái hàm apply của nó sẽ dồn logic của các bước biến đổi lại đẻ thực hiện sau cùng
trait MState[S, +A] {

    import MState._

    // hàm này là trừu tượng, nó sẽ thực hiện biến đổi object đầu vào thành một object mới
    // s là khởi nguyên, và kết quả trả về S (trong (S,A)) sẽ là thằng đã được biến đổi
    def apply(s: S): (S, A)


    // từ trạng thái hiện tại chuyển sang 1 trạng thái mới
    def map[B](f: A => B): MState[S, B] = {
        val u = apply _
        val r = u.andThen(psa => {
            (psa._1, f(psa._2))
        })
        state(r)
    }

    // từ trang thái hiện tại, chuyển sang một MState với trạng thái mới
    def flatMap[B](f: A => MState[S, B]): MState[S, B] = {
        val tmpF: S => (S, A) = apply
        val t2 = tmpF.andThen(psa => {
            f(psa._2).apply(psa._1)
        })
        state(t2)
    }


}

object MState {

    // chỉ là để khởi tạo một state mới với một hàm đầu vào
    private def state[S, A](f: S => (S, A)): MState[S, A] = {
        new MState[S, A] {
            override def apply(s: S): (S, A) = f(s)
        }
    }

    // truyền luôn hàm đầu vào là s-> (s,s)
    private def init[S](): MState[S, S] = state[S, S](s => (s, s))

    def modify[S](fModified: S => S): MState[S, Unit] = {
        val newApply: S => (S, Unit) = s => (fModified(s), ())
        init[S]().flatMap(s => {
            state(newApply)
        })
    }

    def gets[S, A](f: S => A): MState[S, A] = {
        init[S]().flatMap(s => {
            state(_ => (s, f(s)))
        })
    }

}

