package useoop.morequeryopt

import org.bson.Document

case class Query(dataQueryOrigin: Document, queryOption: QueryOption = NoOption) {
    def sort(sortData: Document): Query = copy(dataQueryOrigin, SortOption(sortData, queryOption))

    def skip(numSkip: Int): Query = copy(dataQueryOrigin, SkipOption(numSkip, queryOption))

    def limit(maxSize: Int): Query = copy(dataQueryOrigin, LimitOption(maxSize, queryOption))
}
