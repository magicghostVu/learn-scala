package useoop

abstract class AbsRole {
    def canAccess(page: String): Boolean
}

class Root extends AbsRole {
    override def canAccess(page: String): Boolean = true
}

class Admin extends AbsRole {
    override def canAccess(page: String): Boolean = page.eq("admin")
}

object AbsRole {
    def apply(roleName: String): AbsRole = {
        roleName match {
            case "root" => new Root
            case "admin" => new Admin
            case _ => throw new IllegalArgumentException
        }
    }
}
