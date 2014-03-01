import scala.reflect.macros.Context

object MacroImpls {
  def getSrcLoc[T](c: Context)(expr: c.Expr[T]) = {
  }

  def getArgName[S, T](c: Context)(fun: c.Expr[S => T]): Option[String] = {
    import c.universe._
    fun.tree match {
//      case Function(List(ValDef(mods, paramName, typ, _)), body) =>
//        Some(paramName.decoded)
      case q"($param) => $body" =>
        Some(param.name.decoded)
      case _ =>
        None
    }
  }

  //We want a macro returning Lambda. That is, Macros.Lambda, where Macros is
  //the runtime singleton object Macros.
  //
  //The return value must not be Macros.Lambda, since that would refer to the
  //compile-time singleton object Macros. We need instead to use the
  //runtime-receiver c.prefix.value. To constrain its type, we need to tell the
  //compiler that PrefixType is Macros.type! However, there we cannot really
  //remark on the difference between compile-time and runtime Macros.

  //We don't use c.prefix.value everywhere we need, but we should. This is a
  //consequence of SI-6447 (https://issues.scala-lang.org/browse/SI-6447), fixed
  //in 2.11. For some reason, though, the bug does not cause any more a compiler
  //error.
  def lambda_impl(c: Context { type PrefixType = NamedLambdaBuilder })(hoasBody: c.Expr[c.prefix.value.Term => c.prefix.value.Term]): c.Expr[c.prefix.value.Lambda] = {
    import c.universe._
    val (name, userSpecified) = getArgName(c)(hoasBody) match {
      case Some(name) => (name, true)
      case None => (c.fresh("x_"), false) //Reuse freshname generator from macros.
    }
    c.Expr(q"Lambda($name, $userSpecified, $hoasBody)")
  }

  //I thought I'd need to reuse getArgName, but since let is a derived operation and can be expressed through lambda, macroLet_impl can reuse directly lambda_impl.
  def macroLet_impl(c: Context { type PrefixType = NamedLambdaBuilder })(value: c.Expr[c.prefix.value.Term])(hoasBody: c.Expr[c.prefix.value.Term => c.prefix.value.Term]): c.Expr[c.prefix.value.Term] = {
    import c.universe.{Apply => _, _}
    c.Expr(q"Apply(${lambda_impl(c)(hoasBody)}, $value)")
  }
}

trait LambdaBuilder {
  type Term
  type Lambda
}

//Could be a decorator, but with static dispatch, so let's not do that
trait NamedLambdaBuilder {
  val l: LambdaBuilder
  //import l._
  //Workaround SI-8356.
  type Term = l.Term
  type Lambda = l.Lambda
  //Let's have a macro returning Lambda. That is, Macros.Lambda, where Macros is the runtime singleton object Macros.
  def lambda(hoasBody: Term => Term): Lambda = macro MacroImpls.lambda_impl
  def macroLet(value: Term)(hoasBody: Term => Term): Term = macro MacroImpls.macroLet_impl
}

// vim: set ts=8 sw=2 et:
