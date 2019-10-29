package useoop.morequeryopt

import org.bson.Document

case class Query(dataQueryOrigin: Document, queryOption: QueryOption = NoOption) {

    def sort(sortData: Document): Query = Query(dataQueryOrigin, SortOption(sortData, queryOption))

    def skip(numSkip: Int): Query = Query(dataQueryOrigin, SkipOption(numSkip, queryOption))

    def limit(maxSize: Int): Query = Query(dataQueryOrigin, LimitOption(maxSize, queryOption))
}
