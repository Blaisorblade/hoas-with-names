object NamedHoasUsage extends App {
  import Macros._
  println(lambda(x => x))
  println(lambda(identity)) //Test freshname generation.
  println(lambda(identity)) //Do it again.
  println(lambda(x => lambda(y => x)))
  println(lambda(x => lambda(y => y)))
}

// vim: set ts=8 sw=2 et:
