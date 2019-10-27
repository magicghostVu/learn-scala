package useoop

import org.bson.Document

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

    def tryFun(): Unit = {
        MLogger.generalLogger.info("log from UpdatableCollection")
    }

}