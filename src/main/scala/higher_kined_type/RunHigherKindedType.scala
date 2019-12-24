package higher_kined_type

import useoop.MLogger


object SummableInt extends Summable[Int] {
    override def plus(a: Int, b: Int): Int = a + b

    override def init(): Int = 0
}

object SummableStr extends Summable[String] {
    override def plus(a: String, b: String): String = a + b

    override def init(): String = ""
}

object ListFoldLeft extends MFoldable[List] {
    override def foldLeft[A](coll: List[A], m: Summable[A]): A = {
        coll.foldLeft(m.init())(m.plus)
    }
}


object RunHigherKindedType {


    def runMapperExample(): Unit = {
        val eitherMapper = new EitherMapper[Exception]

        val myEither: Either[Exception, String] = Right("phuvh")

        val newEither = eitherMapper.map[String, Int](myEither, str => str.length)

        //newEither

        MLogger.generalLogger.debug("new either is {}", newEither)

    }


    def finalAbstractFunctionSum[F[_], A](coll: F[A], summable: Summable[A], foldable: MFoldable[F]): A = {
        foldable.foldLeft(coll, summable)
    }

    def main(args: Array[String]): Unit = {
        /*val l = List("phuvh", "quyvv", "vint", "longtm")
        //val i = ListFoldLeft.foldLeft(l, SummableStr)
        val o = finalAbstractFunctionSum[List, String](l, SummableStr, ListFoldLeft)

        MLogger.generalLogger.debug("")*/


        runMapperExample()

    }
}
