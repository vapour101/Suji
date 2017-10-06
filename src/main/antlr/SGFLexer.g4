lexer grammar SGFLexer;

fragment DIGIT      : [0-9];
fragment LETTER     : [a-zA-Z];
fragment UPPERCASE  : [A-Z];

JUNK                : ~('(')* -> skip, pushMode(CONTENTMODE);

mode CONTENTMODE;
LPAREN    		    : '(';
RPAREN			    : ')';
LBRACKET            : '[' -> pushMode(VALUEMODE);
NODESTART           : ';';
IDENTIFIER          : UPPERCASE+;
WS                  : [ \n\r\t] -> skip;

mode VALUEMODE;
RBRACKET            : ']' -> popMode;
VALUE               : (~(']') | '\\]')+;
