import scala.language.implicitConversions
trait UntypedLambdaCalc {
  //A HOAS-based term representation for lambda-calculus. Function terms carry
  //a name, but this is only to remember what name the user specified, and
  //should only be used for cosmetic purposes, such as making pretty-printing
  //results more readable.
  sealed trait Term

  case class Lambda(name: String, userSpecified: Boolean, hoasBody: Term => Term) extends Term {
    override def toString = {
      case class Var(name: String) extends Term
      s"Lambda($name => ${hoasBody(Var(name))})"
    }
  }

  case class Apply(fun: Term, arg: Term) extends Term

  //TODO: convert to some other first-order representation for pattern-matching. Consider using deBrujin terms, following for instance
  //"Unembedding domain-specific languages".
}

//XXX: below representations have been written later and have a number of
//mismatches, which should be fixed above (add numbers, fix naming scheme, and
//so on). OTOH, the handling of names is better above.
trait UntypedLambdaCalcNamed {
  sealed trait NTerm
  case class NVar(name: String) extends NTerm
  case class NFun(name: String, body: NTerm) extends NTerm
  case class NApp(fun: NTerm, arg: NTerm) extends NTerm

  case class NNum(n: Int) extends NTerm
  case class NPlus(a: NTerm, b: NTerm) extends NTerm
}

trait UntypedLambdaCalcDeBrujin {
  sealed trait DBTerm
  case class DBVar(idx: Int) extends DBTerm
  case class DBFun(name: String, body: DBTerm) extends DBTerm
  case class DBApp(fun: DBTerm, arg: DBTerm) extends DBTerm

  case class DBNum(n: Int) extends DBTerm
  implicit def toNum(n: Int): DBNum = DBNum(n)
  case class DBPlus(a: DBTerm, b: DBTerm) extends DBTerm

  /*
  var counter = 0
  def fresh() = {
    val res = "x" + counter
    counter += 1
    res
  }
  */
  object fresh {
    var counter = 0
    def apply() = {
      val res = "x" + counter
      counter += 1
      res
    }
  }

  //the by-name parameter is there to reorder side effects in a more predictable way. Otherwise, the first fresh variable is the innermost-bound one.
  def let_x_=(t: DBTerm)(body: => DBTerm) = DBApp(DBFun(fresh(), body), t)
}

object UntypedLambdaCalcDeBrujin extends UntypedLambdaCalcDeBrujin with UntypedLambdaCalcNamed {
  def toNamed(t: DBTerm, l: List[String]): NTerm =
    t match {
      case DBFun(name, body) => NFun(name, toNamed(body, name :: l))
      case DBVar(idx) => NVar(l(idx))

      case DBApp(fun, arg) => NApp(toNamed(fun, l), toNamed(arg, l))
      case DBNum(n) => NNum(n)
      case DBPlus(a, b) => NPlus(toNamed(a, l), toNamed(b, l))
    }
  val term = let_x_=(1) { let_x_=(2) { DBPlus(DBVar(1), DBVar(0)) }}
  println(term)
  println(toNamed(term, Nil))
}

// vim: set ts=8 sw=2 et:
