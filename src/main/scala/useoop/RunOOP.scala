package useoop

import org.bson.Document
import org.slf4j.{Logger, LoggerFactory}


object RunOOP {

    def logger(): Logger = {
        LoggerFactory.getLogger("run-oop")
    }

    def main(args: Array[String]): Unit = {
        val client: MyMongoClient = new MyMongoClient()
        val myDB: MyDB = client.getDB("phuvh")

        val collection1 = myDB.updatableCollection("collection_1")

        collection1 + new Document()


        for (s: String <- client.getDB("mydb").collectionNames()) {
            println(s)
        }
        //MyDate
    }
}
