package functional.type_class

trait JsonConverter[A] {
    def toJson(a: A): String
}
