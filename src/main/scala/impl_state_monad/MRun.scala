package impl_state_monad

import useoop.MLogger

import scala.util.Random

object MRun {


    trait RNG {
        def nextInt: (Int, RNG)

        def nextLong: (Long, RNG)
    }


    case class MyRandom(seed: Long) extends RNG {
        def nextInt(): (Int, MyRandom) = {
            val newSeed = seed + 1L
            val random: Random = new Random(seed)
            val r = random.nextInt()
            (r, MyRandom(newSeed))
        }

        override def nextLong: (Long, RNG) = {
            val newSeed = seed + 1L
            val random: Random = new Random(seed)
            val r = random.nextLong()
            (r, MyRandom(newSeed))
        }
    }


    type Rand[+A] = RNG => (A, RNG)


    // biến một giá trị thành một hàm, hàm này nhận vào 1 tham số là một RNG
    def unit[A](a: A): Rand[A] = {
        rng => (a, rng)
    }

    def map[A, B](randA: Rand[A])(f: A => B): Rand[B] = {
        rng: RNG => {
            val (aa, _) = randA(rng)
            val bb = f(aa)
            (bb, rng)
        }
    }

    def flatMap[A, B](randA: Rand[A])(f: A => Rand[B]): Rand[B] = {
        rng: RNG => {
            val (a, _) = randA(rng)
            val randB = f(a)
            val (b, _) = randB(rng)
            (b, rng)
        }
    }


    def main(args: Array[String]): Unit = {
        //val mr = MyRandom(10)

        //val ri: Rand[Int] = _.nextInt

        val m1 = unit(1)

        val m2 = map(m1)(i => i / 2)

        val pp = m2(MyRandom(1))

        MLogger.generalLogger.debug("pp is {}", pp)

    }


    def r1(): Unit = {
        val myRandom = MyRandom(18)
        val (i1, r1) = myRandom.nextInt()
        val (i2, r2) = r1.nextInt()
        MLogger.generalLogger.info("i1 {}, i2 {}", i1, i2)
    }


}
