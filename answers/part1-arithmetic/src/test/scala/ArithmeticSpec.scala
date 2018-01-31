import org.scalatest.{MustMatchers, WordSpec}
import Arithmetic._

class ArithmeticSpec extends WordSpec with MustMatchers {

    "Initial Alegbra" should {
        "evaluate" in {
            // 3 * 3 + 1 - 5
            val exp = Sub(Add(Mul(Lit(3), Lit(3)), Lit(1)), Lit(5))
            val value = evaluate(exp)
            value mustBe 5
        }
    }

    "Final Algebra" should {
        "evaluate" in {
            import Evaluate._
            val value = sub(add(mul(lit(3), lit(3)), lit(1)), lit(5))
            value mustBe 5
        }
    }

    "Equivalence" should {
        "Inital -> Final" in {
            evaluate(initialExp) mustBe Arithmetic.finalize(initialExp, Evaluate)
        }

        "Final -> Initial" in {
            finalExp(Evaluate) mustBe evaluate(finalExp(Reify))
        }
    }
}
