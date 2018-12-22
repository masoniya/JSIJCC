package javascriptInterpreter.visitors;

import javascriptInterpreter.tree.*;

public class ExecutionVisitor extends JavascriptDefaultVisitor {

    public Object visit(ASTexpression node, Object data){
        EvaluationVisitor v = new EvaluationVisitor();
        return v.visit(node, data);
    }


}
