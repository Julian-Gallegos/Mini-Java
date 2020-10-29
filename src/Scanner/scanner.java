// DO NOT EDIT
// Generated by JFlex 1.8.2 http://jflex.de/
// source: src/Scanner/minijava.jflex

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


// See https://github.com/jflex-de/jflex/issues/222
@SuppressWarnings("FallThrough")
public final class scanner implements java_cup.runtime.Scanner {

  /** This character denotes the end of file. */
  public static final int YYEOF = -1;

  /** Initial size of the lookahead buffer. */
  private static final int ZZ_BUFFERSIZE = 16384;

  // Lexical states.
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = {
     0, 0
  };

  /**
   * Top-level table for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_TOP = zzUnpackcmap_top();

  private static final String ZZ_CMAP_TOP_PACKED_0 =
    "\1\0\37\u0100\1\u0200\267\u0100\10\u0300\u1020\u0100";

  private static int [] zzUnpackcmap_top() {
    int [] result = new int[4352];
    int offset = 0;
    offset = zzUnpackcmap_top(ZZ_CMAP_TOP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_top(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Second-level tables for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_BLOCKS = zzUnpackcmap_blocks();

  private static final String ZZ_CMAP_BLOCKS_PACKED_0 =
    "\11\0\1\1\1\2\2\3\1\2\22\0\1\1\1\4"+
    "\1\5\3\0\1\6\1\0\1\7\1\10\1\11\1\12"+
    "\1\13\1\14\1\15\1\16\12\17\1\0\1\20\1\21"+
    "\1\22\3\0\22\23\1\24\7\23\1\25\1\0\1\26"+
    "\1\0\1\27\1\0\1\30\1\31\1\32\1\33\1\34"+
    "\1\35\1\36\1\37\1\40\2\23\1\41\1\42\1\43"+
    "\1\44\1\45\1\23\1\46\1\47\1\50\1\51\1\52"+
    "\1\53\1\54\1\55\1\23\1\56\1\0\1\57\7\0"+
    "\1\3\u01a2\0\2\3\326\0\u0100\3";

  private static int [] zzUnpackcmap_blocks() {
    int [] result = new int[1024];
    int offset = 0;
    offset = zzUnpackcmap_blocks(ZZ_CMAP_BLOCKS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_blocks(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\1\1\4\1\5\1\6"+
    "\1\7\1\10\1\11\1\12\1\1\1\13\1\14\1\15"+
    "\1\16\2\17\1\20\1\21\16\17\1\22\1\23\1\24"+
    "\1\0\1\25\7\17\1\26\13\17\1\0\1\25\7\17"+
    "\1\27\2\17\1\30\7\17\1\0\1\31\4\17\1\32"+
    "\3\17\1\33\3\17\1\34\1\35\1\36\3\17\1\37"+
    "\2\17\1\40\4\17\1\41\2\17\1\0\1\17\1\42"+
    "\1\43\1\44\1\45\1\0\1\46\1\0\1\47\7\0"+
    "\1\50\6\0\1\51";

  private static int [] zzUnpackAction() {
    int [] result = new int[135];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\60\0\140\0\60\0\220\0\60\0\60\0\60"+
    "\0\60\0\60\0\60\0\60\0\300\0\360\0\60\0\60"+
    "\0\60\0\u0120\0\u0150\0\60\0\60\0\u0180\0\u01b0\0\u01e0"+
    "\0\u0210\0\u0240\0\u0270\0\u02a0\0\u02d0\0\u0300\0\u0330\0\u0360"+
    "\0\u0390\0\u03c0\0\u03f0\0\60\0\60\0\60\0\u0420\0\u0450"+
    "\0\u0480\0\u04b0\0\u04e0\0\u0510\0\u0540\0\u0570\0\u05a0\0\u0120"+
    "\0\u05d0\0\u0600\0\u0630\0\u0660\0\u0690\0\u06c0\0\u06f0\0\u0720"+
    "\0\u0750\0\u0780\0\u07b0\0\u07e0\0\60\0\u0810\0\u0840\0\u0870"+
    "\0\u08a0\0\u08d0\0\u0900\0\u0930\0\u0120\0\u0960\0\u0990\0\u0120"+
    "\0\u09c0\0\u09f0\0\u0a20\0\u0a50\0\u0a80\0\u0ab0\0\u0ae0\0\u0b10"+
    "\0\60\0\u0b40\0\u0b70\0\u0ba0\0\u0bd0\0\u0120\0\u0c00\0\u0c30"+
    "\0\u0c60\0\u0120\0\u0c90\0\u0cc0\0\u0cf0\0\u0120\0\u0120\0\u0120"+
    "\0\u0d20\0\u0d50\0\u0d80\0\u0120\0\u0db0\0\u0de0\0\u0120\0\u0e10"+
    "\0\u0e40\0\u0e70\0\u0ea0\0\u0120\0\u0ed0\0\u0f00\0\u0f30\0\u0f60"+
    "\0\u0120\0\u0120\0\u0120\0\u0120\0\u0f90\0\u0120\0\u0fc0\0\u0120"+
    "\0\u0ff0\0\u1020\0\u1050\0\u1080\0\u10b0\0\u10e0\0\u1110\0\u0f30"+
    "\0\u1140\0\u1170\0\u11a0\0\u11d0\0\u1200\0\u1230\0\60";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[135];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /**
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\2\3\1\0\1\4\1\2\1\5\1\6\1\7"+
    "\1\10\1\11\1\12\1\13\1\14\1\15\1\16\1\17"+
    "\1\20\1\21\1\22\1\23\1\24\1\25\1\2\1\22"+
    "\1\26\1\27\1\22\1\30\1\31\2\22\1\32\1\33"+
    "\1\34\1\35\1\22\1\36\1\37\1\40\1\41\1\22"+
    "\1\42\1\43\2\22\1\44\1\45\61\0\2\3\63\0"+
    "\1\46\62\0\1\47\4\0\1\50\60\0\1\16\57\0"+
    "\1\22\3\0\2\22\2\0\27\22\21\0\1\22\3\0"+
    "\2\22\2\0\26\22\1\51\21\0\1\22\3\0\2\22"+
    "\2\0\15\22\1\52\11\22\21\0\1\22\3\0\2\22"+
    "\2\0\12\22\1\53\2\22\1\54\11\22\21\0\1\22"+
    "\3\0\2\22\2\0\12\22\1\55\12\22\1\56\1\22"+
    "\21\0\1\22\3\0\2\22\2\0\1\22\1\57\25\22"+
    "\21\0\1\22\3\0\2\22\2\0\6\22\1\60\5\22"+
    "\1\61\12\22\21\0\1\22\3\0\2\22\2\0\5\22"+
    "\1\62\21\22\21\0\1\22\3\0\2\22\2\0\1\22"+
    "\1\63\25\22\21\0\1\22\3\0\2\22\2\0\5\22"+
    "\1\64\21\22\21\0\1\22\3\0\2\22\2\0\22\22"+
    "\1\65\4\22\21\0\1\22\3\0\2\22\2\0\5\22"+
    "\1\66\21\22\21\0\1\22\3\0\2\22\2\0\21\22"+
    "\1\67\5\22\21\0\1\22\3\0\2\22\2\0\10\22"+
    "\1\70\6\22\1\71\7\22\21\0\1\22\3\0\2\22"+
    "\2\0\15\22\1\72\11\22\21\0\1\22\3\0\2\22"+
    "\2\0\10\22\1\73\16\22\2\0\11\47\1\74\46\47"+
    "\2\50\1\75\55\50\17\0\1\22\3\0\2\22\2\0"+
    "\20\22\1\76\6\22\21\0\1\22\3\0\2\22\2\0"+
    "\15\22\1\77\11\22\21\0\1\22\3\0\2\22\2\0"+
    "\1\22\1\100\25\22\21\0\1\22\3\0\2\22\2\0"+
    "\13\22\1\101\13\22\21\0\1\22\3\0\2\22\2\0"+
    "\20\22\1\102\6\22\21\0\1\22\3\0\2\22\2\0"+
    "\21\22\1\103\5\22\21\0\1\22\3\0\2\22\2\0"+
    "\12\22\1\104\14\22\21\0\1\22\3\0\2\22\2\0"+
    "\21\22\1\105\5\22\21\0\1\22\3\0\2\22\2\0"+
    "\14\22\1\106\12\22\21\0\1\22\3\0\2\22\2\0"+
    "\11\22\1\107\15\22\21\0\1\22\3\0\2\22\2\0"+
    "\24\22\1\110\2\22\21\0\1\22\3\0\2\22\2\0"+
    "\2\22\1\111\24\22\21\0\1\22\3\0\2\22\2\0"+
    "\21\22\1\112\5\22\21\0\1\22\3\0\2\22\2\0"+
    "\1\22\1\113\25\22\21\0\1\22\3\0\2\22\2\0"+
    "\11\22\1\114\15\22\21\0\1\22\3\0\2\22\2\0"+
    "\22\22\1\115\4\22\21\0\1\22\3\0\2\22\2\0"+
    "\11\22\1\116\15\22\21\0\1\22\3\0\2\22\2\0"+
    "\11\22\1\117\15\22\2\0\11\47\1\120\4\47\1\121"+
    "\41\47\17\0\1\22\3\0\2\22\2\0\21\22\1\122"+
    "\5\22\21\0\1\22\3\0\2\22\2\0\12\22\1\123"+
    "\14\22\21\0\1\22\3\0\2\22\2\0\20\22\1\124"+
    "\6\22\21\0\1\22\3\0\2\22\2\0\13\22\1\125"+
    "\13\22\21\0\1\22\3\0\2\22\2\0\5\22\1\126"+
    "\21\22\21\0\1\22\3\0\2\22\2\0\5\22\1\127"+
    "\21\22\21\0\1\22\3\0\2\22\2\0\20\22\1\130"+
    "\6\22\21\0\1\22\3\0\2\22\2\0\7\22\1\131"+
    "\17\22\21\0\1\22\3\0\2\22\2\0\14\22\1\132"+
    "\12\22\21\0\1\22\3\0\2\22\2\0\12\22\1\133"+
    "\14\22\21\0\1\22\3\0\2\22\2\0\22\22\1\134"+
    "\4\22\21\0\1\22\3\0\2\22\2\0\21\22\1\135"+
    "\5\22\21\0\1\22\3\0\2\22\2\0\20\22\1\136"+
    "\6\22\21\0\1\22\3\0\2\22\2\0\5\22\1\137"+
    "\21\22\21\0\1\22\3\0\2\22\2\0\4\22\1\140"+
    "\22\22\21\0\1\22\3\0\2\22\2\0\12\22\1\141"+
    "\14\22\2\0\11\47\1\120\4\47\1\0\41\47\17\0"+
    "\1\22\3\0\2\22\2\0\5\22\1\142\21\22\21\0"+
    "\1\22\3\0\2\22\2\0\5\22\1\143\21\22\21\0"+
    "\1\22\3\0\2\22\2\0\20\22\1\144\6\22\21\0"+
    "\1\22\3\0\2\22\2\0\1\22\1\145\25\22\21\0"+
    "\1\22\3\0\2\22\2\0\14\22\1\146\12\22\21\0"+
    "\1\22\3\0\2\22\2\0\5\22\1\147\21\22\21\0"+
    "\1\22\3\0\2\22\2\0\21\22\1\150\5\22\21\0"+
    "\1\22\3\0\2\22\2\0\11\22\1\151\15\22\21\0"+
    "\1\22\3\0\2\22\2\0\17\22\1\152\7\22\21\0"+
    "\1\22\3\0\2\22\2\0\11\22\1\153\15\22\21\0"+
    "\1\22\3\0\2\22\2\0\5\22\1\154\21\22\21\0"+
    "\1\22\3\0\2\22\2\0\13\22\1\155\13\22\21\0"+
    "\1\22\3\0\2\22\2\0\1\22\1\156\25\22\2\0"+
    "\5\157\1\0\11\157\1\145\3\157\2\145\2\157\27\145"+
    "\2\157\17\0\1\22\3\0\2\22\2\0\4\22\1\160"+
    "\22\22\21\0\1\22\3\0\2\22\2\0\10\22\1\161"+
    "\16\22\21\0\1\22\3\0\2\22\2\0\3\22\1\162"+
    "\23\22\21\0\1\22\3\0\2\22\2\0\14\22\1\163"+
    "\12\22\21\0\1\22\3\0\2\22\2\0\3\22\1\164"+
    "\23\22\17\0\1\165\1\0\1\22\3\0\2\22\2\0"+
    "\27\22\21\0\1\22\3\0\2\22\2\0\14\22\1\166"+
    "\12\22\2\0\5\157\1\0\24\157\1\167\25\157\17\0"+
    "\1\22\3\0\2\22\2\0\20\22\1\170\6\22\46\0"+
    "\1\171\13\0\5\157\1\0\24\157\1\167\11\157\1\172"+
    "\13\157\51\0\1\173\6\0\5\157\1\0\24\157\1\167"+
    "\7\157\1\174\15\157\50\0\1\175\7\0\5\157\1\0"+
    "\24\157\1\167\7\157\1\176\15\157\15\0\1\177\42\0"+
    "\5\157\1\0\22\157\1\200\1\157\1\167\25\157\45\0"+
    "\1\201\60\0\1\202\51\0\1\203\62\0\1\204\64\0"+
    "\1\205\50\0\1\206\61\0\1\207\14\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[4704];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** Error code for "Unknown internal scanner error". */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  /** Error code for "could not match input". */
  private static final int ZZ_NO_MATCH = 1;
  /** Error code for "pushback value was too large". */
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /**
   * Error messages for {@link #ZZ_UNKNOWN_ERROR}, {@link #ZZ_NO_MATCH}, and
   * {@link #ZZ_PUSHBACK_2BIG} respectively.
   */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state {@code aState}
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\1\1\1\11\1\1\7\11\2\1\3\11"+
    "\2\1\2\11\16\1\3\11\1\0\24\1\1\0\1\11"+
    "\22\1\1\0\1\11\35\1\1\0\5\1\1\0\1\1"+
    "\1\0\1\1\7\0\1\1\6\0\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[135];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** Input device. */
  private java.io.Reader zzReader;

