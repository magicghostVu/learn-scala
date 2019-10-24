package useoop

import com.mongodb.client.{FindIterable, MongoCollection}
import org.bson.Document

trait ReadOnlyCollection {
    val internal: MongoCollection[Document] // đây là một abstract member

    def find(filterData: Document): FindIterable[Document] = internal find filterData

    def findOne(filterData: Document): Document = internal find filterData first

    def count(filterData: Document): Long = internal countDocuments filterData
}

trait AdministratorCollection extends ReadOnlyCollection {
    def drop: Unit = internal drop

    def dropIndexes: Unit = internal dropIndexes
}

trait UpdatableCollection extends ReadOnlyCollection {
    //thêm một document mới
    def +(data: Document): Unit = internal insertOne data

    // xoá một document đi
    def -(dataFilter: Document): Unit = internal deleteOne dataFilter

    //hàm update
    def !(data: Document): Unit = {
        val cmd = new Document("$set", data)
        internal.updateOne(data, cmd)
    }
}

class MyCollection(override val internal: MongoCollection[Document]) extends ReadOnlyCollection

