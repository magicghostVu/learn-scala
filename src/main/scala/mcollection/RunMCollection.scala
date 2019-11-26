package mcollection

import useoop.MLogger

import scala.collection.mutable.ListBuffer

object RunMCollection {


    def useListBuffer(): Unit = {
        val listBuf: ListBuffer[String] = new ListBuffer[String]()
        listBuf.append("phuvh", "vint")
        listBuf(1) = "phuvh1"

        val mm: Map[Int, String] = Map(1 -> "phuvh", 2 -> "vint")


    }

    def main(args: Array[String]): Unit = {
        val sep: Seq[String] = Seq[String]("Java", "Scala", "Python")

        val pFun: PartialFunction[Int, String] = sep

        val g: PartialFunction[Int, String] = {
            case i: Int => "phuvh"
        }

        val pp = pFun.orElse(g)

        MLogger.generalLogger.info("class is {}", sep)
    }
}
