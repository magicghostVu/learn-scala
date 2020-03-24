package functional.tagless_final

import cats.Monad
import cats.data.State
import cats.syntax.eq._
import cats.instances.string._
import cats.syntax.either._


case class User(email: String, password: String)

case class AuthErr(errCode: String)

case class RegistrationError(msg: String)


// F ở đây là một monad
trait Dsl[F[_]] {
    def register(email: String, password: String): F[Either[RegistrationError, User]]

    def authUser(email: String, password: String): F[Either[AuthErr, User]]

}

object Dsl {
    type UserRepository = List[User]

    type UserRepositoryState[A] = State[UserRepository, A]

    implicit object StateInterpreter extends Dsl[UserRepositoryState] {

        override def register(email: String, password: String): UserRepositoryState[Either[RegistrationError, User]] =
            State { users =>
                if (users.exists(_.email === email))
                    (users, RegistrationError("User already exists").asLeft[User])
                else {
                    val user = User(email, password)
                    (users :+ user, user.asRight[RegistrationError])
                }
            }

        override def authUser(email: String, password: String): UserRepositoryState[Either[AuthErr, User]] =
            State.inspect(_
                .find(user => user.email === email && user.password === password)
                .toRight(AuthErr("Authentication failed")))
    }

}

object ExampleWithCats extends App {
    /*def registerAndLogin[F[_] : Monad](implicit authnDsl: Dsl[F]): F[Either[AuthErr, User]] = {

        val email = "john@doe.com"
        val password = "swordfish"

        for {
            _ <- authnDsl.register(email, password)
            authenticated <- authnDsl.authUser(email, password)
        } yield authenticated
    }*/
}
