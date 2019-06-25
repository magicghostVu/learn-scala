package main

import java.io.File


object Main {

    def testOperator: String = {
        val h = 1
        var u = h.+(2)
        val gg = new Array[String](3)
        gg(1) = "phuvh"
        val k = gg(1)
        k
    }

    def aa = {
        var v = new Person()
        val g = testOperator
        println(g)
    }


    def factorial(a: Int): Int = {
        if (a == 1) {
            1
        } else {
            factorial(a - 1) * a
        }
    }

    def factorialv2(a: Int): Int = {
        def fac(i: Int, aa: Int): Int = {
            if (i == 1) aa
            else {
                fac(i - 1, i * aa)
            }
        }

        fac(a, 1)
    }

    def testReturnFunc = {
        def st(a: String) = {
            a.length
        }
    }


    // các ví dụ
    def functionSignature(): Unit = {
        val add: (Int, Int) => Int = (x, y) => {
            x + y
        }
        val subtract: (Int, Int) => Int = (x, y) => {
            x - y
        }

        val ff: (Int, Int, (Int, Int) => Int) => Int = (a, b, c) => {
            c(a, b)
        }

        val t = ff(2, 3, subtract)


        val myPow: (Double, Double) => Double = (a, b) => {
            a * b
        }

        val h = (a: Int, b: Int) => {
            a + b
        }


        def gg(a: Int, b: Int): Int = {
            a + b
        }

        /*val ff = (a: Int, b: Int, fParam: (Int, Int) => Int) => {
            fParam(a, b)
        }*/
    }

    def main(args: Array[String]): Unit = {


        val l1 = System.currentTimeMillis()

        //val h: List[Int] = List(1, 2, 4, 5, 6, 7)


        val hh = factorial(10)

        val hh2 = factorialv2(10)

        val l2 = System.currentTimeMillis()
        println(s"result factorial is $hh vs is $hh2, time is ${l2 - l1}")

    }
}
