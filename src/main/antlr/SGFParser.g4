parser grammar SGFParser;

options { tokenVocab=SGFLexer; }

collection		: gametree+;
gametree		: '(' sequence gametree* ')';
sequence		: node+;
node			: ';' property*;

property		: identifier value+;
identifier		: UPPERCASE+;
value			: '[' vtypecompose ']';

vtypecompose	:
				| numbertype
				| colortype
				| texttype
				| pointtype
				| compose;
numbertype		: NUMBER
				| REAL
				| DOUBLE
				;
colortype		: COLOR;
pointtype		: POINT;
texttype		: TEXT;
compose			: pointtype ':' pointtype;
