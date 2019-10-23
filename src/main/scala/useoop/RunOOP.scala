package useoop

import java.util.concurrent.ThreadLocalRandom
import org.slf4j.{Logger, LoggerFactory}
import java.sql.{ Date => MyDate } // alias import


object RunOOP {

    def logger(): Logger = {
        LoggerFactory.getLogger("run-oop")
    }

    def main(args: Array[String]): Unit = {
        //val client: MyMongoClient = new MyMongoClient()
        val t: ThreadLocalRandom = ThreadLocalRandom.current
        val ir = t.nextInt
        logger().info("i random is {}", ir)

        val role = AbsRole("root")

        val canAccess: Boolean = role.canAccess("all")
        logger().info("can access is {}", canAccess)

        //MyDate
    }
}
