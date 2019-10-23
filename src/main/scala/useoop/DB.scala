package useoop

import com.mongodb.client.{MongoDatabase => DBOrigin} // sử dụng alias import

class MyDB(val db: DBOrigin) {

}

object DB {
    def apply(db: DBOrigin): MyDB = {
        new MyDB(db)
    }
}
