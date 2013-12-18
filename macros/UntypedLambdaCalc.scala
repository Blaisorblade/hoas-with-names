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

  case class Apply(fun: Term, arg: Term)

  //TODO: convert to some other first-order representation for pattern-matching. Consider using deBrujin terms, following for instance
  //"Unembedding domain-specific languages".
}


// vim: set ts=4 sw=4 et:
