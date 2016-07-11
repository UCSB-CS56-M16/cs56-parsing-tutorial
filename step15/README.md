# This Version (step15) #

The test suite for the parser (in `src/edu/ucsb/cs56/pconrad/parsing/parser/TestParser.java`) and the evaluator (in `src/edu/ucsb/cs56/pconrad/parsing/evaluator/TestEvaluator.java`) has been modified to use the [Factory method pattern](https://en.wikipedia.org/wiki/Factory_method_pattern).
This separates the construction of AST nodes with specific behaviors from the underlying implementation of those nodes, in that we no longer call out to specific constructors.

This refactor is unlikely to be something you would do in practice.
However, in our case, this allows you to define your own classes which implement `AST` without us being specific about how you should do this.
For example, instead of saying "You must define a class which implements `AST` with the name `FooAST` with a constructor which takes an `int`", we need only say "You must define something which implements `AST`, along with defining the corresponding member function in `DefaultASTFactory` which makes `foo` ASTs."

This is very much in the spirit of step14, but applied to AST nodes as opposed to tokens.
