import cats.{MonadError, Parallel}

import scala.language.higherKinds

package object part2e {

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

    type ParallelId[F[_]] = Parallel[F, F]
}
