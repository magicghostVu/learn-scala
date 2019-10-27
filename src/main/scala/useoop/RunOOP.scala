package useoop


import org.bson.Document
import org.slf4j.{Logger, LoggerFactory}


object RunOOP {

    def logger(): Logger = {
        LoggerFactory.getLogger("run-oop")
    }

    def main(args: Array[String]): Unit = {
        val client: MyMongoClient = new MyMongoClient()
        val myDB: MyDB = client.getDB("learn-scala")


        val y = myDB.updatableWithMemoizer_2("oop")

        y.func

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
