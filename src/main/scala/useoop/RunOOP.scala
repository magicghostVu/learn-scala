package useoop


import java.io.File

import org.apache.commons.io.FileUtils
import org.bson.Document
import org.json.{JSONArray, JSONObject}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.convert.Wrappers.JIterableWrapper


object RunOOP {

    def logger(): Logger = {
        LoggerFactory.getLogger("run-oop")
    }


    def insertData(myDB: MyDB): Unit = {
        val coll: MyCollection with UpdatableCollection = myDB.updatableCollection("all_city_vn")
        val start = System.currentTimeMillis()
        val path: String = System.getProperty("user.dir") + "/datacity/cities.json"
        val fileData: File = new File(path)
        val jsonStr: String = FileUtils.readFileToString(fileData, "utf-8")
        val allCitiesJson: JSONArray = new JSONArray(jsonStr)
        val iter: Iterable[Object] = JIterableWrapper(allCitiesJson)
        for (cityJson <- iter) {
            val str: String = cityJson.toString
            val d = Document.parse(str)
            coll + d
        }
        val end = System.currentTimeMillis()
        logger().info("insert done in {}", end - start)
    }


    def main(args: Array[String]): Unit = {
        val client: MyMongoClient = new MyMongoClient()
        val myDB: MyDB = client.getDB("learn_scala")

        insertData(myDB)

        /*val y = myDB.updatableWithMemoizer_2("oop")

        y.func*/

        /* val collection: MyCollection with UpdatableAndMemoizer = myDB.updatableWithMemoizer("oop")
         /*val coll2 = myDB.updatableWithMemoizer_2("oop")*/

         var filterData = new Document("name", "phuvh")

         val r1 = collection.findOneByName(filterData)

         var r2 = collection.findOneByName(filterData)

         collection - filterData

         val r3 = collection.findOneByName(filterData)

         logger().info("r3 is {}", r3)*/

        /*val lin: TestClassLinear = myDB.getTestLinear("learn-scala")


        lin.tryFun()*/
        //collection - r1


    }
}
