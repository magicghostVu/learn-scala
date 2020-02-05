package functional.learn_cats

import useoop.MLogger

object MApp {

    def main(args: Array[String]): Unit = {

        val mapp = Map("1" -> 1, "2" -> 2, "3" -> 3)

        val ll = List("1", "4", "3")

        val oo: List[Int] = for {
            k <- ll
            v <- mapp.get(k).orElse(None)
            //v <- Option(5)
        } yield v

        MLogger.generalLogger.debug("oo is {}", oo)


    }
}
