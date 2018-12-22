package javascriptInterpreter.visitors;

import javascriptInterpreter.tree.*;

import static javascriptInterpreter.visitors.JSToJavaUtils.*;

public class DeclarationVisitor extends JavascriptDefaultVisitor {

    public Object visit(ASTvariableDefinition node, Object data){
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTvariableDeclarationList node, Object data){
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTvariableDeclaration node, Object data){

        return defaultVisit(node, data);
    }
}
