package useoop.morequeryopt

import org.bson.Document

case class Query(dataQueryOrigin: Document, queryOption: QueryOption = NoOption) {
    def sort(sortData: Document) = copy(dataQueryOrigin, SortOption(sortData, queryOption))

    def skip(numSkip: Int) = copy(dataQueryOrigin, SkipOption(numSkip, queryOption))

    def limit(maxSize: Int) = copy(dataQueryOrigin, LimitOption(maxSize, queryOption))
}
