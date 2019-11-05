package functional.type_params

import useoop.MLogger


object TestTypeParams {

    def myIndexOf[A](l: List[A], v: A): Int = {
        return l.indexOf(v)
    }

    def myIndexOf_2[A](l: List[A], v: A): Maybe[Int] = {
        val ii = myIndexOf(l, v)
        if (ii != -1) Maybe.just[Int](ii)
        else Maybe.nil
    }


    def main(args: Array[String]): Unit = {
        val l: List[String] = List[String]("1", "2", "one", "two")

        val i: Maybe[Int] = myIndexOf_2(l, "one__")

        i match {
            case Just(v) => {
                MLogger.generalLogger.info("index is {}", v)
            }
            case Nil => {
                MLogger.generalLogger.info("list not contain such element")
            }
            case _ => {

            }
        }

    }
}
