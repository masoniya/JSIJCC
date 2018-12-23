package javascriptInterpreter.visitors;

import core.Main;
import javascriptInterpreter.tree.*;

import static javascriptInterpreter.tree.JavascriptTreeConstants.*;

public class ExecutionVisitor extends JavascriptDefaultVisitor {



    public JavascriptType visit(ASTstatement node, Context data){
        node.childrenAccept(this, data);
        return null;
    }

    public JavascriptType visit(ASTemptyStatement node, Context data){
        return null;
    }

    public JavascriptType visit(ASTblock node, Context data){
        if(node.jjtGetNumChildren() > 0){
            node.childrenAccept(this, data);
        }
        return null;
    }

    public JavascriptType visit(ASTvariableDefinition node, Context data){
        DeclarationVisitor v = new DeclarationVisitor();
        return node.jjtAccept(v, data);
    }



    public JavascriptType visit(ASTifStatement node, Context data){
        ASTexpression condition = (ASTexpression)node.jjtGetChild(0);
        EvaluationVisitor v = new EvaluationVisitor();
        double x = condition.jjtAccept(v, data).getDouble();
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

    @SuppressWarnings("Duplicates")
    public JavascriptType visit(ASTswitchStatement node, Context data){

        ASTcaseBlock caseBlock = (ASTcaseBlock) node.jjtGetChild(1);
        boolean matchedCase = false;

        ASTcaseClauses first = null;
        ASTdefaultClause second = null;
        ASTcaseClauses third = null;

        //empty switch statement
        if(caseBlock.jjtGetNumChildren() == 0){
            return null;
        }

        else if(caseBlock.jjtGetNumChildren() == 1){
            //only default clause
            if(caseBlock.jjtGetChild(0).getId() == JJTDEFAULTCLAUSE){
                second = (ASTdefaultClause) caseBlock.jjtGetChild(0);
            }
            //no default clause
            else{
                first = (ASTcaseClauses) caseBlock.jjtGetChild(0);
            }
        }

        else if(caseBlock.jjtGetNumChildren() == 2){
            //default before cases
            if(caseBlock.jjtGetChild(0).getId() == JJTDEFAULTCLAUSE){
                second = (ASTdefaultClause) caseBlock.jjtGetChild(0);
                third = (ASTcaseClauses) caseBlock.jjtGetChild(1);
            }
            //default after cases
            else{
                first = (ASTcaseClauses) caseBlock.jjtGetChild(0);
                second = (ASTdefaultClause) caseBlock.jjtGetChild(1);
            }
        }

        //cases before and after default
        else{
            first = (ASTcaseClauses) caseBlock.jjtGetChild(0);
            second = (ASTdefaultClause) caseBlock.jjtGetChild(1);
            third = (ASTcaseClauses) caseBlock.jjtGetChild(2);
        }

        ASTexpression expression = (ASTexpression)node.jjtGetChild(0);
        EvaluationVisitor v = new EvaluationVisitor();
        double x = expression.jjtAccept(v, data).getDouble();
        int expressionEvaluation = (int)x;


        if(first != null){
            for(int i = 0; i < first.jjtGetNumChildren(); i++){
                ASTcaseClause caseClause = (ASTcaseClause)first.jjtGetChild(i);
                double y = caseClause.jjtGetChild(0).jjtAccept(v, data).getDouble();
                int caseEvaluation = (int)y;
                //fallthrough
                if(matchedCase){
                    caseClause.jjtGetChild(1).jjtAccept(this, data);
                }
                else{
                    if(expressionEvaluation == caseEvaluation){
                        matchedCase = true;
                        caseClause.jjtGetChild(1).jjtAccept(this, data);
                    }
                }
            }
        }

        //execute default if there is fallthrough
        if(second != null){
            if(matchedCase){
                second.jjtGetChild(0).jjtAccept(this, data);
            }
        }

        if(third != null){
            for(int i = 0; i < third.jjtGetNumChildren(); i++){
                ASTcaseClause caseClause = (ASTcaseClause)third.jjtGetChild(i);
                double y = caseClause.jjtGetChild(0).jjtAccept(v, data).getDouble();
                int caseEvaluation = (int)y;
                //fallthrough
                if(matchedCase){
                    caseClause.jjtGetChild(1).jjtAccept(this, data);
                }
                else{
                    if(expressionEvaluation == caseEvaluation){
                        matchedCase = true;
                        caseClause.jjtGetChild(1).jjtAccept(this, data);
                    }
                }
            }
        }

        //execute default block if not cases are matched
        if(second != null && !matchedCase){
            second.jjtGetChild(0).jjtAccept(this, data);

            if(third != null){
                for(int i = 0; i < third.jjtGetNumChildren(); i++){
                    ASTcaseClause caseClause = (ASTcaseClause)third.jjtGetChild(i);
                    //fallthrough
                    caseClause.jjtGetChild(1).jjtAccept(this, data);
                }
            }
        }

        return null;
    }

    public JavascriptType visit(ASTdoStatement node, Context data){
        ASTexpression condition = (ASTexpression)node.jjtGetChild(1);
        EvaluationVisitor v = new EvaluationVisitor();
        double x;
        boolean conditionResult;

        do{
            node.jjtGetChild(0).jjtAccept(this, data);

            //update condition
            x = condition.jjtAccept(v, data).getDouble();
            conditionResult = JSToJavaUtils.doubleToBoolean(x);
        }
        while(conditionResult);

        return null;
    }

    public JavascriptType visit(ASTwhileStatement node, Context data){
        ASTexpression condition = (ASTexpression)node.jjtGetChild(0);
        EvaluationVisitor v = new EvaluationVisitor();
        double x = condition.jjtAccept(v, data).getDouble();
        boolean conditionResult = JSToJavaUtils.doubleToBoolean(x);

        while(conditionResult){
            node.jjtGetChild(1).jjtAccept(this, data);

            //update condition
            x = condition.jjtAccept(v, data).getDouble();
            conditionResult = JSToJavaUtils.doubleToBoolean(x);
        }

        return null;
    }

    public JavascriptType visit(ASTforStatement node, Context data){

        switch(node.jjtGetChild(0).getId()){
            case JJTFORHEADER :
                ASTforHeader forHeader = (ASTforHeader)node.jjtGetChild(0);

                //no loop componenets
                if(forHeader.jjtGetNumChildren() == 0){
                    for(;;){
                        node.jjtGetChild(1).jjtAccept(this, data);
                    }
                }

                ASTforInitializer first = null;
                ASTexpression second = null;
                ASTexpression third = null;

                //all loop componenets
                if(forHeader.jjtGetNumChildren() == 3){
                    first = (ASTforInitializer) forHeader.jjtGetChild(0);
                    second = (ASTexpression) forHeader.jjtGetChild(1);
                    third = (ASTexpression) forHeader.jjtGetChild(2);
                }

                //two componenets
                else if(forHeader.jjtGetNumChildren() == 2){
                    //has child 1
                    if(forHeader.jjtGetChild(0).getId() == JJTFORINITIALIZER){
                        first = (ASTforInitializer) forHeader.jjtGetChild(0);

                        //has child 2 no 3
                        if(!first.jjtGetLastToken().next.next.image.equals(";")){
                            second = (ASTexpression) forHeader.jjtGetChild(1);
                        }
                        //has child 3 no 2
                        else{
                            third = (ASTexpression) forHeader.jjtGetChild(1);
                        }
                    }
                    //has child 2 and 3 no 1
                    else{
                        second = (ASTexpression) forHeader.jjtGetChild(0);
                        third = (ASTexpression) forHeader.jjtGetChild(1);
                    }
                }

                else if(forHeader.jjtGetNumChildren() == 1){
                    //loop has child 1 only
                    if(!forHeader.jjtGetFirstToken().image.equals(";")){
                        first = (ASTforInitializer) forHeader.jjtGetChild(0);
                    }

                    //loop has child 3 only
                    else if(!forHeader.jjtGetLastToken().image.equals(";")){
                        third = (ASTexpression) forHeader.jjtGetChild(0);
                    }
                    //loop has child 2 only
                    else {
                        second = (ASTexpression) forHeader.jjtGetChild(0);
                    }
                }

                EvaluationVisitor v = new EvaluationVisitor();
                //execute initializer
                if(first != null){
                    if(first.jjtGetChild(0).getId() == JJTEXPRESSION){
                        first.jjtAccept(v, data);
                    }
                    else{
                        first.jjtAccept(this, data);
                    }
                }

                double x;
                boolean conditionResult = true;

                //evaluate condition
                if(second != null){
                    x = second.jjtAccept(v, data).getDouble();
                    conditionResult = JSToJavaUtils.doubleToBoolean(x);
                }

                //the final loop
                for(;Main.loopTicks++ < 10;){
                    //execute loop body
                    node.jjtGetChild(1).jjtAccept(this, data);


                    //execute loop last expression
                    if(third != null){
                        third.jjtAccept(v, data);
                    }

                    //update condition
                    if(second != null){
                        System.out.println("I have a condition");
                        x = second.jjtAccept(v, data).getDouble();
                        conditionResult = JSToJavaUtils.doubleToBoolean(x);
                    }
                }
                break;


            //for in loop
            case JJTFORINHEADER :
                ASTforInHeader forInHeader = (ASTforInHeader)node.jjtGetChild(0);
                //TODO implement for in loop
                break;
        }
        return null;
    }







    public JavascriptType visit(ASTexpressionStatement node, Context data){
        EvaluationVisitor v = new EvaluationVisitor();
        return node.jjtGetChild(0).jjtAccept(v, data);
    }





    public JavascriptType visit(ASTtopStatement node, Context data){
        node.childrenAccept(this, data);
        return null;
    }
    public JavascriptType visit(ASTtopStatements node, Context data){
        node.childrenAccept(this, data);
        return null;
    }
    public JavascriptType visit(ASTprogram node, Context data){
        node.childrenAccept(this, data);
        return null;
    }
}
