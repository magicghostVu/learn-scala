package useoop.caseclass

import org.bson.Document
import useoop.MLogger
import useoop.morequeryopt.Query


object RunTest {
    def main(args: Array[String]): Unit = {
        /*val s: Student = Student(1, "phuvh", 24, "K58T")
        val s2: Student = s.copy(name = "vint")

        MLogger.generalLogger.info("s code {}, s2 code {}", s.hashCode, s2.hashCode)


        s2 match {
            // hàm unapply đã được ngầm gọi ở đây, nó extract tất cả các tham số ban đầu ra
            case Student(id, name, age, className) => {
                /*val msg = "is %s, name %s, age %s, className %s".format(id, name, age, className)
                MLogger.generalLogger.info(msg)*/


                MLogger.generalLogger.info("id {}, name {}, age {}, className {}",
                    Array[AnyRef](id: Integer, name: String, age: Integer, className: String): _*)
            }
        }*/

        forComprehensionWithPatternMatching

        //tryCaseQuery
    }


    def forComprehensionWithPatternMatching = {

        val s: Student = Student(1, "phuvh", 24, "K58T-uet")
        val s2: Student = s.copy(name = "vint", className = "k54-ftu")

        val s3 = s.copy(name = "quyvv", age = 20, className = "hnue")

        val lp = List[Student](s, s2, s3)

        val li: List[Int] = for (Student(id, name, age, className) <- lp; val sum = id + age) yield id


        MLogger.generalLogger.info("li is {}", li)

    }

    def tryCaseQuery = {

        /*val rangeQ = new Document("name", "phuvh")

        val q1= Query(rangeQ).limit(10).sort(rangeQ)

        println()*/

    }
}
