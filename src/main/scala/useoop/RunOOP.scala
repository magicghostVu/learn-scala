package useoop


import com.mongodb.Block
import com.mongodb.client.FindIterable
import org.bson.Document
import org.slf4j.{Logger, LoggerFactory}


object RunOOP {

    def logger(): Logger = {
        LoggerFactory.getLogger("run-oop")
    }

    def main(args: Array[String]): Unit = {
        val client: MyMongoClient = new MyMongoClient()
        val myDB: MyDB = client.getDB("learn_scala")

        val readOnlyCollection: MyCollection = myDB.readOnlyCollection("oop")

        //val numRecord: Long = collection.count(new Document())


        val filterData = new Document("name", "phuvh")

        val resultFind = readOnlyCollection.find(filterData)


        for (d: Document <- resultFind) {
            logger().info("d is {}", d)
        }


       /* val updatableCollection = myDB.updatableCollection("oop")

        val newD = new Document()
        newD.put("name", "vint")
        newD.put("age", 24)

        updatableCollection + newD*/


    }
}
