# This Version (step09) #

Produces ASTs.
Major improvement over step08 is that this introduces the state pattern in tokenization.
Not only does this make this easier to extend, it also makes it easy to remove the mutable instance variable tracking where we are in the input.
