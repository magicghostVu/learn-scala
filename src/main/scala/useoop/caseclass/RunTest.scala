package useoop.caseclass

import org.bson.Document
import useoop.MLogger
import useoop.morequeryopt.Query


object RunTest {
    def main(args: Array[String]): Unit = {
        val s: Student = Student(1, "phuvh", 24, "K58T")
        val s2: Student = s.copy(name = "vint")

        MLogger.generalLogger.info("s code {}, s2 code {}", s.hashCode, s2.hashCode)


        s2 match {
            case Student(id, name, age, className) => {

                val msg = "is %s, name %s, age %s, className %s".format(id, name, age, className)
                MLogger.generalLogger.info(msg)
            }
        }

        //tryCaseQuery
    }

    def tryCaseQuery = {

        /*val rangeQ = new Document("name", "phuvh")

        val q1= Query(rangeQ).limit(10).sort(rangeQ)

        println()*/

    }
}
