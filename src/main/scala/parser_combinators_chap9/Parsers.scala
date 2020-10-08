package parser_combinators_chap9


/// một interface chung chung cho việc parse một input
trait Parsers[ParserError, Parser[+ _]] {
    self =>
    def run[A](parser: Parser[A])(input: String): Either[ParserError, A]

    def or[A](parserA: Parser[A], parserB: Parser[A]): Parser[A]

    def many[A](parser: Parser[A]): Parser[List[A]]

    def map[A, B](parser: Parser[A])(f: A => B): Parser[B] = {
        flatMap(parser)(a => {
            succeed(f(a))
        })
    }


    // return a potion of input if parse success
    def slice[A](parser: Parser[A]): Parser[String]

    def product[A, B](p: Parser[A], p2: Parser[B]): Parser[(A, B)] = {
        flatMap(p)(a => {
            p2.map(b => (a, b))
        })
    }


    def map2[A, B, C](parserA: Parser[A],
                      parserB: Parser[B])(f: (A, B) => C): Parser[C] = {
        flatMap(parserA)(a => {
            parserB.map(b => {
                f(a, b)
            })
        })
    }


    def manyBasedOnOthers[A](parser: Parser[A]): Parser[List[A]] = {
        map2(parser, manyBasedOnOthers(parser))(_ :: _) or succeed(List[A]())
    }


    // new primitive
    def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B]

    // lazy
    def wrap[A](p: => Parser[A]): Parser[A]

    // chưa hiểu ý đồ của đoạn này =)))))))))))))))))
    def many1[A](parser: Parser[A]): Parser[List[A]] = map2(parser, many(parser))(_ :: _)


    // chuyển một giá trị bất kỳ về Parser[String](sau đó ngầm định chuyển về ParserOps[String])
    implicit def asStringParser[A](a: A)(f: A => Parser[String]): ParserOps[String] = f(a)


    implicit def listOfN[A](n: Int, pa: Parser[A]): Parser[List[A]]


    implicit def char(char: Char): Parser[Char] = string(char.toString).map(_.charAt(0))

    implicit def string(str: String): Parser[String]


    def succeed[A](a: A): Parser[A] = string("").map(_ => a)


    implicit class ParserOps[A](p: Parser[A]) {
        def |[B >: A](p2: Parser[B]): Parser[B] = self.or[B](p, p2)

        def or[B >: A](p2: Parser[B]): Parser[B] = self.or[B](p, p2)

        def many[B >: A](parser: Parser[B]): Parser[List[B]] = self.many(p)

        def map[B](f: A => B): Parser[B] = self.map(p)(f)

        def flatMap[B](bindingFunction: A => Parser[B]): Parser[B] = self.flatMap(p)(bindingFunction)
    }

}
