package functional.use_monad

import useoop.MLogger

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

    type Ran[+A] = Rnd => (A, Rnd)

    val int: Ran[Int] = r => r.nextInt()


    def main(args: Array[String]): Unit = {
        SimpleRnd(1).nextInt() match {
            case (a, b) => {
                MLogger.generalLogger.debug("a is {}", a)
            }
            case _ => {
            }
        }

    }

}