  /** Current state of the DFA. */
  private int zzState;

  /** Current lexical state. */
  private int zzLexicalState = YYINITIAL;

  /**
   * This buffer contains the current text to be matched and is the source of the {@link #yytext()}
   * string.
   */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** Text position at the last accepting state. */
  private int zzMarkedPos;

  /** Current text position in the buffer. */
  private int zzCurrentPos;

  /** Marks the beginning of the {@link #yytext()} string in the buffer. */
  private int zzStartRead;

  /** Marks the last character in the buffer, that has been read from input. */
  private int zzEndRead;

  /**
   * Whether the scanner is at the end of file.
   * @see #yyatEOF
   */
  private boolean zzAtEOF;

  /**
   * The number of occupied positions in {@link #zzBuffer} beyond {@link #zzEndRead}.
   *
   * <p>When a lead/high surrogate has been read from the input stream into the final
   * {@link #zzBuffer} position, this will have a value of 1; otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /** Number of newlines encountered up to the start of the matched text. */
  private int yyline;

  /** Number of characters from the last newline up to the start of the matched text. */
  private int yycolumn;

  /** Number of characters up to the start of the matched text. */
  @SuppressWarnings("unused")
  private long yychar;

  /** Whether the scanner is currently at the beginning of a line. */
  @SuppressWarnings("unused")
  private boolean zzAtBOL = true;

