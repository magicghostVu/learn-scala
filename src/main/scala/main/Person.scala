package main

class Person(private var _name: String, private var _age: Int) {
    def this() {
        this("phuvh", 24)
    }

    def this(name: String) {
        this(name, 0)
    }


    def name: String = _name

    def age: Int = _age

    // coi như đây là một kiểu setter của scala
    // function assignment
    def age_=(newAge: Int): Unit = {
        _age = newAge
    }

    override def toString: String = s"$name: $age"

}
