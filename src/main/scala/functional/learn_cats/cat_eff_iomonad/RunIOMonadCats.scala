package functional.learn_cats.cat_eff_iomonad


import cats.effect._
import useoop.MLogger




object RunIOMonadCats {

    def printLineEff(valPrint: String): IO[Unit] = {
        IO[Unit] {
            MLogger.generalLogger.debug("eff {}", valPrint)
        }
    }

    def main(args: Array[String]): Unit = {
        val io: IO[Unit] = printLineEff("phuvh")


        val f0: () => Int = () => 1

        val ioo = IO(f0)

        

        //MLogger.generalLogger.info("Done, {}", io)
        //io.unsafeRunSync()
    }
}
