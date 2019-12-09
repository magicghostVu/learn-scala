package functional.use_monad

trait MState[S, +A] {
    def apply(s: S): (S, A)
}

object MState {
    def state[S, A](f: S => (S, A)): MState[S, A] = {
        new MState[S, A] {
            override def apply(s: S): (S, A) = f(s)
        }
    }

    def init[S]: MState[S, S] = state[S, S](s => (s, s))

    def modify[S](f: S => S) = {
        // todo: impl
        init[S].flatMap
    }

}