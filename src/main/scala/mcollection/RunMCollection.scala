package mcollection

import useoop.MLogger

object RunMCollection {
    def main(args: Array[String]): Unit = {
        val sep: Seq[String] = Seq[String]("Java", "Scala", "Python");
        MLogger.generalLogger.info("class is {}", sep)
    }
}
