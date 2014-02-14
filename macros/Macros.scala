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

  //I thought I'd need to reuse getArgName, but since let is a derived operation and can be expressed through lambda, macroLet_impl can reuse directly lambda_impl.

  def macroLet(value: Term)(hoasBody: Term => Term): Term = macro macroLet_impl
  //Why do we need the dependent type for Term, but not for Term => Term? This seems a bug in the function producing the expected shape.
  def macroLet_impl(c: Context { type PrefixType = Macros.type })(value: c.Expr[c.prefix.value.Term])(hoasBody: c.Expr[Term => Term]): c.Expr[c.prefix.value.Term] = {
    import c.universe.{Apply => _, _}
    //what's below does not work because we can't use macros in the same compilation unit:
    //reify(Apply(lambda(hoasBody.splice), value.splice))
    //But we can invoke the macro definition:
    reify(Apply(lambda_impl(c)(hoasBody).splice, value.splice))
    //The receiver of a splice call can be an arbitrary expression, though that does not come for free since reify is a (primitive) macro.
  }
//
}

// vim: set ts=8 sw=2 et:
