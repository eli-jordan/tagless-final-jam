import cats.MonadError

import scala.language.higherKinds

package object reading {

  /**
    * Type alias for MonadError with the error type fixed to Throwable, allowing
    * it to be used as a context bound.
    *
    * e.g. Something[F[_]: Throwing](...)
    */
  type Throwing[F[_]] = MonadError[F, Throwable]

  /**
    * Summoning method for MonadError with Throwable error type
    */
  def Throwing[F[_]](implicit t: Throwing[F]): Throwing[F] = t
}
