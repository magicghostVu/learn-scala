package functional.type_params

import useoop.MLogger

object TestCallByName {
    var enableLog: Boolean = true
    val log: String => Unit = str => {
        if (enableLog) {
            MLogger.generalLogger.info("msg is {}", str)
        }
    }

    val logWithFunctionParam: (() => String) => Unit = f0 => {
        if (enableLog) {
            MLogger.generalLogger.info("msg is {}", f0())
        }
    }


    def logWithFunctionParamDef(a: => String): Unit = {
        if (enableLog) {
            MLogger.generalLogger.info("msg is {}", a)
        }
    }


    def main(args: Array[String]): Unit = {
        val fStr: () => String = () => {
            MLogger.generalLogger.info("call fStr")
            "phuvh"
        }
        enableLog = false

        def returnStr(): String = {
            MLogger.generalLogger.info("call returnStr")
            "Phuvh"
        }

        //log(fStr())

        logWithFunctionParam(returnStr)
    }
}
