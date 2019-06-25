package main

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

    def testFirstClassFunction: (Int, Int) => Int = {
        val t = (a: Int, b: Int) =>  {
            if (a % 2 == 0) a + b
            else 0
        }
        t
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

    class Student(n: String, a: Int) {
        val name: String = n
        val age: Int = a

        def +(s: Student): Student = {
            new Student(name + s.name, age + s.age)
        }

        override def toString: String = {
            s"$name: $age"
        }
    }

    object Student {
        def apply(name: String, age: Int): Student = {
            println("apply call")
            new Student(name, age)
        }

        def unapply(arg: Student): (String, Int) = {
            (arg.name, arg.age)
        }
    }

    def pair2(i: Int): (Int, Int) = {
        (i, i)
    }


    def tryCurryFunc(): Unit = {
        val s: Student = Student("phuvh", 25)
        val sf: Student => Student = s + _
        val i = sf(s)
        println(i)
    }

    def tryMonad(): Unit = {
        val y: Option[String] = Option("phuvh")
        y match {
            case Some(value) => {
                println(s"value is $value")
            }
            case None => {

            }
        }

    }

    def tryPartiallyApplyFunc(): Int => Int = {
        val myf = (a: Int, b: Int) => a + b
        val myf2 = myf(_, 10)
        myf2
    }

    def concatenator(w1: String): String => String = {
        /*w2 => w1 + " " + w2*/
        val tmp = (w2: String) => {
            w1 + " " + w2
        }
        tmp
    }


    def cryFuncv2(a: Int): Int => Int = {
        // direct define
        b => a + b
        // use closure define
        /*val g = (u: Int) => {
            u + a
        }
        g*/
    }


    def curryFunc(a: String)(b: String): String = {
        a + " " + b
    }

    def main(args: Array[String]): Unit = {
        println("this is scala")

    }
}
