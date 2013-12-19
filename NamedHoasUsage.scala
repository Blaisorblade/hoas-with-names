object NamedHoasUsage extends App {
  import Macros._
  println(lambda(x => x))
  println(lambda(identity)) //Test freshname generation.
  println(lambda(identity)) //Do it again.
  println(lambda(identity(x => x)))
  //identity compose identity doesn't work.
  //println(lambda(identity compose identity)) //Do it again.
  val id = identity[Term] _
  println(lambda(id compose id)) //Do it again.
  println(lambda(x => lambda(y => x)))
  println(lambda(x => lambda(y => y)))

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
