package useoop

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase

// nếu không có var/val được khai báo trong constructor thì sau khi gọi hàm khởi tạo sẽ không truy cập trực tiếp các thuộc tính đó nữa
class MyMongoClient(val host: String, val port: Int) {
    def this() {
        this("localhost", 27017)
    }

    private val _internalClient: MongoClient = new MongoClient(host, port)

    private def internalClient(): MongoClient = _internalClient

    def getDB(name: String): MongoDatabase = {
        internalClient().getDatabase(name)
    }

    def dropDb(name: String): Unit = {
        internalClient().dropDatabase(name)
    }

    def createDb(name: String): MyDB = {
        MyDB(internalClient().getDatabase(name))
    }
}


