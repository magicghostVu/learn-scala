package useoop

import org.bson.Document

trait UpdatableAndMemoizer extends UpdatableCollection with MemoizerCollection {

    override def -(dataFilter: Document): Unit = {
        UpdatableAndMemoizer.super.-(dataFilter)
        val name = Option(dataFilter.getString("name"))
        if (name.isDefined) {
            mapCached.remove(name.get)
        }

    }

    def printSuper = {
        var c = super.getClass
        MLogger.generalLogger.info("super class is {}", super.getClass)
    }

    /*override def tryFun(): Unit = {
        MLogger.generalLogger.info("try func from UpdatableAndMemoizer")
    }*/
}




