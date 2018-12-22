package javascriptInterpreter.visitors;

import javascriptInterpreter.tree.*;

public class SemanticCheckVisitor extends JavascriptDefaultVisitor {

    public Object visit(ASTexpressionStatement node, Object data){
        node.dump(">");
        return defaultVisit(node, data);
    }

}
