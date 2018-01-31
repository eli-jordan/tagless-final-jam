package arithmetic

/**
  * Tagless final can be thought of as one way to encode the interpreter pattern.
  *
  * To illuminate this intuition, in this exercise we will implement an expression language for integer
  * arithmetic, using the traditional ADT approach, and the tagless final approach.
  *
  * The language must support four operations;
  *
  * 1. "lit" - constructs a new integer literal
  * 2. "add" - sums two integer expressions
  * 3. "sub" - subtracts two integer expressions
  * 4. "mul" - multiplies two integer expressions
  */
object Arithmetic {

  /**
    * First we will implement the expression language using an ADT.
    * The four operations described above are simply defined as a set of case classes.
    */
  sealed trait IExp

  case class Lit(value: Int)           extends IExp // constructs a new integer literal
  case class Add(lhs: IExp, rhs: IExp) extends IExp // sums two integer expressions
  case class Sub(lhs: IExp, rhs: IExp) extends IExp // subtracts two integer expressions
  case class Mul(lhs: IExp, rhs: IExp) extends IExp // multiplies two integer expressions

  // Define a data structure that represents
  // 3 * 3 + 1
  val initialExp = Add(Mul(Lit(3), Lit(3)), Lit(1))

  /**
    * Exercise - Define an 'evaluate' function that computes the value represented
    * by an IExp.
    */
  def evaluate(exp: IExp): Int = exp match {
    case Lit(v)        => ???
    case Add(lhs, rhs) => ???
    case Sub(lhs, rhs) => ???
    case Mul(lhs, rhs) => ???
  }

  /**
    * To translate our ADT representation of the expression language
    * into the tagless final encoding.
    *
    * We simply;
    * 1. Make each case class a method on a trait, with a type parameter.
    * 2. Any parameters to the case classes become parameters to the methods.
    * 3. Anywhere the base type of our ADT (IExp in this case) is used, we use the type parameter.
    */
  trait FExp[A] {
    def lit(value: Int): A

    def add(lhs: A, rhs: A): A

    def sub(lhs: A, rhs: A): A

    def mul(lhs: A, rhs: A): A
  }

  // Define an expression that represents
  // 3 * 3 + 1
  def finalExp[A](exp: FExp[A]): A = {
    import exp._
    add(mul(lit(3), lit(3)), lit(1))
  }

  /**
    * Exercise - Define an evaluator for the tagless final encoding of our expression language.
    * Note that the evaluator becomes an implementation of the trait that defines the
    * operations in the language, rather than a pattern match on the case classes.
    */
  object Evaluate extends FExp[Int] {
    override def lit(value: Int): Int = ???

    override def add(lhs: Int, rhs: Int): Int = ???

    override def sub(lhs: Int, rhs: Int): Int = ???

    override def mul(lhs: Int, rhs: Int): Int = ???
  }

  /**
    * The evaluator function for 'IExp' that is implemented in terms of 'FExp'
    */
  def toFinal[A](exp: IExp, c: FExp[A]): A = exp match {
    case Lit(v)        => c.lit(v)
    case Add(lhs, rhs) => c.add(toFinal(lhs, c), toFinal(rhs, c))
    case Sub(lhs, rhs) => c.sub(toFinal(lhs, c), toFinal(rhs, c))
    case Mul(lhs, rhs) => c.mul(toFinal(lhs, c), toFinal(rhs, c))
  }

  /**
    * The evaluator for 'FExp' that is implemented in terms of 'IExp'
    */
  object Reify extends FExp[IExp] {
    override def lit(value: Int): IExp = Lit(value)

    override def add(lhs: IExp, rhs: IExp): IExp = Add(lhs, rhs)

    override def sub(lhs: IExp, rhs: IExp): IExp = Sub(lhs, rhs)

    override def mul(lhs: IExp, rhs: IExp): IExp = Mul(lhs, rhs)
  }
}
