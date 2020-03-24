package functional.tagless_final

import useoop.MLogger

//ở đây hosting language là scala và hosted language là cái language mà ta đang xây dựng
trait Language[Wrapper[_]] {

    def number(v: Int): Wrapper[Int]

    def increment(numberAdd: Wrapper[Int]): Wrapper[Int]

    def add(a: Wrapper[Int], b: Wrapper[Int]): Wrapper[Int]

    def text(str: String): Wrapper[String]

    def toUpper(wrapperStr: Wrapper[String]): Wrapper[String]

    def concat(str1: Wrapper[String], str2: Wrapper[String]): Wrapper[String]

    def intToString(intWrapper: Wrapper[Int]): Wrapper[String]


}

trait ScalaToLanguageBridge[ScalaValue] {
    def apply[Wrapper[_]](implicit language: Language[Wrapper]): Wrapper[ScalaValue]
}


object RunTaglessFinal {


    def buildNumber(number: Int): ScalaToLanguageBridge[Int] = {
        new ScalaToLanguageBridge[Int] {
            override def apply[Wrapper[_]](implicit language: Language[Wrapper]): Wrapper[Int] = {
                language.number(number)
            }
        }
    }


    def buildIncrementNumber(number: Int): ScalaToLanguageBridge[Int] = {
        new ScalaToLanguageBridge[Int] {
            override def apply[Wrapper[_]](implicit language: Language[Wrapper]): Wrapper[Int] = {
                language.increment(language.number(number))
            }
        }
    }

    def buildText(originText: String): ScalaToLanguageBridge[String] = {
        new ScalaToLanguageBridge[String] {
            override def apply[Wrapper[_]](implicit language: Language[Wrapper]): Wrapper[String] = {
                language.text(originText)
            }
        }
    }

    def buildIncrementExpression(originExpression: ScalaToLanguageBridge[Int]): ScalaToLanguageBridge[Int] = {
        new ScalaToLanguageBridge[Int] {
            override def apply[Wrapper[_]](implicit language: Language[Wrapper]): Wrapper[Int] = {
                language.increment(originExpression.apply)
            }

        }
    }


    // giả sử ta cần build một biểu thức như sau
    // (a + 1) + b
    def buildComplexExpression(a: Int, b: Int): ScalaToLanguageBridge[String] = {

        new ScalaToLanguageBridge[String] {
            override def apply[Wrapper[_]](implicit language: Language[Wrapper]): Wrapper[String] = {
                val increA = language.increment(language.number(a))
                val aPlusB = language.add(increA, language.number(b))
                val strResult = language.intToString(aPlusB)
                val strToShow = "result is "
                language.concat(language.text(strToShow), strResult)
            }
        }

    }


    // ở đây interpreter1 sẽ eval biểu thức theo logic thông thường và đưa ra kết quả cuối cùng theo plain logic (1 + 2 = 3)
    // ta hoàn toàn có thể định nghĩa/cài đặt một interpreter khác để đưa ra một cài dặt khác
    // đưa ra String của biểu thức cuối cùng (1+2 = "1 + 2") nhưng vẫn type-safe

    object Interpreter1 {

        // dùng kiểu dữ liệu ID
        type NoWrap[ScalaVal] = ScalaVal

        implicit val myIntepre1 = new Language[NoWrap] {

            override def number(v: Int): Int = {
                v
            }

            override def increment(numberAdd: NoWrap[Int]): NoWrap[Int] = {
                numberAdd + 1
            }


            override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = {
                a + b
            }

            override def text(str: String): NoWrap[String] = {
                str
            }

            override def toUpper(wrapperStr: NoWrap[String]): NoWrap[String] = {
                wrapperStr.toUpperCase
            }

            override def concat(str1: NoWrap[String], str2: NoWrap[String]): NoWrap[String] = {
                str1.concat(str2)
            }

            override def intToString(intWrapper: NoWrap[Int]): String = {
                intWrapper.toString
            }
        }
    }

    /*object Interpreter2 {
        type Nested[ScalaValue] = ScalaToLanguageBridge[ScalaValue]

        val simplify = new Language[Nested] {


            var nesting: Int = 0

            override def number(v: Int): Nested[Int] = {
                if (nesting > 0) {

                }
            }
        }

    }*/

    def main(args: Array[String]): Unit = {
        val aa = buildComplexExpression(1, 2)
        import Interpreter1.myIntepre1

        // khi gọi apply thì mới chạy logic
        val o = aa.apply
        MLogger.generalLogger.debug("o is {}", o)
    }

}
