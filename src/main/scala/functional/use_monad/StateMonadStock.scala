package functional.use_monad

object StateMonadStock {

    type Stock = Map[String, Double]

    type Transaction[+A] = Stock => (A, Stock)

    def Price(name: String): Double = {
        1.0
    }


    def portfolio(name: String)(stock: Stock): Double = stock.getOrElse(name, 0.0)

    // trả về số đã mua được và một state stock mới
    def buy(name: String, amountMoney: Double)(stock: Stock): (Double, Stock) = {
        val purchased = amountMoney / Price(name)
        val newAmount = portfolio(name)(stock) + purchased
        (purchased, stock + (name -> newAmount))

    }


    // trả ra số tiền bán được
    def sell(name: String, quantity: Double)(stock: Stock): (Double, Stock) = {
        val crQuantity = portfolio(name)(stock)
        val maxQCanSell = Math.min(crQuantity, quantity)
        val rev = maxQCanSell * Price(name)
        (rev, stock + (name -> (crQuantity - maxQCanSell)))
    }


    // bán hết của 1 hãng và lấy tiền đó mua của một hãng khác
    def move(from: String, to: String)(stock: Stock): ((Double, Double), Stock) = {
        val crQuantity = portfolio(from)(stock)
        val (revSell, newStock) = sell(from, crQuantity)(stock)
        val (qPurchased, newStock2) = buy(to, revSell)(newStock)
        ((crQuantity, qPurchased), newStock2)
    }


    def get(name: String): Transaction[Double] = {
        stock => {
            (portfolio(name)(stock), stock)
        }
    }


    def buyPartial(name: String, amount: Double): Transaction[Double] = {
        buy(name, amount)
    }


    def sellPartial(name: String, quantity: Double): Transaction[Double] = {
        sell(name, quantity)
    }

    def movePartial(from: String, to: String): Transaction[(Double, Double)] = {
        move(from, to)
    }


    implicit def map[A, B](ta: Transaction[A])(fMap: A => B): Transaction[B] = {
        ta.andThen(aNewStock => {
            val b = fMap(aNewStock._1)
            val newStock = aNewStock._2
            (b, newStock)
        })
    }

    implicit def flatMap[A, B](transactionA: Transaction[A])(fFlatMap: A => Transaction[B]): Transaction[B] = {
        transactionA.andThen(aNewS => {
            val newStock: Stock = aNewS._2
            val a: A = aNewS._1
            val tb: Transaction[B] = fFlatMap(a)
            tb(newStock)
        })
    }

    def apply[A](a: A): Transaction[A] = {
        stock => (a, stock)
    }


    def main(args: Array[String]): Unit = {

    }

}
