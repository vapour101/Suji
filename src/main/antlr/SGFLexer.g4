lexer grammar SGFLexer;

fragment DIGIT	: [0-9];
fragment LETTER : [a-zA-Z];

JUNK        	: ~('(')* -> skip, pushMode(CONTENT);

mode CONTENT;
LPAREN    		: '(';
RPAREN			: ')';
LBRACKET		: '[' -> pushMode(VALUE);

NODESTART		: ';';
UPPERCASE		: [A-Z];
WS				: [ \n\r\t] -> skip;

mode VALUE;
RBRACKET		: ']' -> popMode;
SEPARATOR		: ':';

DOUBLE			: '1' | '2';
NUMBER			: ('+'|'-')? DIGIT+;
REAL			: NUMBER ('.' DIGIT+)?;
COLOR			: 'B' | 'W';
POINT			: LETTER LETTER;

TEXT			: (~(']') | '\\]' | '\\:')*;
