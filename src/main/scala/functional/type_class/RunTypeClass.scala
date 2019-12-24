package functional.type_class

import com.google.gson.Gson
import useoop.MLogger

object RunTypeClass {
    def toJson[A](a: A)(implicit jsonConverter: JsonConverter[A]): String = {
        jsonConverter.toJson(a)
    }


    object Converters {
        val gson: Gson = new Gson()

        implicit object JsonMovieConverter extends JsonConverter[Movie] {
            override def toJson(a: Movie): String = {
                gson.toJson(a)
            }
        }

    }

    // MovieConverter


    def main(args: Array[String]): Unit = {
        import Converters._
        val m = Movie("twelve monkey", "12/12/1995")
        val stringJson = toJson(m)
        MLogger.generalLogger.debug("json is {}", stringJson)

        val h = m.getClass

        MLogger.generalLogger.debug("class is {}", h)


    }
}
