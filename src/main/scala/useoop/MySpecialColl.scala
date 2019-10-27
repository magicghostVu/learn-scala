package useoop

import com.mongodb.client.MongoCollection
import org.bson.Document

class MySpecialColl(override val internal: MongoCollection[Document]) extends MyCollection(internal) with UpdatableAndMemoizer {
    def func = {
        MLogger.generalLogger.info("super is {}", super.getClass)
    }
}
