package parser_combinators_chap9.impl_json_parser


//tất cả các lớp con của json đều nằm ở đây
object JSON {

    sealed trait JSON

    case object JNull extends JSON

    case class JNumber(get: Double) extends JSON

    case class JString(get: String) extends JSON

    case class JBool(get: Boolean) extends JSON

    case class JArray(get: IndexedSeq[JSON]) extends JSON

    case class JObject(get: Map[String, JSON]) extends JSON

}
