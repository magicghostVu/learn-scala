package useoop

import com.mongodb.client.{MongoCollection => Collection, MongoDatabase => DBOrigin}
import org.bson.Document

import scala.collection.convert.Wrappers.JIterableWrapper

class MyDB private(val db: DBOrigin) {
    def collectionNames(): Iterable[String] = {
        val allName: JIterableWrapper[String] = JIterableWrapper(db.listCollectionNames())
        for (name <- allName) yield name
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

    def memoizerCollection(name: String): Unit = {
        new MyCollection(collection(name)) with Memoizer
    }
}

object MyDB {
    def apply(db: DBOrigin): MyDB = {
        new MyDB(db)
    }
}
