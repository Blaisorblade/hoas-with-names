object Macros extends NamedLambdaBuilder with UntypedLambdaCalc
trait LambdaUtils {
  import Macros._
  def runtimeLet(value: Term)(hoasBody: Term => Term): Term = Apply(lambda(hoasBody), value)
}


object NamedHoasUsage extends App with LambdaUtils {
  import Macros._

  println(lambda(x => x))
  println(lambda(x_+ => x_+))
  println(lambda(y => y))

  println(lambda(identity)) //Test freshname generation.
  println(lambda(identity)) //Do it again.
  println(lambda(identity(x => x)))
  //identity compose identity doesn't work.
  //println(lambda(identity compose identity)) //Do it again.
  val id = identity[Term] _
  println(lambda(id compose id)) //Do it again.
  println(lambda(x => lambda(y => x)))
  println(lambda(x => lambda(y => y)))

  println()
  println("runtimeLet must guess the variable name:")
  println(lambda(y => runtimeLet(y)(z => Apply(y, z))))
  println()
  println("macroLet just works:")
  println(lambda(y => macroLet(y)(z => Apply(y, z))))

  println()
  println()
  println("Can you spot the differences?")
  //Break prettyprinting.
  println(lambda {
      x =>
        val y = x
        lambda { x =>
          x
        }
      })
  println(lambda {
      x =>
        val y = x
        lambda { x =>
          y
        }
      })
  println("No? Then I guess pretty printing is broken.")
}

// vim: set ts=8 sw=2 et:
