package functional.use_monad

import useoop.MLogger

case class PriceState(price: Double)

object RunMonad {


    def findBasePrice(priceState: PriceState): Double = {
        priceState.price
    }

    def stateDiscount(priceState: PriceState): Double = {
        priceState.price * 0.9
    }

    def productDiscount(priceState: PriceState): Double = {
        priceState.price * 0.8
    }

    def applyTax(priceState: PriceState): Double = {
        priceState.price * 1.1
    }


    def modifiedPriceState(fCalculatePrice: PriceState => Double): MState[PriceState, Unit] = {

        // trả về một priceState mới với giá được tính lại
        MState.modify[PriceState](oldState => oldState.copy(price = fCalculatePrice(oldState)))


    }


    def main(args: Array[String]): Unit = {

        //biến stateMonad đã inject tất cả các logic về việc đối tượng đầu vào sẽ biến đổi như thế nào qua các bước
        //nó chỉ đợi thằng đầu vào ban đầu đi vào và chạy code


        def logStep(f: PriceState => String): MState[PriceState, String] = MState.gets[PriceState, String](f)

        val stateMonad = for {
            _ <- modifiedPriceState(findBasePrice)
            a <- logStep(p => "base price is " + p.price)
            _ <- modifiedPriceState(stateDiscount)
            _ <- modifiedPriceState(productDiscount)
            _ <- modifiedPriceState(applyTax)

        } yield a


        val priceState: PriceState = PriceState(10.0)

        val updated: (PriceState, String) = stateMonad(priceState)


        MLogger.generalLogger.debug("updated is {}, base is {}", updated._1.price, updated._2)


    }

    def monadLaw(): Unit = {

        def f1(x: Int) = {
            List(x, x + 1)
        }

        val l1 = List(1).flatMap(f1)
        val l2 = f1(1)
    }

}
