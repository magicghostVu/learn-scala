package functional.expfunction

import useoop.MLogger

object MPartialFunction {
    def main(args: Array[String]): Unit = {


        val pf1: PartialFunction[String, Int] = {
            case str: String if str.length == 5 => 10
        }


        val uu = pf1.isDefinedAt("phuvh1")


        val p2: PartialFunction[String, Int] = {
            case _ => 9;
        }

        val p3 = pf1.orElse(p2)

        val int = p3("phuv")

        MLogger.generalLogger.debug("uu is {}", uu)


    }
}
