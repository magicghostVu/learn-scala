package useoop.morequeryopt

import org.bson.Document

sealed trait QueryOption

case object NoOption extends QueryOption

case class SortOption(sorting: Document, other: QueryOption = NoOption) extends QueryOption

case class SkipOption(number: Int, other: QueryOption = NoOption) extends QueryOption

case class LimitOption(maxSize: Int, other: QueryOption = NoOption) extends QueryOption

