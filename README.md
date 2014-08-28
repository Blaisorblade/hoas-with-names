hoas-with-names
===============

Represent functions using higher-order asbtract syntax (HOAS) *using macros to save names*.

To try this out, launch sbt and type the `run` command. This will show the reification of the terms in [`NamedHoasUsage.scala`](NamedHoasUsage.scala), which preserves user-specified names! However, those names are there for strictly cosmetic purposes - they do not represent the binding structure accurately.

If you're curious about the implementation, take a look at [`macros/Macros.scala`](macros/Macros.scala).

Implementation note: currently this uses Scala 2.11 macros and quasiquotes.
