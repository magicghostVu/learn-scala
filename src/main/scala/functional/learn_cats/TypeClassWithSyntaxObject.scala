package functional.learn_cats

import useoop.MLogger

case class Person(name: String, age: Int)

object TypeClassWithSyntaxObject {


    trait ConvertableToString[A] {
        def mToString(a: A): String
    }

    object allConvertStringInstance {
        implicit val intConvertString: ConvertableToString[Int] = (a: Int) => a.toString

        implicit object floatConvertToString extends ConvertableToString[Float] {
            override def mToString(a: Float): String = a.toString
        }

        implicit object PersonConvertToString extends ConvertableToString[Person] {
            override def mToString(a: Person): String = {
                val name = a.name
                val age = a.age
                s"(Person $name:$age)"
            }
        }

    }

    object convertToStringSyntax {

        implicit class convertToStringOps[A](val a: A) extends AnyVal {
            def convertToString(implicit convertableToString: ConvertableToString[A]): String = {
                convertableToString.mToString(a)
            }
        }

    }


    def main(args: Array[String]): Unit = {
        import allConvertStringInstance._
        import convertToStringSyntax._
        val i = 1.convertToString
        val ii = 1.4f.convertToString

        val p = Person("phuvh", 24)

        MLogger.generalLogger.info("i is {}, ii is {}, p is {}", Array[AnyRef](i, ii, p.convertToString): _*)
    }

}
