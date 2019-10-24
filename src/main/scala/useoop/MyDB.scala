package useoop

import java.util

import com.mongodb.client.{MongoCollection => Collection, MongoDatabase => DBOrigin}
import org.bson.Document

import scala.collection.convert.Wrappers.JSetWrapper
import scala.collection.mutable

class MyDB private(val db: DBOrigin) {
    def collectionNames(): mutable.Set[String] = {
        val setJava = new util.HashSet[String]()
        db.listCollectionNames().into(setJava)
        for (name <- JSetWrapper(setJava)) yield name
    }

    def collection(name: String): Collection[Document] = {
        db.getCollection(name)
    }


    def readOnlyCollection(name: String): MyCollection = {
        new MyCollection(collection(name))
    }

    def updatableCollection(name: String): MyCollection with UpdatableCollection = {
        new MyCollection(collection(name)) with UpdatableCollection
    }

    def administrableCollection(name: String): MyCollection with AdministratorCollection = {
        new MyCollection(collection(name)) with AdministratorCollection
    }
}

object MyDB {
    def apply(db: DBOrigin): MyDB = {
        new MyDB(db)
    }
}
