/* Generated By:JJTree&JavaCC: Do not edit this line. JavascriptConstants.java */
package javascriptInterpreter.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface JavascriptConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 4;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 6;
  /** RegularExpression Id. */
  int WHITE_SPACE = 7;
  /** RegularExpression Id. */
  int LINE_TERMINATOR = 8;
  /** RegularExpression Id. */
  int RESERVED = 9;
  /** RegularExpression Id. */
  int FUTURE_RESERVED = 10;
  /** RegularExpression Id. */
  int BREAK = 11;
  /** RegularExpression Id. */
  int CASE = 12;
  /** RegularExpression Id. */
  int CATCH = 13;
  /** RegularExpression Id. */
  int CONTINUE = 14;
  /** RegularExpression Id. */
  int DFLT = 15;
  /** RegularExpression Id. */
  int DELETE = 16;
  /** RegularExpression Id. */
  int DO = 17;
  /** RegularExpression Id. */
  int ELSE = 18;
  /** RegularExpression Id. */
  int FINALLY = 19;
  /** RegularExpression Id. */
  int FOR = 20;
  /** RegularExpression Id. */
  int FUNCTION = 21;
  /** RegularExpression Id. */
  int IF = 22;
  /** RegularExpression Id. */
  int IN = 23;
  /** RegularExpression Id. */
  int INSTANCEOF = 24;
  /** RegularExpression Id. */
  int NEW = 25;
  /** RegularExpression Id. */
  int RETURN = 26;
  /** RegularExpression Id. */
  int SWITCH = 27;
  /** RegularExpression Id. */
  int THIS = 28;
  /** RegularExpression Id. */
  int THROW = 29;
  /** RegularExpression Id. */
  int TRY = 30;
  /** RegularExpression Id. */
  int TYPEOF = 31;
  /** RegularExpression Id. */
  int VAR = 32;
  /** RegularExpression Id. */
  int VOID = 33;
  /** RegularExpression Id. */
  int WHILE = 34;
  /** RegularExpression Id. */
  int NULL_LITERAL = 35;
  /** RegularExpression Id. */
  int BOOLEAN_LITERAL = 36;
  /** RegularExpression Id. */
  int STRING_LITERAL = 37;
  /** RegularExpression Id. */
  int DOUBLE_QUOTES_STRING = 38;
  /** RegularExpression Id. */
  int SINGLE_QUOTES_STRING = 39;
  /** RegularExpression Id. */
  int NUMERIC_LITERAL = 40;
  /** RegularExpression Id. */
  int DECIMAL_LITERAL = 41;
  /** RegularExpression Id. */
  int EXPONENT_PART = 42;
  /** RegularExpression Id. */
  int DECIMAL_INTEGER_LITERAL = 43;
  /** RegularExpression Id. */
  int BINARY_INTEGER_LITERAL = 44;
  /** RegularExpression Id. */
  int OCTAL_INTEGER_LITERAL = 45;
  /** RegularExpression Id. */
  int HEX_INTEGER_LITERAL = 46;
  /** RegularExpression Id. */
  int NON_ZERO_DIGIT = 47;
  /** RegularExpression Id. */
  int DECIMAL_DIGITS = 48;
  /** RegularExpression Id. */
  int BINARY_DIGITS = 49;
  /** RegularExpression Id. */
  int OCTAL_DIGITS = 50;
  /** RegularExpression Id. */
  int HEX_DIGITS = 51;
  /** RegularExpression Id. */
  int DECIMAL_DIGIT = 52;
  /** RegularExpression Id. */
  int BINARY_DIGIT = 53;
  /** RegularExpression Id. */
  int OCTAL_DIGIT = 54;
  /** RegularExpression Id. */
  int HEX_DIGIT = 55;
  /** RegularExpression Id. */
  int IDENTIFIER_NAME = 56;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_MULTI_LINE_COMMENT = 1;
  /** Lexical state. */
  int IN_SINGLE_LINE_COMMENT = 2;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"/*\"",
    "\"//\"",
    "<token of kind 3>",
    "\"*/\"",
    "<token of kind 5>",
    "<SINGLE_LINE_COMMENT>",
    "<WHITE_SPACE>",
    "<LINE_TERMINATOR>",
    "<RESERVED>",
    "\"enum\"",
    "\"break\"",
    "\"case\"",
    "\"catch\"",
    "\"continue\"",
    "\"default\"",
    "\"delete\"",
    "\"do\"",
    "\"else\"",
    "\"finally\"",
    "\"for\"",
    "\"function\"",
    "\"if\"",
    "\"in\"",
    "\"instanceof\"",
    "\"new\"",
    "\"return\"",
    "\"switch\"",
    "\"this\"",
    "\"throw\"",
    "\"try\"",
    "\"typeof\"",
    "\"var\"",
    "\"void\"",
    "\"while\"",
    "\"null\"",
    "<BOOLEAN_LITERAL>",
    "<STRING_LITERAL>",
    "<DOUBLE_QUOTES_STRING>",
    "<SINGLE_QUOTES_STRING>",
    "<NUMERIC_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<EXPONENT_PART>",
    "<DECIMAL_INTEGER_LITERAL>",
    "<BINARY_INTEGER_LITERAL>",
    "<OCTAL_INTEGER_LITERAL>",
    "<HEX_INTEGER_LITERAL>",
    "<NON_ZERO_DIGIT>",
    "<DECIMAL_DIGITS>",
    "<BINARY_DIGITS>",
    "<OCTAL_DIGITS>",
    "<HEX_DIGITS>",
    "<DECIMAL_DIGIT>",
    "<BINARY_DIGIT>",
    "<OCTAL_DIGIT>",
    "<HEX_DIGIT>",
    "<IDENTIFIER_NAME>",
    "\"[\"",
    "\"]\"",
    "\",\"",
    "\"...\"",
    "\"{\"",
    "\"}\"",
    "\":\"",
    "\"(\"",
    "\")\"",
    "\".\"",
    "\"++\"",
    "\"--\"",
    "\"~\"",
    "\"!\"",
    "\"-\"",
    "\"+\"",
    "\"**\"",
    "\"*\"",
    "\"/\"",
    "\"%\"",
    "\"<<\"",
    "\">>\"",
    "\">>>\"",
    "\"<\"",
    "\">\"",
    "\"<=\"",
    "\">=\"",
    "\"==\"",
    "\"!=\"",
    "\"===\"",
    "\"!==\"",
    "\"&\"",
    "\"^\"",
    "\"|\"",
    "\"&&\"",
    "\"||\"",
    "\"?\"",
    "\"=\"",
    "\"*=\"",
    "\"/=\"",
    "\"+=\"",
    "\"-=\"",
    "\"%=\"",
    "\"<<=\"",
    "\">>=\"",
    "\">>>=\"",
    "\"&=\"",
    "\"^=\"",
    "\"|=\"",
    "\"**=\"",
    "\";\"",
  };

}
