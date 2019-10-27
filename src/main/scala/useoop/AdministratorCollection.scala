package useoop

trait AdministratorCollection extends ReadOnlyCollection {
    def drop: Unit = internal drop()

    def dropIndexes: Unit = internal dropIndexes()
}
