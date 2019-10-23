package patternmatching

object Run {

    def tryMatchingType(o: Any): Unit = {
        o match {
            case s: String => println("o is String")
            case l: List[_] => println("o is list")
            case _ => println("o is " + o.getClass)
        }
    }

    def try2(): Unit = {

        val suffixes = List("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
        val args: List[Int] = List(0, 1, 2, 3, 4)

        println(ordinal(args(0)))

        //chỗ này có sử dụng pattern matching with name
        def ordinal(number: Int) = number match {
            case tenTo20 if 10 to 20 contains tenTo20 => number + "th"
            case rest => rest + suffixes(number % 10)
        }
    }

    def userPatternMatchingWithName(i: Int): Unit = {
        i match {
            case in10 if in10 < 10 => println("nho hon 10")
            case _ => println("xong cmnr")
        }
    }

    def main(args: Array[String]): Unit = {
        /*tryMatchingType(List("phuch", "vint"))
        tryMatchingType("phuvh")
        tryMatchingType(new Object)
        tryMatchingType(println(args))*/

        userPatternMatchingWithName(10)

        try {
            throw new Exception
        } catch {
            case e: IllegalArgumentException => {
                println("Illegal")
            }
            case e: Exception => {
                println(e)
            }
        }

    }
}
