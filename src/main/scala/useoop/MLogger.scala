package useoop

import org.slf4j.{Logger, LoggerFactory}


object MLogger {
    private def logger(name: String): Logger = {
        LoggerFactory.getLogger(name)
    }

    val generalLogger: Logger = logger("general-logger")


}
