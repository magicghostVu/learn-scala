package functional.type_class

import com.google.gson.Gson
import useoop.MLogger

object RunTypeClass {


    // hàm này có tham số jsonConverter được để là ngầm định để có thế được truyền một cách "dynamic" theo ngữ cảnh
    // chương trình
    def toJson[A](a: A)(implicit jsonConverter: JsonConverter[A]): String = {
        jsonConverter.toJson(a)
    }

    implicit class mm[A](a: A) {
        def toJson(implicit jsonConverter: JsonConverter[A]): String = {
            jsonConverter.toJson(a)
        }
    }


    object Converters {
        val gson: Gson = new Gson()

        implicit object JsonMovieConverter extends JsonConverter[Movie] {
            override def toJson(a: Movie): String = {
                gson.toJson(a)
            }
        }



    }

    /*def toJsonMovie(movie: Movie): String = {
        import Converters._
        movie.toJson
    }*/


    def main(args: Array[String]): Unit = {
        import Converters._
        val m = Movie("twelve monkey", "12/12/1995")
        val stringJson = m.toJson
        MLogger.generalLogger.debug("json is {}", stringJson)
        val h = m.getClass
        MLogger.generalLogger.debug("class is {}", h)
    }
}
