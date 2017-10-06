parser grammar SGFParser;

options { tokenVocab=SGFLexer; }

collection		: gametree+;
gametree		: '(' sequence gametree* ')';
sequence		: node+;
node			: ';' property*;

property		: identifier value+;
identifier		: IDENTIFIER;
value			: '[' valueornull ']';

valueornull 	:                      #novalue
				| VALUE                #hasvalue
                ;
