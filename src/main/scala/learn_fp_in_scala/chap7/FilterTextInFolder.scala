package learn_fp_in_scala.chap7

import java.io.{BufferedReader, File, FileReader}
import java.util
import java.util.concurrent.{ExecutorService, Executors}
import java.util.stream.Collectors

import learn_fp_in_scala.chap7.Par._
import useoop.MLogger

import scala.jdk.javaapi.CollectionConverters


object FilterTextInFolder {
    implicit val executor: ExecutorService = Executors.newFixedThreadPool(10)

    private val uid = "Payment Update Param: "

    private val ACTION = ""

    private val p1 = ""

    //private val param1 = "108847121"


    def testListJava(): List[String] = {
        val jl = util.Arrays.asList("23", "56")
        CollectionConverters.asScala(jl).toList
    }


    def main(args: Array[String]): Unit = {
        val pathToFolder = "C:\\Users\\phuvh\\Desktop\\payment_log_04_01"
        val folderAllFile = new File(pathToFolder)
        val allChildren = folderAllFile.listFiles()
        val fListString = Par.parMap(allChildren.toList)(file => {
            MLogger.generalLogger.debug("file is {}", file.getName)
            val fr = new FileReader(file)
            val bReader = new BufferedReader(fr)
            val stream = bReader.lines()
            val listJava = stream
                .filter(str => {
                    str.contains(uid) && str.contains(ACTION) && str.contains(p1)
                })
                .collect(Collectors.toList())
            CollectionConverters.asScala(listJava).toList
        })
        val listFinal = Par.map(fListString)(_.flatten)
        val start = System.currentTimeMillis()
        val res = listFinal.run().get()





        val end = System.currentTimeMillis()
        MLogger.generalLogger.debug("time is {}, size is {}", end - start, res.size)

        res.foreach(println(_))

    }
}
