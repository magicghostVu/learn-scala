package useoop

import org.bson.Document
import org.slf4j.{Logger, LoggerFactory}


object RunOOP {

    def logger(): Logger = {
        LoggerFactory.getLogger("run-oop")
    }

    def main(args: Array[String]): Unit = {
        val client: MyMongoClient = new MyMongoClient("192.168.1.197", 27017)
        val myDB: MyDB = client.getDB("learn_scala")

        /* val collection1 = myDB.updatableCollection("collection_1")

         collection1 + new Document()*/


        val collection2 = myDB.memoizerCollection("oop")


        for (s: String <- myDB.collectionNames()) {
            logger().info("collection name {}", s)
        }
        //MyDate
    }
}