  /** Whether the user-EOF-code has already been executed. */
  private boolean zzEOFDone;

  /* user code: */
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


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public scanner(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Translates raw input code points to DFA table row
   */
  private static int zzCMap(int input) {
    int offset = input & 255;
    return offset == input ? ZZ_CMAP_BLOCKS[offset] : ZZ_CMAP_BLOCKS[ZZ_CMAP_TOP[input >> 8] | offset];
  }

  /**
   * Refills the input buffer.
   *
   * @return {@code false} iff there was new input.
   * @exception java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead - zzStartRead);

      /* translate stored positions */
      zzEndRead -= zzStartRead;
      zzCurrentPos -= zzStartRead;
      zzMarkedPos -= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length * 2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException(
          "Reader returned 0 characters. See JFlex examples/zero-reader for a workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
        if (numRead == requested) { // We requested too few chars to encode a full Unicode character
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        } else {                    // There is room in the buffer for at least one more char
          int c = zzReader.read();  // Expecting to read a paired low surrogate char
          if (c == -1) {
            return true;
          } else {
            zzBuffer[zzEndRead++] = (char)c;
          }
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }


  /**
   * Closes the input reader.
   *
   * @throws java.io.IOException if the reader could not be closed.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true; // indicate end of file
    zzEndRead = zzStartRead; // invalidate buffer

    if (zzReader != null) {
      zzReader.close();
    }
  }


  /**
   * Resets the scanner to read from a new input stream.
   *
   * <p>Does not close the old reader.
   *
   * <p>All internal variables are reset, the old input stream <b>cannot</b> be reused (internal
   * buffer is discarded and lost). Lexical state is set to {@code ZZ_INITIAL}.
   *
   * <p>Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader The new input stream.
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzEOFDone = false;
    yyResetPosition();
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE) {
      zzBuffer = new char[ZZ_BUFFERSIZE];
    }
  }

  /**
   * Resets the input position.
   */
  private final void yyResetPosition() {
      zzAtBOL  = true;
      zzAtEOF  = false;
      zzCurrentPos = 0;
      zzMarkedPos = 0;
      zzStartRead = 0;
      zzEndRead = 0;
      zzFinalHighSurrogate = 0;
      yyline = 0;
      yycolumn = 0;
      yychar = 0L;
  }


  /**
   * Returns whether the scanner has reached the end of the reader it reads from.
   *
   * @return whether the scanner has reached EOF.
   */
  public final boolean yyatEOF() {
    return zzAtEOF;
  }


  /**
   * Returns the current lexical state.
   *
   * @return the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state.
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   *
   * @return the matched text.
   */
  public final String yytext() {
    return new String(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead);
  }


  /**
   * Returns the character at the given position from the matched text.
   *
   * <p>It is equivalent to {@code yytext().charAt(pos)}, but faster.
   *
   * @param position the position of the character to fetch. A value from 0 to {@code yylength()-1}.
   *
   * @return the character at {@code position}.
   */
  public final char yycharat(int position) {
    return zzBuffer[zzStartRead + position];
  }


  /**
   * How many characters were matched.
   *
   * @return the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * <p>In a well-formed scanner (no or only correct usage of {@code yypushback(int)} and a
   * match-all fallback rule) this method will only be called with things that
   * "Can't Possibly Happen".
   *
   * <p>If this method is called, something is seriously wrong (e.g. a JFlex bug producing a faulty
   * scanner etc.).
   *
   * <p>Usual syntax/scanner level error handling should be done in error fallback rules.
   *
   * @param errorCode the code of the error message to display.
   */
  private static void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    } catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * <p>They will be read again by then next call of the scanning method.
   *
   * @param number the number of characters to be read again. This number must not be greater than
   *     {@link #yylength()}.
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
  yyclose();    }
  }




