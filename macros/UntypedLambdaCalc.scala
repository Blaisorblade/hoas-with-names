import scala.language.implicitConversions

trait Names {
  sealed trait Name {
    def name: String
  }
  case class StringName(name: String) extends Name
  case class IdxStringName(prefix: String, idx: Int) extends Name {
    //It's a val to cache the result.
    override val name = prefix + idx
  }
  implicit def string2name(name: String): Name = StringName(name)
}

trait UntypedLambdaCalc extends Names {
  //A HOAS-based term representation for lambda-calculus. Function terms carry
  //a name, but this is only to remember what name the user specified, and
  //should only be used for cosmetic purposes, such as making pretty-printing
  //results more readable.
  sealed trait Term

  case class Lambda(name: Name, userSpecified: Boolean, hoasBody: Term => Term) extends Term {
    //XXX Consider moving this to prettyprint.
    override def toString = {
      case class Var(name: Name) extends Term {
        override def toString = name.name
      }
      s"Lambda(${name.name} => ${hoasBody(Var(name))})"
    }
  }

  case class Apply(fun: Term, arg: Term)

  //TODO: convert to some other first-order representation for pattern-matching. Consider using deBrujin terms, following for instance
  //"Unembedding domain-specific languages".
}


// vim: set ts=8 sw=2 et:
