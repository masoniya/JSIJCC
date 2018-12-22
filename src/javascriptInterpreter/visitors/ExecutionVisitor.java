package javascriptInterpreter.visitors;

import core.Main;
import javascriptInterpreter.tree.*;

import static javascriptInterpreter.tree.JavascriptTreeConstants.*;

public class ExecutionVisitor extends JavascriptDefaultVisitor {





    public Object visit(ASTstatement node, Object data){
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTemptyStatement node, Object data){
        return null;
    }



    public Object visit(ASTblock node, Object data){
        if(node.jjtGetNumChildren() > 0){
            node.childrenAccept(this, data);
        }
        return null;
    }



    public Object visit(ASTifStatement node, Object data){
        ASTexpression condition = (ASTexpression)node.jjtGetChild(0);
        EvaluationVisitor v = new EvaluationVisitor();
        double x = (double)condition.jjtAccept(v, data);
        boolean conditionResult = JSToJavaUtils.doubleToBoolean(x);

        //has no else
        if(node.jjtGetNumChildren() == 2){
            if(conditionResult){
                node.jjtGetChild(1).jjtAccept(this, data);
            }
        }
        //has else
        else{
            if(conditionResult){
                node.jjtGetChild(1).jjtAccept(this, data);
            }
            else{
                node.jjtGetChild(2).jjtAccept(this, data);
            }
        }
        return null;
    }

    public Object visit(ASTswitchStatement node, Object data){
        ASTexpression expression = (ASTexpression)node.jjtGetChild(0);
        EvaluationVisitor v = new EvaluationVisitor();
        double x = (double)expression.jjtAccept(v, data);
        int expressionEvaluation = (int)x;

        //TODO switch statement implementation

        return null;
    }

    public Object visit(ASTdoStatement node, Object data){
        ASTexpression condition = (ASTexpression)node.jjtGetChild(1);
        EvaluationVisitor v = new EvaluationVisitor();
        double x;
        boolean conditionResult;

        do{
            node.jjtGetChild(0).jjtAccept(this, data);

            //update condition
            x = (double)condition.jjtAccept(v, data);
            conditionResult = JSToJavaUtils.doubleToBoolean(x);
        }
        while(conditionResult);

        return null;
    }

    public Object visit(ASTwhileStatement node, Object data){
        ASTexpression condition = (ASTexpression)node.jjtGetChild(0);
        EvaluationVisitor v = new EvaluationVisitor();
        double x = (double)condition.jjtAccept(v, data);
        boolean conditionResult = JSToJavaUtils.doubleToBoolean(x);

        while(Main.loopTicks++ < 10){
            node.jjtGetChild(1).jjtAccept(this, data);

            //update condition
            x = (double)condition.jjtAccept(v, data);
            conditionResult = JSToJavaUtils.doubleToBoolean(x);
        }

        return null;
    }


    //this hasn't been finished yet
    public Object visit(ASTforStatement node, Object data){

        switch(node.jjtGetChild(0).getId()){
            case JJTFORHEADER :
                ASTforHeader forHeader = (ASTforHeader)node.jjtGetChild(0);

                if(forHeader.jjtGetNumChildren() == 0){
                    //infinite loop
                    for(;;){
                        node.jjtGetChild(1).jjtAccept(this, data);
                    }
                }

                ASTforInitializer first = null;
                ASTexpression second = null;
                ASTexpression third = null;

                if(forHeader.jjtGetNumChildren() == 3){
                    first = (ASTforInitializer) forHeader.jjtGetChild(0);
                    second = (ASTexpression) forHeader.jjtGetChild(1);
                    third = (ASTexpression) forHeader.jjtGetChild(2);
                }
                else{
                    if(forHeader.jjtGetChild(0).getId() == JJTFORINITIALIZER){
                        first = (ASTforInitializer) forHeader.jjtGetChild(0);
                    }

                }




                //execute initializer
                forHeader.jjtGetChild(0).jjtAccept(this, data);

                //evaluate condition
                EvaluationVisitor v = new EvaluationVisitor();
                ASTexpression condition = (ASTexpression)forHeader.jjtGetChild(1);
                double x = (double)condition.jjtAccept(v, data);
                boolean conditionResult = JSToJavaUtils.doubleToBoolean(x);

                for(;conditionResult;){
                    //execute loop body
                    node.jjtGetChild(1).jjtAccept(this, data);

                    //execute loop last expression
                    forHeader.jjtGetChild(2).jjtAccept(v, data);
                }

                break;
            case JJTFORINHEADER :
                ASTforInHeader forInHeader = (ASTforInHeader)node.jjtGetChild(0);
                //TODO implement for in loop
                break;
        }
        return null;
    }







    public Object visit(ASTexpressionStatement node, Object data){
        EvaluationVisitor v = new EvaluationVisitor();
        return node.jjtGetChild(0).jjtAccept(v, data);
    }





    public Object visit(ASTtopStatement node, Object data){
        node.childrenAccept(this, data);
        return null;
    }
    public Object visit(ASTtopStatements node, Object data){
        node.childrenAccept(this, data);
        return null;
    }
    public Object visit(ASTprogram node, Object data){
        node.childrenAccept(this, data);
        return 0;
    }
}
