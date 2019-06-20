package main

import scala.beans.BeanProperty

class Person(@BeanProperty name: String, @BeanProperty age: Int) {
    def this() {
        this("phuvh", 24)
    }

    def this(name: String) {
        this(name, 0)
    }

    override def toString: String =  s"$name: $age"

}
