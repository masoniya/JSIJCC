package javascriptInterpreter.visitors;

import javascriptInterpreter.tree.*;

import static javascriptInterpreter.visitors.JSToJavaUtils.*;

public class DeclarationVisitor extends JavascriptDefaultVisitor {

    public Object visit(ASTvariableDefinition node, Context data){
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTvariableDeclarationList node, Context data){
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTvariableDeclaration node, Context data){
        String identifierName = node.jjtGetFirstToken().image;
        JavascriptType identifierValue = new JavascriptType();

        if(node.jjtGetNumChildren() > 0){
            ASTassignmentExpression valueNode = (ASTassignmentExpression) node.jjtGetChild(0);
            EvaluationVisitor v = new EvaluationVisitor();
            identifierValue = (JavascriptType)valueNode.jjtAccept(v, data);
        }
        System.out.println("adding identifier : " + identifierName);
        data.addIdentifier(identifierName, identifierValue);

        return defaultVisit(node, data);
    }
}
