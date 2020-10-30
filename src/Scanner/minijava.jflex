/**
 * JFlex specification for lexical analysis of a simple demo language.
 * Change this into the scanner for your implementation of MiniJava.
 *
 *
 * CSE 401/M501/P501 19au, 20sp, 20au
 */


package Scanner;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Parser.sym;

%%
%public
%final
%class scanner
%unicode
%cup
%line
%column

/* The following code block is copied literally into the generated scanner
 * class. You can use this to define methods and/or declare fields of the
 * scanner class, which the lexical actions may also reference. Most likely,
 * you will only need to tweak what's already provided below.
 *
 * We use CUP's ComplexSymbolFactory and its associated ComplexSymbol class
 * that tracks the source location of each scanned symbol.
 */
%{
  /** The CUP symbol factory, typically shared with parser. */
  private ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();

  /** Initialize scanner with input stream and a shared symbol factory. */
  public scanner(java.io.Reader in, ComplexSymbolFactory sf) {
    this(in);
    this.symbolFactory = sf;
  }

  /**
   * Construct a symbol with a given lexical token, a given
   * user-controlled datum, and the matched source location.
   *
   * @param code     identifier of the lexical token (i.e., sym.<TOKEN>)
   * @param value    user-controlled datum to associate with this symbol
   * @effects        constructs new ComplexSymbol via this.symbolFactory
   * @return         a fresh symbol storing the above-desribed information
   */
  private Symbol symbol(int code, Object value) {
    // Calculate symbol location
    int yylen = yylength();
    Location left = new Location(yyline + 1, yycolumn + 1);
    Location right = new Location(yyline + 1, yycolumn + yylen);
    // Calculate symbol name
    int max_code = sym.terminalNames.length;
    String name = code < max_code ? sym.terminalNames[code] : "<UNKNOWN(" + yytext() + ")>";
    return this.symbolFactory.newSymbol(name, code, left, right, value);
  }

  /**
   * Construct a symbol with a given lexical token and matched source
   * location, leaving the user-controlled value field uninitialized.
   *
   * @param code     identifier of the lexical token (i.e., sym.<TOKEN>)
   * @effects        constructs new ComplexSymbol via this.symbolFactory
   * @return         a fresh symbol storing the above-desribed information
   */
  private Symbol symbol(int code) {
    // Calculate symbol location
    int yylen = yylength();
    Location left = new Location(yyline + 1, yycolumn + 1);
    Location right = new Location(yyline + 1, yycolumn + yylen);
    // Calculate symbol name
    int max_code = sym.terminalNames.length;
    String name = code < max_code ? sym.terminalNames[code] : "<UNKNOWN(" + yytext() + ")>";
    return this.symbolFactory.newSymbol(name, code, left, right);
  }

  /**
   * Convert the symbol generated by this scanner into a string.
   *
   * This method is useful to include information in the string representation
   * in addition to the plain CUP name for a lexical token.
   *
   * @param symbol   symbol instance generated by this scanner
   * @return         string representation of the symbol
   */
   public String symbolToString(Symbol s) {
     // All symbols generated by this class are ComplexSymbol instances
     ComplexSymbol cs = (ComplexSymbol)s; 
     if (cs.sym == sym.IDENTIFIER) {
       return "ID(" + (String)cs.value + ")";
     } else if (cs.sym == sym.DIGIT) {
       return "DIGIT(" + (String)cs.value + ")";
     } else if (cs.sym == sym.BECOMES) {
       return "BECOMES";
     } else if (cs.sym == sym.PLUS) {
       return "PLUS";
     /*
     } else if (cs.sym == sym.DISPLAY) {
       return "DISPLAY";
     */
     } else if (cs.sym == sym.MINUS) {
       return "MINUS";
     } else if (cs.sym == sym.RETURN) {
       return "RETURN";
     } else if (cs.sym == sym.LPAREN) {
       return "LPAREN";
     } else if (cs.sym == sym.RPAREN) {
       return "RPAREN";
     } else if (cs.sym == sym.LBRACKET) { 
       return "LBRACKET";
     } else if (cs.sym == sym.RBRACKET) {
       return "RBRACKET";
     } else if (cs.sym == sym.AND) {
       return "AND";
     } else if (cs.sym == sym.LTHAN) {
       return "LTHAN";
     } else if (cs.sym == sym.TIMES) { 
       return "TIMES";
     } else if (cs.sym == sym.LBRACKET) {
       return "LBRACKET";
     } else if (cs.sym == sym.RBRACKET) { 
       return "RBRACKET";
     } else if (cs.sym == sym.LBRACE) {
       return "LBRACE";
     } else if (cs.sym == sym.RBRACE) {
       return "RBRACE";
     } else if (cs.sym == sym.NOT) {
       return "NOT";
     } else if (cs.sym == sym.IF) { 
       return "IF";
     } else if (cs.sym == sym.ELSE) {
       return "ELSE";
     } else if (cs.sym == sym.WHILE) {
       return "WHILE";
     } else if (cs.sym == sym.STDOUT) {
       return "STDOUT";
     } else if (cs.sym == sym.STRING) { 
       return "STRING";
     } else if (cs.sym == sym.INT) {
       return "INT";
     } else if (cs.sym == sym.BOOL) {
       return "BOOL";
     } else if (cs.sym == sym.TRUE) { 
       return "TRUE"; 
     } else if (cs.sym == sym.FALSE) {
       return "FALSE";
     } else if (cs.sym == sym.THIS) {
       return "THIS";
     } else if (cs.sym == sym.NEW) {
       return "NEW";
     } else if (cs.sym == sym.PUBLIC) {
       return "PUBLIC";
     } else if (cs.sym == sym.STATIC) {
       return "STATIC";
     } else if (cs.sym == sym.VOID) {
       return "VOID";
     } else if (cs.sym == sym.MAIN) {
       return "MAIN";
     } else if (cs.sym == sym.CLASS) {
       return "CLASS";
     } else if (cs.sym == sym.EXTENDS) {
       return "EXTENDS";
     } else if (cs.sym == sym.SEMICOLON) {
       return "SEMICOLON";
     } else if (cs.sym == sym.COMMA) {
       return "COMMA";
     } else if (cs.sym == sym.DOT) {
       return "DOT";
     } else if (cs.sym == sym.LENGTH) {
       return "LENGTH";
     } else if (cs.sym == sym.TEXT) {
       return "TEXT(" + (String)cs.value + ")";
     } else if (cs.sym == sym.error) {
       return "<UNEXPECTED(" + (String)cs.value + ")>";
     } else {
       return cs.getName();
     }
   }
%}

