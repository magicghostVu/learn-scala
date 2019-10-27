package useoop

import java.util.function.Consumer

import com.mongodb.client.{MongoCollection, MongoIterable}
import org.bson.Document


// trong scala thì trait cũng giống như abstract class tuy nhiên nó có một số đặc điểm khác
// nó không có constructor, bên trong tất cả các thuộc tính là abstract members
// ở các lớp con các member này phải được override trong constructor
// có thể kế thừa nhiều trait, không thể kế thừa nhiều class -> chưa hiểu chỗ giải quyết xung đột kim cương như nào
// vì các phương thức trong trait là các phương thức có thể không phải trừu tượng

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

trait Memoizer extends ReadOnlyCollection {
    val mapCached: mutable.Map[String, Document] = mutable.Map[String, Document]()

    def findOneByName(filterData: Document): Document = {
        if (!filterData.containsKey("name")) {
            findOne(filterData)
        }
        val name: String = filterData.getString("name")
        mapCached.getOrElseUpdate(name, findOne(filterData))
    }
}



// khi một class kế thừa 1 trait, nó bắt buộc phải override cái abstract member, (có mặt trong primary constructor)
class MyCollection(override val internal: MongoCollection[Document]) extends ReadOnlyCollection

