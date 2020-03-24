package functional.monad_transfromer

import useoop.MLogger

object RunMonadTransformer {


    def createData(): Option[List[Int]] = {
        Option(List(1, 2, 3, 4, 5))
    }


    case class OptionList[A](data: Option[List[A]]) {
        def map[B](fmap: A => B): OptionList[B] = {
            val tmpValue = data.map(l => {
                l.map(fmap)
            })
            OptionList(tmpValue)
        }

        def flatMap[B](binding: A => OptionList[B]): OptionList[B] = {
            val tmpData = data.flatMap(l => {
                val tmpl = l.flatMap(a => {
                    val tmp = binding(a)
                    tmp.data match {
                        case Some(ll) => ll
                        case None => Nil
                    }
                })
                Option(tmpl)
            })
            OptionList(tmpData)
        }
    }

    def main(args: Array[String]): Unit = {

        // dùng flatMap chay
        val rr: Option[List[Double]] = createData().flatMap(l => {
            Option(l.flatMap(
                i => List(i.toDouble)
            ))
        })
        MLogger.generalLogger.debug("rr is {}", rr)

        // thử dùng for-comprehension
        val h = for {
            l <- rr
        } yield {
            for {
                i <- l
            } yield i.toDouble
        }

        MLogger.generalLogger.debug("h is {}", h)

        val myCustomData: OptionList[Int] = OptionList(createData())

        // dùng monad transformer
        val oo = for {
            i <- myCustomData
            ii <- myCustomData
        } yield i + ii

        MLogger.generalLogger.debug("oo is {}", oo.data.get.size)

        val either = Right[Int, String]("phuvh")

        val e = either.flatMap(str => {
            Left[Int, String](7)
        })


    }
}
