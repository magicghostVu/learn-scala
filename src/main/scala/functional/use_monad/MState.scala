package functional.use_monad

trait MState[S, +A] {
    def apply(s: S): (S, A)

    def map[B](fmap: A => B): MState[S, B] = {
        val newApp = (apply _).andThen(tsa => {
            (tsa._1, fmap(tsa._2))
        })
        //newApp
        new MState[S, B] {
            override def apply(s: S): (S, B) = newApp(s)
        }
    }

    def flatMap[B](fFlatMap: A => MState[S, B]): MState[S, B] = {
        val newApp = (apply _).andThen(tsa => {
            fFlatMap(tsa._2)(tsa._1)
        })
        new MState[S, B] {
            override def apply(s: S): (S, B) = newApp(s)
        }
    }

}

object MState {
    def state[S, A](f: S => (S, A)): MState[S, A] = {
        new MState[S, A] {
            override def apply(s: S): (S, A) = f(s)
        }
    }

    def init[S]: MState[S, S] = state[S, S](s => (s, s))

    def modify[S](fModified: S => S): MState[S, Unit] = {
        val newApp: S => (S, Unit) = s => (fModified(s), ())
        init[S].flatMap(s => {
            state(newApp)
        })
    }

}