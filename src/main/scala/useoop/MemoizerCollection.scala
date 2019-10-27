package useoop

import org.bson.Document

import scala.collection.mutable

trait MemoizerCollection extends ReadOnlyCollection {

    val mapCached: mutable.Map[String, Document] = mutable.Map[String, Document]()

    def findOneByName(filterData: Document): Document = {
        if (!filterData.containsKey("name")) {
            MLogger.generalLogger.info("filter data not contain name, so find in db")
            super.findOne(filterData)
        }
        val name: String = filterData.getString("name")
        if (mapCached.contains(name)) {
            MLogger.generalLogger.info("use cached for name {} so return now", name)
            return mapCached(name)
        }
        MLogger.generalLogger.info("cached not contain data so get from db")
        val d: Document = findOne(filterData)
        val o: Option[Document] = Option(d)
        if (o.isEmpty) {
            return null
        }
        mapCached.put(name, d)
        d
    }

    /*def -(d: Document): Unit = {

    }*/

}