/* Helper definitions */
letter = [a-zA-Z]
digit = [0-9]
eol = [\r\n]
white = {eol}|[ \t]
norn = [a-m]|[o-q]|[s-z]|[A-Z]
nofwdslashstar = {letter}|{digit}|_|\| |{eol}
everythingButEOL = [^\r\n]
multilinehelper = [^*]|"*"+[^*/]
comma = "\""

%%

/* Token definitions */

/* reserved words (first so that they take precedence over identifiers) */
//"display" { return symbol(sym.DISPLAY); }
"return" { return symbol(sym.RETURN); }
"if" { return symbol(sym.IF); }
"else" { return symbol(sym.ELSE); }
"while" { return symbol(sym.WHILE); }
"System.out.println" { return symbol(sym.STDOUT); }
"int" { return symbol(sym.INT); }
"boolean" { return symbol(sym.BOOL); }
"true" { return symbol(sym.TRUE); }
"false" { return symbol(sym.FALSE); }
"this" { return symbol(sym.THIS); }
"new" { return symbol(sym.NEW); }
"public" { return symbol(sym.PUBLIC); }
"static" { return symbol(sym.STATIC); }
"void" { return symbol(sym.VOID); }
"main" { return symbol(sym.MAIN); }
"class" { return symbol(sym.CLASS); }
"extends" { return symbol(sym.EXTENDS); }
"length" { return symbol(sym.LENGTH); }


/* operators */
"+" { return symbol(sym.PLUS); }
"=" { return symbol(sym.BECOMES); }
"&&" { return symbol(sym.AND); }
"<" { return symbol(sym.LTHAN); }
"-" { return symbol(sym.MINUS); }
"*" { return symbol(sym.TIMES); }
"," { return symbol(sym.COMMA); }
"." { return symbol(sym.DOT); }
"!" { return symbol(sym.NOT); }


/* delimiters */
"(" { return symbol(sym.LPAREN); }
")" { return symbol(sym.RPAREN); }
"{" { return symbol(sym.LBRACE); }
"}" { return symbol(sym.RBRACE); }
";" { return symbol(sym.SEMICOLON); }
"[" { return symbol(sym.LBRACKET); }
"]" { return symbol(sym.RBRACKET); }

/* identifiers */
{letter} ({letter}|{digit}|_)* {
  return symbol(sym.IDENTIFIER, yytext());
}

/* quotes */
comma [^\"]* comma {
  return symbol(sym.TEXT, yytext());
}

/* digits */
{digit}+ {
  return symbol(sym.DIGIT, yytext()); 
}

/* whitespace */
{white}+ { /* ignore whitespace */ }

/* multi line comment */
"/*" {multilinehelper}*"*/" { /* ignore multi line comments */ }

/* single line comment */
"//" {everythingButEOL}*{eol}? { /* ignore single line comments */ }



/* lexical errors (last so other matches take precedence) */
. {
    System.err.printf(
      "%nUnexpected character '%s' on line %d at column %d of input.%n",
      yytext(), yyline + 1, yycolumn + 1
    );
    return symbol(sym.error, yytext());
  }

<<EOF>> { return symbol(sym.EOF); }
