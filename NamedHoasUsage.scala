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
}

// vim: set ts=8 sw=2 et:
