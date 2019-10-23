package useoop

import java.util

import com.mongodb.client.{MongoDatabase => DBOrigin}

import scala.collection.convert.Wrappers.JSetWrapper
import scala.collection.mutable // sử dụng alias import

class MyDB private(val db: DBOrigin) {
    def collectionNames(): mutable.Set[String] = {
        val setJava = new util.HashSet[String]()
        db.listCollectionNames().into(setJava)
        for (name <- JSetWrapper(setJava)) yield name
    }
}

object MyDB {
    def apply(db: DBOrigin): MyDB = {
        new MyDB(db)
    }
}
