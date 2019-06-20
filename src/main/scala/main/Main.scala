package main

object Main {

    def testOperator: String = {

        val h = 1

        var u = h.+(2)

        val gg = new Array[String](3)
        gg(1)="phuvh"
        val k = gg(1)
        k
    }

    def aa = {
        var v = Person("phuvh", 24)


        val g = testOperator
        println(g)
    }

    def main(args: Array[String]): Unit = {
        /*print("Hello Scala\n")
        val t: StringBuilder = new StringBuilder()

        val r = new java.lang.StringBuilder()

        r.append("okok, this is java String builder");
        t.append("phuvh")
        t.append(" love vint")


        val threadName = Thread.currentThread().getName

        val s: String = s"data is $t thread name is $threadName"
        println(s)*/
        aa


    }
}
