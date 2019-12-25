package functional.type_class

// json converter trong trường hợp nàu được gọi là một type class
trait JsonConverter[A] {
    def toJson(a: A): String
}
