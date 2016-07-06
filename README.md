# cs56-parsing-tutorial

This tutorial presents some of the basic concepts that are used in parsers and interpreters.   

It is not intended as a full treatment of those topics, but rather as "just enough" material to help you complete a programming assignment that involves parsing of arithmetic expressions.     We do not expect you to be familiar with these topics already, so we are providing a brief overview in this tutorial.

You will get a more in-depth introduction to these topics in courses such as:

* [CMPSC 138](http://www.cs.ucsb.edu/education/courses/cmpsc-138) (Automata and Formal Languages)
* [CMPSC 160](http://www.cs.ucsb.edu/education/courses/cmpsc-160) (Compilers)
* [CMPSC 162](http://www.cs.ucsb.edu/education/courses/cmpsc-162) (Programming Languages)

This tutorial is by Kyle Dewey, with some edits from Phill Conrad.

# Summary #

During this course, you will have a programming assignment in which you add some features to an existing parser and interpreter of arithmetic expressions.

The provided interpreter may or may not be buggy; it is your responsibility to test things thoroughly.
The bulk of the difficulty of this assignment is expected to be in determining exactly what in the existing interpreter must be modified, without dramatically changing how the interpreter works.

# The source code in this tutorial

As a starting point for your programming assignment, you'll be provided with some code that implements all three of these phases.    We'll call this the "starter code".     That starter code will be provided to you in a repo that you can "pull" from to populate your own private repo where you write your solution.    

This starter code is fairly complex.

To help you understand this code, we have provided a series of examples of a complete tokenizer/parser/interpreter that show a progression from:

* in the starting point, a simplistic code structure that is easier to understand, but limited in its power,
* through a series of intermediate refactorings, each motivated by either a desire to make the code more powerful, or easier to maintain, or both,
* to, finally, the code that you will have as your starting point.

This progression from simpler, but limited code, to your final starting point code is contained in this repository in  thirteen subdirectories, called `part01` through `part13`. 

*   `part01` is the first version, which has a major bug present, and it can only determine if something parses.
    That is, it does *not* produce ASTs, so it is not very useful in practice.
    However, it is extremely simple, with nearly all the code in a single file under 150 lines.
*   The code in `part02` through `part12` leads you through a series of refinements and refactorings.
*   Incrementally this is refined all the way up until `part13`, which is equivalent to the `master` branch of 
    the repo containing your starter code, [cs56-parsing-assignment]({{page.starter_repo}}).

The rest of this README.md file provides a high-level view of how parsing and interpreting works.     It is recommended that you start by reading this file through from start to finish.  Later, you can proceed through each of the README.md files in  `part01` through `part13`, looking at the source code in each directory, to understand how the code works, and how it was developed.

Javadoc for the starter code in [cs56-parsing-assignment]({{page.starter_repo}} is available: 
* [cs56-parsing-assignment-javadoc](https://ucsb-cs56-m16.github.io/cs56-parsing-assignment-javadoc/).


# Background #

The execution of a program is a complex problem requiring multiple distinct components working together.
To illustrate this complexity, consider the snippet of Java code below:

```java
if (x > 7) { // magic number
  foo = 10;
} else {
  /* TODO: this needs a better solution
     right now this is broken */
  System.err.println( "FIX THIS CODE" );
}
```

From a human standpoint, the above code is relatively simple.
However, from the standpoint of a system which reads the above code character-by-character, there are immediately some challenges here, including:

1. The characters `x > 7` (as in the above program) have the same meaning as `x>7`, as whitespace is unimportant in this context.
   For simplicity and consistency, these two forms should be treated uniformly.
   Simply stripping programs of all whitespace looks promising here, except...
2. Some whitespace is significant.
   For example, with `"FIX THIS CODE"`, we cannot simply strip out the whitespace.
   Otherwise the program would print `"FIXTHISCODE"`, which differs from the intended string.
   In this particular example, the component which reads the code must understand that `"` is somehow significant, and that the whole string `"FIX THIS CODE"` must be treated as a single unit.
3. Certain groupings of characters act as a single unit.
   This was true for `"FIX THIS CODE"`, but it is also true for `if`, `else`, `foo`, `System`, `err`, and `println`.
   This is even true for `10`, which represents the decimal constant "10" as opposed to two completely independent digits.
   Each of these element groupings are indivisible.
4. Whole groupings of characters form even larger cohesive units.
   The previous point alone suggests that the character string `if if else if if if` might form a valid program, but this clearly isn't true.
   In the Java snippet, `if` and `else` work together to form a single if-else statement.
5. Making things even more complex with the if-else statement in the Java code snippet, the braces (`{` and `}`) are not actually required here.
   If we had multiple statements they would be required.
   As with the first point, this difference is not significant when it comes to actually executing the code (they work the same), so they should be treated uniformly.
6. We can have recursive forms.
   While `System.err.println(...)` may look simple, this actually reads as `(System.err).println(...)`.
   That is, this uses the dot (`.`) operator in a nested way.
   Nested loops and nested if-else statements behave similarly.
7. The variables `x` and `foo` have particular values which may change as the program is executed.
   The values of these variables are independent of issues like whether or not braces were used in if-else statements.
   Additionally, the comments in the above code have no effect on the program's execution.
   As such, actually executing the code is a very different problem from reading the code.

This is but a short list of potential challenges, but the main point should be clear: this is a complex problem, with multiple different concerns in play.
The trick which is often employed to make this tractable to reason about is to separate these problems into distinct phases, where each phase addresses a unique set of concerns.
There are different ways of going about this.
However, one of the simplest and most commonly-employed methods is to take a three-phase approach, where the phases are (in order):

1. Tokenization
2. Parsing
3. Interpretation

Further description of each of these phases follows.

## Tokenization ##

Tokenization involves converting larger sequences of characters into meaningful units (integers, operators, variables, keywords, etc.).
Each of these units is called a "token".
To illustrate what tokens are, consider again the aforementioned Java code snippet, repeated below for convenience:

```java
if (x > 7) { // magic number
  foo = 10;
} else {
  /* TODO: this needs a better solution
     right now this is broken */
  System.err.println( "FIX THIS CODE" );
}
```

This code snippet contains over 100 characters.
However, it contains only 24 tokens, namely (put into a table to save space):

|                 |     |      |         |        |
|-----------------|-----|------|---------|--------|
|`if`             |`(`  |`x`   |`>`      |`7`     |
|`)`              |`{`  |`foo` |`=`      |`10`    |
|`;`              |`}`  |`else`|`{`      |`System`|
|`.`              |`err`| `.`  |`println`|`(`     |
|`"FIX THIS CODE"`|`)`  | `;`  |`}`      |        |

As shown, tokens separate semantically meaningful groupings of characters into individual units.
For example, `if`, `else`, `10`, `"FIX THIS CODE"`, and others are considered individual, distinct tokens.
Additionally, components extraneous to the code, such as unnecessary whitespace and comments, are completely stripped out.
That said, there are still some "extra" components in here, such as the braces (`{` and `}`).
This is left for the next phase to handle, namely parsing.

## Parsing ##

Parsing involves recognizing structures that are made up of tokens.
These structures can be large, and they may even be recursively defined (as with if-else statements).
Such structures are usually referred to as **Abstract Syntax Trees** (ASTs).
A breakdown of this term follows:

- **Abstract**: this forms an abstraction over not only what the programmer typed, but over the language's syntax itself.
  This is in contrast to the **Concrete** syntax, which includes extra details which are ultimately unimportant to execution.
  For example, `if` statements work in the same way whether or not braces (`{` and `}`) are used.
  As such, we can treat `if` statements with and without braces uniformly, and this is precisely what is done in the abstract syntax.
- **Syntax**: these deal with the syntax of the language.
- **Tree**: The underlying representation for the code is that of a tree.

To better understand what an AST is, consider the following example.
This shows an AST which resulted from parsing the tokens `1`, `+`, `2`:

![1+2](IMAGES/1+2.png)

As shown, `+` forms the root of the tree, and it has the child nodes `1` and `2`.
Each of these is a leaf, which makes sense considering that integer constants simply evaluate to themselves without any other bits of code getting involved.

A more complex example is shown below, which uses the tokens from our running example.
The actual tokens have been repeated below for convenience.

|                 |     |      |         |        |
|-----------------|-----|------|---------|--------|
|`if`             |`(`  |`x`   |`>`      |`7`     |
|`)`              |`{`  |`foo` |`=`      |`10`    |
|`;`              |`}`  |`else`|`{`      |`System`|
|`.`              |`err`| `.`  |`println`|`(`     |
|`"FIX THIS CODE"`|`)`  | `;`  |`}`      |        |

![parsing_example](IMAGES/parsing_example.png)

As shown in the above example, parsing takes operator precedence into account.
Specifically, the parser knew to parse `System.err.println(...)` effectively as `(System.err).println(...)`, as opposed ot the invalid `System.(err.println(...))`.

## <a name="interpreter_general_description"></a>Interpretation ##

Interpretation involves taking ASTs and recursively evaluating them to values.
In a language like Java, this can get fairly complex, given the complexity of the overall language.
As such, we will refrain from using the running example here.
Instead, we'll use a much simpler arithmetic expression language for the purpose of an example.

Consider the AST below, corresponding to the expression `3 * 4 + 2`:

![interp_01](IMAGES/interp_01.png)

Evaluation starts at the top of the AST, which corresponds to the `+` node in this AST.
The `+` node first finds the value of its left child, corresponding to the `*` node.
Evaluation then proceeds to the `*` node, which gets the value of its left child.
This leads evaluation to the `3` node.
Because the constant `3` trivially evaluates to itself, evaluation returns `3` at this point.
This is illustrated in the image below, which shows values returned from evaluation in red:

![interp_02](IMAGES/interp_02.png)

Once the `3` node is complete, evaluation goes back to the `*` node, which gets the value of its right child.
Evaluation then moves to the `4` node, which simply returns `4`.
This is illustrated below:

![interp_03](IMAGES/interp_03.png)

Evaluation now proceeds to the `*` node, which finally has the values of both of its child nodes.
From here, it multiplies these values together, and returns the result.
This is illustrated below:

![interp_04](IMAGES/interp_04.png)

At this point, evaluation returns to the `+` node, which now has the value of its left child (`*`).
Evaluation then proceeds to the right child, which returns the constant `2`, illustrated below:

![interp_05](IMAGES/interp_05.png)

The `+` node finally has the values of both of its children, and subsequently adds them together.
This result is then returned.
This is illustrated below:

![interp_06](IMAGES/interp_06.png)

Note that this entire process followed the general pattern of a recursive depth-first traversal.

# Provided Components #

The starter code you'll be given consists of a tokenizer, parser, and interpreter for a simple arithmetic expression language.

In this language, there is only one type, namely `integer`, and there are only constants, operators, and parentheses.

The language has the following features:

- Addition (`+`)
- Subtraction (`-`)
- Multiplication (`*`)
- Division (`/`).
  Division by zero is considered an error condition which is handled specially.
- Parenthesized expressions
- Unary minus (e.g., `-3`)

Some examples of programs in this language follow, along with their expected results:

| Expresssion             | Result                          |
|-------------------------|---------------------------------|
| `2+4`                   |  6                              |
| `3+4/5-6*2+3/7`         | -9                              |
| `3+(4/5)-(6*2)+(3/7)`   | -9                              |
| `((3+4)/(5-6))*(2+3)/7` | -5                              |      
| `2+`                    | error (parsing)                 |
| `)5(`                   | error (parsing)                 |
| `((2+3)`                | error (parsing)                 |
| `@2`                    | error (tokenization)            |
| `1/0`                   | error (interpretation)          |

The provided starter code collectively handles the features listed above.   The assignment you'll be given will involve extending this set of features in some way.  Your assignment description will explain exactly what features you are required to add.

While some tests are included, you are encouraged to write your own tests.
There may be bugs in the provided code, and indeed some bugs may have been *intentionally placed* into the code.

Further description of each of the provided components follows.

## Provided Tokenizer ##

(In this section, we are referring to the code in `part13`, which should be the same as your starting point code.)

The tokenizer is spread across the `.java` files in `src/edu/ucsb/cs56/pconrad/parsing/tokenizer`.
The tokenizer recognizes the following tokens:

* integers (e.g., `1`, `672`, `25`)
* left-paren `(`
* right-paren `)`
* plus `+`
* minus `-`
* times `*`
* divide `/`

## Provided Parser ##

(In this section, we are referring to the code in `part13`, which should be the same as your starting point code.)

The parser is spread across the `.java` files in `src/edu/ucsb/cs56/pconrad/parsing/parser`.
While this parser is relatively simple compared to the one for Java, this still contains a fair amount of complexity.
Describing how this parser works is fairly difficult to do purely in English.
Fortunately, there are standardized ways of describing how a parser should work, and we will use one of them here.
The specific technique we will use is that of [Extended Backus-Naur Form (EBNF)](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form), which traces its history back to the development of the programming language [ALGOL](https://en.wikipedia.org/wiki/ALGOL) in the 1950s.
This technique, along with its particular application to our arithmetic expression language, is subsequently described.

In EBNF, languages are broken up into **terminals** and **non-terminals**.
To understand what these terms mean, consider the AST for `1+2` again, repeated below for convenience:

![1+2](IMAGES/1+2.png)

In this AST, `1` and `2` would be terminals, because they are leaves in the tree (that is, they *terminate* paths through the tree).
In contrast, `+` would be a non-terminal, as it acts as an internal node in the tree (that is, non-terminals *do not terminate* paths through the tree).

A relatively simple EBNF description of a parser that could handle `1+2` is shown below:

```
simple ::= INTEGER '+' INTEGER
```

The above description roughly states that `simple` is a non-terminal, which consists of a terminal `INTEGER` (which is understood to mean integers like `1`, `734`, `28`, and so on), followed by the `+` token, followed by another `INTEGER` token.

In general, grammars in EBNF consist of a series of *productions*, each of which has:
* a left-hand side consisting of a non-terminal
* the symbol `::=` which is read as "produces"
* a right-hand side, which consists of a sequence that may include both terminals and non-terminals

In our example with `simple`, there is only one production, namely `simple` itself.
The `simple` example is actually too simple for our purposes.
While it will handle inputs like `1+2`, `3+4`, and so on, it does not understand other operations like `-` (e.g., it does not handle `1-2`).
Moreover, it cannot handle nested inputs, such as `1+2+3`.
In order to handle all these other desirable features, we need to add some additional productions to this, and overall make some major changes.
Exactly what changes need to be made and why we make them is beyond the scope of this assignment and even the course overall; this is a topic better suited for [CMPSC 160](http://www.cs.ucsb.edu/education/courses/cmpsc-160) (Compilers).
As such, we will merely provide the final EBNF description of our language's parser, shown below:

```
expression ::= additive-expression
additive-expression ::= multiplicative-expression ( ( '+' | '-' ) multiplicative-expression ) *
multiplicative-expression ::= primary ( ( '*' | '/' ) primary ) *
primary ::= '(' expression ')' | INTEGER | '-' primary
```

This grammar adds in some additional complexity, along with some features of EBNF which have not yet been discussed.
Here is how to make sense of these four productions:

*   The non-terminals in the above grammar are 
    `expression`, `additive-expression`, `multiplicative-expression`, and `primary`.  
*   The terminals are `'+'`, `'-'`, `'*'`, `'/'`, `'('`, `')'`, and `INTEGER`, where `INTEGER` is a stand-in 
    for non-negative integers (e.g., `1`, `17`, `254`, and so on)
*   When parentheses appear without quotes around them (e.g. `( )` as opposed to `'('` and `')'`), 
    they are used for grouping.

* The vertical bar (`|`) signifies "or".  For example `( '*' | '/' )` means "either the star or the slash symbol appears here".
* The star, when not in quotes, signifies "zero or more repetitions of".  
  For example `primary ( ( '*' | '/' ) primary ) *` means "a instance of `primary`" followed by zero or more instances of "a star or a slash followed by another `primary`."

This EBNF description is a simplified version of the example from the Wikipedia page for [Operator-Precedence Parser](https://en.wikipedia.org/wiki/Operator-precedence_parser).
Of special note is the fact that the token `-` appears in two distinct places in the above productions.
One case handles binary minus, as with `1-2`, and another case handles unary minus, as with `-7`.
Both cases still deal with the same token, namely `-`.
The provided parser should correctly handle this description, modulo any bugs.

## Provided Interpreter ##

(In this section, we are referring to the code in `part13`, which should be the same as your starting point code.)

The interpreter is spread across the `.java` files in `src/edu/ucsb/cs56/pconrad/parsing/parser/evaluator`.
The interpreter is arguably the simplest component in all of the provided code; for the most part, it merely executes whatever the user specified using the [same mechanism previously described](#interpreter_general_description).
The only exeception to this is if division by zero occurs, in which case it will throw an `EvaluatorException`.

# Your next steps

Now that you have read through this tutorial, your next step should probably be to read through the actual assignment that you'll be working on in this course.    Your instructor will let you know where to find it.

For CS56 M16, that is here: <https://ucsb-cs56-m16.github.io/lab/lab06>

If you find that the starter code for that assignment is hard to understand, it may be helpful to skim through the README.md files for each of the thirteen subdirectories, browsing through the source code as you do, so that you can see the progression from a simpler parser/interpreter to the one that you are working with.

You may also find it helpful to clone this tutorial repo, and compile/run the code in one or more of the subdirectories.

You can then use commands such as this one to compare two files between versions:

```
diff step01/src/edu/ucsb/cs56/pconrad/parsing/Parser.java step02/src/edu/ucsb/cs56/pconrad/parsing/Parser.java
```

* [`step01/README.md`](step01/README.md)
* [`step02/README.md`](step02/README.md)
* [`step03/README.md`](step03/README.md)
* [`step04/README.md`](step04/README.md)
* [`step05/README.md`](step05/README.md)
* [`step06/README.md`](step06/README.md)
* [`step07/README.md`](step07/README.md)
* [`step08/README.md`](step08/README.md)
* [`step09/README.md`](step09/README.md)
* [`step10/README.md`](step10/README.md)
* [`step11/README.md`](step11/README.md)
* [`step12/README.md`](step12/README.md)
* [`step13/README.md`](step13/README.md)

