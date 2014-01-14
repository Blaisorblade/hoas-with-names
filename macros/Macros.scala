import scala.reflect.macros.Context

object Macros extends UntypedLambdaCalc {
  def getSrcLoc[T](c: Context)(expr: c.Expr[T]) = {
  }

  def getArgName[S, T](c: Context)(fun: c.Expr[S => T]): Option[String] = {
    import c.universe._
    fun match {
      case Expr(Function(List(ValDef(mods, paramName, typ, _)), body)) =>
        Some(paramName.decoded)
      case _ =>
        None
    }
  }

  //Let's have a macro returning Lambda. That is, Macros.Lambda, where Macros is the runtime singleton object Macros.
  def lambda(hoasBody: Term => Term): Lambda = macro lambda_impl

  //The return value must not be Macros.Lambda, since that would refer to the
  //compile-time singleton object Macros. We need instead to use the
  //runtime-receiver c.prefix.value. To constrain its type, we need to tell the
  //compiler that PrefixType is Macros.type! However, there we cannot really
  //remark on the difference between compile-time and runtime Macros.

  def lambda_impl(c: Context { type PrefixType = Macros.type })(hoasBody: c.Expr[Term => Term]): c.Expr[c.prefix.value.Lambda] = {
    import c.universe._
    val (name, userSpecified) = getArgName(c)(hoasBody) match {
      case Some(name) => (name, true)
      case None => (c.fresh("x_"), false) //Reuse freshname generator from macros.
    }
    reify(Lambda(c.literal(name).splice, c.literal(userSpecified).splice, hoasBody.splice))
  }
}

// vim: set ts=8 sw=2 et:
