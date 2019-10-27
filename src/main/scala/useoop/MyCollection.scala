package useoop

import com.mongodb.client.MongoCollection
import org.bson.Document

// khi một class kế thừa 1 trait, nó bắt buộc phải override cái abstract member, (có mặt trong primary constructor)
class MyCollection (override val internal: MongoCollection[Document]) extends ReadOnlyCollection {

}
