/* Generated By:JJTree: Do not edit this line. ASTassignmentExpression.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javascriptInterpreter.tree;

import javascriptInterpreter.parser.*;

public
class ASTassignmentExpression extends SimpleNode {
  public ASTassignmentExpression(int id) {
    super(id);
  }

  public ASTassignmentExpression(Javascript p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(JavascriptVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2e7f5b9a66d1932fd7dd62f96b0fb9f2 (do not edit this line) */