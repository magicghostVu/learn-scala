package functional.use_monad

import scala.util.Random


trait Rnd {
    def nextInt(): (Int, Rnd)
}

case class SimpleRnd(seed: Long) extends Rnd {
    override def nextInt(): (Int, Rnd) = {
        val random = new Random(seed)
        val r = random.nextInt()
        val newSeed = seed + 1
        (r, SimpleRnd(newSeed))
    }
}


object RandomPurely {

    // thực chất thì type này là một function1 từ Rnd => (A,Rnd)
    type Ran[+A] = Rnd => (A, Rnd)

    val int: Ran[Int] = r => r.nextInt()


    // trả ra một function (Ran[A]) từ một đầu vào là A
    def unit[A](a: A): Ran[A] = {
        rng => (a, rng)
    }

    def map[A, B](ran: Ran[A])(fMap: A => B): Ran[B] = {
        /*rnd => {
            val (a, rnd2) = ran(rnd)
            val b = fMap(a)
            (b, rnd2)
        }*/

        /*ran.andThen(as => {
            val b = fMap(as._1)
            val rb = unit(b)
            rb
        })*/
        // hoàn toàn có thể cài đặt map thông qua flatMap

        flatMap(ran)(a => {
            val b = fMap(a)
            unit(b)
        })
    }

    def flatMap[A, B](ran: Ran[A])(fFlatMap: A => Ran[B]): Ran[B] = {
        rnd: Rnd => {
            val (a, rnd2) = ran(rnd)
            val res = fFlatMap(a)
            res.apply(rnd2)
        }
    }


    def main(args: Array[String]): Unit = {

        val sr = SimpleRnd(10)
        int(sr)
    }

}
