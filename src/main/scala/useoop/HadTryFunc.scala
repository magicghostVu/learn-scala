package useoop

trait HadTryFunc extends ReadOnlyCollection {
    def tryFun = {
        MLogger.generalLogger.info("tryFun from HadTryFunc")
    }
}