  /**
   * Resumes scanning until the next regular expression is matched, the end of input is encountered
   * or an I/O-Error occurs.
   *
   * @return the next token.
   * @exception java.io.IOException if any I/O-Error occurs.
   */
  @Override  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char[] zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':  // fall through
        case '\u000C':  // fall through
        case '\u0085':  // fall through
        case '\u2028':  // fall through
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is
        // (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof)
            zzPeek = false;
          else
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMap(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
              {
                return symbol(sym.EOF);
              }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1:
            { System.err.printf(
      "%nUnexpected character '%s' on line %d at column %d of input.%n",
      yytext(), yyline + 1, yycolumn + 1
    );
    return symbol(sym.error, yytext());
            }
            // fall through
          case 42: break;
          case 2:
            { /* ignore whitespace */
            }
            // fall through
          case 43: break;
          case 3:
            { return symbol(sym.NOT);
            }
            // fall through
          case 44: break;
          case 4:
            { return symbol(sym.LPAREN);
            }
            // fall through
          case 45: break;
          case 5:
            { return symbol(sym.RPAREN);
            }
            // fall through
          case 46: break;
          case 6:
            { return symbol(sym.TIMES);
            }
            // fall through
          case 47: break;
          case 7:
            { return symbol(sym.PLUS);
            }
            // fall through
          case 48: break;
          case 8:
            { return symbol(sym.COMMA);
            }
            // fall through
          case 49: break;
          case 9:
            { return symbol(sym.MINUS);
            }
            // fall through
          case 50: break;
          case 10:
            { return symbol(sym.DOT);
            }
            // fall through
          case 51: break;
          case 11:
            { return symbol(sym.DIGIT, yytext());
            }
            // fall through
          case 52: break;
          case 12:
            { return symbol(sym.SEMICOLON);
            }
            // fall through
          case 53: break;
          case 13:
            { return symbol(sym.LTHAN);
            }
            // fall through
          case 54: break;
          case 14:
            { return symbol(sym.BECOMES);
            }
            // fall through
          case 55: break;
          case 15:
            { return symbol(sym.IDENTIFIER, yytext());
            }
            // fall through
          case 56: break;
          case 16:
            { return symbol(sym.LBRACKET);
            }
            // fall through
          case 57: break;
          case 17:
            { return symbol(sym.RBRACKET);
            }
            // fall through
          case 58: break;
          case 18:
            { return symbol(sym.LBRACE);
            }
            // fall through
          case 59: break;
          case 19:
            { return symbol(sym.RBRACE);
            }
            // fall through
          case 60: break;
          case 20:
            { return symbol(sym.AND);
            }
            // fall through
          case 61: break;
          case 21:
            { /* ignore single line comments */
            }
            // fall through
          case 62: break;
          case 22:
            { return symbol(sym.IF);
            }
            // fall through
          case 63: break;
          case 23:
            { return symbol(sym.INT);
            }
            // fall through
          case 64: break;
          case 24:
            { return symbol(sym.NEW);
            }
            // fall through
          case 65: break;
          case 25:
            { /* ignore multi line comments */
            }
            // fall through
          case 66: break;
          case 26:
            { return symbol(sym.ELSE);
            }
            // fall through
          case 67: break;
          case 27:
            { return symbol(sym.MAIN);
            }
            // fall through
          case 68: break;
          case 28:
            { return symbol(sym.THIS);
            }
            // fall through
          case 69: break;
          case 29:
            { return symbol(sym.TRUE);
            }
            // fall through
          case 70: break;
          case 30:
            { return symbol(sym.VOID);
            }
            // fall through
          case 71: break;
          case 31:
            { return symbol(sym.CLASS);
            }
            // fall through
          case 72: break;
          case 32:
            { return symbol(sym.FALSE);
            }
            // fall through
          case 73: break;
          case 33:
            { return symbol(sym.WHILE);
            }
            // fall through
          case 74: break;
          case 34:
            { return symbol(sym.LENGTH);
            }
            // fall through
          case 75: break;
          case 35:
            { return symbol(sym.PUBLIC);
            }
            // fall through
          case 76: break;
          case 36:
            { return symbol(sym.RETURN);
            }
            // fall through
          case 77: break;
          case 37:
            { return symbol(sym.STATIC);
            }
            // fall through
          case 78: break;
          case 38:
            { return symbol(sym.BOOL);
            }
            // fall through
          case 79: break;
          case 39:
            { return symbol(sym.EXTENDS);
            }
            // fall through
          case 80: break;
          case 40:
            { return symbol(sym.TEXT, yytext());
            }
            // fall through
          case 81: break;
          case 41:
            { return symbol(sym.STDOUT);
            }
            // fall through
          case 82: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
