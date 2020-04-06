package learn_fp_in_scala.chap7

import java.io.{BufferedReader, File, FileReader}
import java.util
import java.util.concurrent.{ExecutorService, Executors}
import java.util.stream.Collectors

import com.google.gson.Gson
import learn_fp_in_scala.chap7.Par._
import org.apache.commons.io.FileUtils
import useoop.MLogger

import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.jdk.javaapi.CollectionConverters


object FilterTextInFolder {
    implicit val executor: ExecutorService = Executors.newFixedThreadPool(10)

    private val uid = ""

    private val ACTION = "JACK_POT_INGAME"

    private val p1 = ""

    //private val param1 = "108847121"


    def testListJava(): List[String] = {
        val jl = util.Arrays.asList("23", "56")
        CollectionConverters.asScala(jl).toList
    }


    case class UserWinJackpot(uid: Int, goldWin: Long, avatarUrl: String, displayName: String)


    // hợp 2 user lại thành 1
    def combine2User(u1: UserWinJackpot, u2: UserWinJackpot): UserWinJackpot = {
        val newGold = u1.goldWin + u2.goldWin
        u1.copy(goldWin = newGold)
    }


    def main(args: Array[String]): Unit = {


        val pathToFolder = "E:\\work_at_home\\log_process"
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
                val tmp = str.split("\\|\\|")
                val uid = tmp(1).split(";")(1).toInt
                val arrNameGoldUrl = tmp(2).split(";")
                val goldWin = arrNameGoldUrl(1).toLong
                val name = arrNameGoldUrl(0)
                val avatarUrl = arrNameGoldUrl(2)
                UserWinJackpot(uid, goldWin, avatarUrl, name)
            })
            .foldRight(HashMap[Int, UserWinJackpot]())((u, m) => {
                /*if (m.contains(u.uid)) {
                    val old = m.get(u.uid).head
                    val newUser = combine2User(old, u)
                    m.put(u.uid, newUser)
                } else {
                    m.put(u.uid, u)
                }
                m*/
                if (m.contains(u.uid)) {
                    val old = m.get(u.uid).head
                    val newUser = combine2User(old, u)
                    m.+((u.uid, newUser))

                } else {
                    m.+((u.uid, u))
                    //m2
                }
            })
            .values.toList
        val listJava = CollectionConverters.asJava(listFinalUser)
        val gson = new Gson()
        val strToFile = gson.toJson(listJava)
        val path = System.getProperty("user.dir") + "/fileUser.json"
        FileUtils.writeStringToFile(new File(path), strToFile, "utf-8", false)
        println("done")


    }
}
