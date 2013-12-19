hoas-with-names
===============

Represent functions using higher-order asbtract syntax (HOAS) *using macros to save names*.

To try this out, launch sbt and type the `run` command. This will show the reification of the terms in [`NamedHoasUsage.scala`](NamedHoasUsage.scala), which preserves user-specified names! However, those names are there for strictly cosmetic purposes - they do not represent the binding structure accurately.

If you're curious about the implementation, take a look at [`macros/Macros.scala`](macros/Macros.scala).

Implementation note: I started this project using macro-paradise and quasiquotes (see branch topic/still-with-quasiquotes), but switched temporarily away from that because of some minor but annoying instabilities (scalamacros/paradise#11), and because I wanted to encourage people to reuse this project. Hence for now I'm not using quasiquotes on master.
