package learn_fp_in_scala.chap7

import java.io.{BufferedReader, File, FileReader}
import java.util
import java.util.concurrent.{ExecutorService, Executors}
import java.util.stream.Collectors

import com.google.gson.GsonBuilder
import learn_fp_in_scala.chap7.Par._
import org.apache.commons.io.FileUtils
import useoop.MLogger

import scala.io.StdIn
import scala.jdk.javaapi.CollectionConverters


object FilterTextInFolder {
    implicit val executor: ExecutorService = Executors.newFixedThreadPool(10)

    private val uid = "108681643"

    private val ACTION = ""

    private val p1 = ""

    //private val param1 = "108847121"


    def testListJava(): Unit = {


        //val y = StdIn.readInt()


        val l = (0 until 10)
            .foldLeft(List[Int]())((a, b) => {
                a.appended(1)
            })
            .map(_.toString)

       l.reverse

    }


    case class UserWinJackpot(uid: Int, goldWin: Long, avatarUrl: String, displayName: String)


    // hợp 2 user lại thành 1
    def combine2User(u1: UserWinJackpot, u2: UserWinJackpot): UserWinJackpot = {
        val newGold = u1.goldWin + u2.goldWin
        u1.copy(goldWin = newGold)
    }


    /*def testExmaple(): Unit = {
        object permissions {

            sealed trait Permissions { type T }
            case object Create { type T = Nothing }
            case object Edit { type T = Item }
            final case class Item (title: String)
            def doWork[T0] (permission: Permissions { type T = T0 } , v: T0) =
                permission match {
                    //case Edit => ??? // Edit#T == Item is your type here
                    case Edit => {

                    }
                }
        }
    }*/


    case class UserUpdateElo(time: String, score: Int)

    def main(args: Array[String]): Unit = {

        testListJava()
        /*val pathToFolder = "E:\\work_at_home\\log_process"
        val folderAllFile = new File(pathToFolder)
        val allChildren = folderAllFile.listFiles()
        val fListString = Par.parMap(allChildren.toList)(file => {
            //MLogger.generalLogger.debug("file is {}", file.getName)
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

        //
        //res.foreach(println(_))
        val listFinalUser = res
            .map(str => {

                val pp = str.split("  ")

                val time = pp(0)

                val uidNScore = pp(1).split(" - ")(1).split(" ")(2)

                UserUpdateElo(time, uidNScore.toInt)
            })

        //.values.toList
        val listJava = CollectionConverters.asJava(listFinalUser)
        val gson = new GsonBuilder().setPrettyPrinting().create()
        val strToFile = gson.toJson(listJava)
        val path = System.getProperty("user.dir") + "/fileUser.json"
        FileUtils.writeStringToFile(new File(path), strToFile, "utf-8", false)
        println("done")*/


    }
}
