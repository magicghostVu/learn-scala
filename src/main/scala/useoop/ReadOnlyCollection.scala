package useoop

import java.util.function.Consumer

import com.mongodb.client.{MongoCollection, MongoIterable}
import org.bson.Document

import scala.collection.mutable

trait ReadOnlyCollection {
    val internal: MongoCollection[Document] // đây là một abstract member

    def find(filterData: Document): mutable.Set[Document] = {
        val raw: MongoIterable[Document] = internal.find(filterData)
        val mSet: mutable.Set[Document] = new mutable.HashSet[Document]()
        val c: Consumer[Document] = d => {
            mSet += d
        }
        raw.forEach(c)
        mSet
    }


    def findOne(filterData: Document): Document = internal find filterData first

    def count(filterData: Document): Long = internal countDocuments filterData
}

trait AdministratorCollection extends ReadOnlyCollection {
    def drop: Unit = internal drop()

    def dropIndexes: Unit = internal dropIndexes()
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

// khi một class kế thừa 1 trait, nó bắt buộc phải override cái abstract member, (có mặt trong primary constructor)
class MyCollection(override val internal: MongoCollection[Document]) extends ReadOnlyCollection

