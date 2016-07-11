# This Version (step10) #

Produces ASTs.
Major improvement over step09 is that this introduces the visitor pattern on `Token`s.
This avoids an `instanceof` check in the parser.
We could apply the visitor pattern more throughly in the parser, though there isn't much reason to do so; it mostly just introduces boilerplate, and it makes things look ugly.
