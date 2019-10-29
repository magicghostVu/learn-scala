package useoop

import java.util.function.Consumer

import com.mongodb.client.{FindIterable, MongoCollection}
import org.bson.Document
import useoop.morequeryopt._


// trong scala thì trait cũng giống như abstract class tuy nhiên nó có một số đặc điểm khác
// nó không có constructor, bên trong tất cả các thuộc tính là abstract members
// ở các lớp con các member này phải được override trong constructor
// có thể kế thừa nhiều trait, không thể kế thừa nhiều class -> chưa hiểu chỗ giải quyết xung đột kim cương như nào
// vì các phương thức trong trait là các phương thức có thể không phải trừu tượng
// scala có các khái niệm như class linearization hay stackable trait để giải quyết vấn đề này
import scala.collection.mutable

trait ReadOnlyCollection {
    val internal: MongoCollection[Document] // đây là một abstract member
    def find(filterData: Document): mutable.Set[Document] = {
        val raw: FindIterable[Document] = internal.find(filterData)
        //internal.find()

        val mSet: mutable.Set[Document] = new mutable.HashSet[Document]()
        val c: Consumer[Document] = d => {
            mSet += d
        }
        raw.forEach(c)
        mSet
        //Filters.eq("name", "phuvh")

    }

    def findOne(filterData: Document): Document = internal find filterData first

    def count(filterData: Document): Long = internal countDocuments filterData

    def findByQuery(query: Query): FindIterable[Document] = {
        def applyOpt(iterableData: FindIterable[Document], option: QueryOption): FindIterable[Document] = {
            option match {
                case NoOption => iterableData
                case SkipOption(number, other) => {
                    val newIter = iterableData.skip(number)
                    return applyOpt(newIter, other)
                }
                case LimitOption(maxSize, other) => {
                    val newIter = iterableData.limit(maxSize)
                    return applyOpt(newIter, other)
                }
            }
        }
        applyOpt(internal.find(query.dataQueryOrigin), query.queryOption)
    }

}

