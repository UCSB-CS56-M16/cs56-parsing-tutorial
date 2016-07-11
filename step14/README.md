# This Version (step14) #

The test suite for the tokenizer (in `src/edu/ucsb/cs56/pconrad/parsing/tokenizer/TestArithmeticTokenizer.java`) has been modified to use the [Factory method pattern](https://en.wikipedia.org/wiki/Factory_method_pattern).
This separates the construction of tokens with specific behaviors from the underlying implementation of those tokens, in that we no longer call out to specific constructors.

This refactor is unlikely to be something you would do in practice.
However, in our case, this allows you to define your own classes which implement `Token` without us being specific about how you should do this.
For example, instead of saying "You must define a class which implements `Token` with the name `FooToken` with a constructor which takes an `int`", we need only say "You must define something which implements `Token`, along with defining the corresponding member function in `DefaultTokenFactory` which makes `foo` tokens."

