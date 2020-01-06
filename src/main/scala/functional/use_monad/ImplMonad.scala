package functional.use_monad

import useoop.MLogger

object ImplMonad {

    trait MyMonad[C[_]] {
        def unit[A](a: A): C[A]

        def flatMap[A, B](ca: C[A])(f: A => C[B]): C[B]

        def map[A, B](ca: C[A])(f: A => B): C[B] = {
            flatMap(ca)(a => {
                val b: B = f(a)
                unit(b)
            })
        }
    }


    case class State[S, +A](functionApply: S => (A, S)) {
        def map[B](fmap: A => B): State[S, B] = {
            val newFunctionApply = functionApply.andThen(as => {
                val newS = as._2
                val b = fmap(as._1)
                (b, newS)
            })
            State(newFunctionApply)
        }

        def flatMap[B](fFlatMap: A => State[S, B]): State[S, B] = {
            val newApply = functionApply.andThen(as => {
                val newS = as._2
                val sb = fFlatMap(as._1)
                sb.functionApply(newS)
            })
            State(newApply)
        }

        def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] = {
            flatMap(a => sb.map(b => {
                val c = f(a, b)
                c
            }))
        }
    }


    object State {
        def init[S](): State[S, S] = {
            State[S, S](s => (s, s))
        }

        def modify[S](fUpdate: S => S): State[S, Unit] = {
            val f: S => (Unit, S) = s => ((), fUpdate(s))
            init[S]().flatMap(s => {
                State[S, Unit](f)
            })
        }
    }


    case class MyState(value: Int)

    def plusOne(myState: MyState): MyState = myState.copy(myState.value + 1)

    def double2(myState: MyState): MyState = myState.copy(myState.value * 2)

    //def

    def main(args: Array[String]): Unit = {
        val p1 = State.modify(plusOne)
        val p2 = State.modify(double2)

        val p3 = for {
            _ <- p1
            _ <- p2
        } yield ()


        val r = p3.functionApply(MyState(3))

        MLogger.generalLogger.debug("r is {}", r._2)
    }

}
