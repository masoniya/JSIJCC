package javascriptInterpreter.visitors;

import javascriptInterpreter.tree.*;

import java.util.ArrayList;

import static javascriptInterpreter.tree.JavascriptTreeConstants.*;

public class EvaluationVisitor extends JavascriptDefaultVisitor {

    public Object visit(ASTprimaryExpression node, Object data) {
        if(node.jjtGetNumChildren() == 0){
            if(node.jjtGetFirstToken().image.equals("this")){
                return 69.0;
            }
            else {
                return 69.0;
            }
        }
        Node child = node.jjtGetChild(0);
        switch(child.getId()){
            case JJTLITERAL : return child.jjtAccept(this, data);
            case JJTPARENTHESIZEDEXPRESSION : return child.jjtAccept(this, data);
            case JJTARRAYLITERAL : return child.jjtAccept(this, data);
            case JJTOBJECTLITERAL : return child.jjtAccept(this, data);
            case JJTFUNCTIONEXPRESSION : return child.jjtAccept(this, data);
        }
        return 69.0;
    }

    public Object visit(ASTliteral node, Object data){
        return Double.parseDouble(node.jjtGetFirstToken().image);
    }

    public Object visit(ASTarrayLiteral node, Object data){
        ArrayList<JavascriptType> arrayLiteral = new ArrayList<>();


        return 69.0;
    }

    public Object visit(ASTarrayElement node, Object data){


        return defaultVisit(node, data);
    }

    public Object visit(ASTobjectLiteral node, Object data){
        return 69.0;
    }

    public Object visit(ASTfunctionExpression node, Object data){
        return 69.0;
    }

    public Object visit(ASTparenthesizedExpression node, Object data){
        ASTexpression child = (ASTexpression)node.jjtGetChild(0);
        return visit(child, data);
    }


    public Object visit(ASTleftSideExpression node, Object data){
        Node child = node.jjtGetChild(0);
        if(child.getId() == JJTCALLEXPRESSION){
            return visit((ASTcallExpression)child, data);
        }
        else{
            return visit((ASTnewExpression)child, data);
        }
    }

    public Object visit(ASTcallExpression node, Object data){
        ASTmemberExpression child = (ASTmemberExpression) node.jjtGetChild(0);

        //TODO implement call expression
        return visit(child, data);
    }

    public Object visit(ASTmemberExpression node, Object data){
        ASTprimaryExpression child = (ASTprimaryExpression) node.jjtGetChild(0);

        //TODO implement member expression
        return visit(child, data);
    }

    public Object visit(ASTupdateExpression node, Object data){
        ASTleftSideExpression operand = (ASTleftSideExpression)node.jjtGetChild(0);
        if(node.jjtGetFirstToken() != operand.jjtGetFirstToken()){
            double x = (double)visit(operand, data);
            String operator;
            //postfix update
            if(node.jjtGetLastToken().image.equals("++") || node.jjtGetLastToken().image.equals("--")){
                operator = node.jjtGetLastToken().image;
                System.out.println(x + " " + operator);
                switch(operator){
                    case "++" : return x;
                    case "--" : return x;
                }
            }
            //prefix update
            else{
                operator = node.jjtGetFirstToken().image;
                System.out.println(operator + " " + x);
                switch(operator){
                    case "++" : return x + 1;
                    case "--" : return x - 1;
                }
            }
        }
        return visit(operand, data);
    }

    public Object visit(ASTunaryExpression node, Object data){
        ASTupdateExpression operand = (ASTupdateExpression)node.jjtGetChild(0);
        if(node.jjtGetFirstToken() != operand.jjtGetFirstToken()){
            double x = (double)visit(operand, data);
            String operator = node.jjtGetFirstToken().image;
            System.out.print(operator + " " + x);
            switch(operator){
                case "+" : return +x;
                case "-" : return -x;
                case "!" : return !JSToJavaUtils.doubleToBoolean(x);
                case "~" : return (double) ~(int)x;
                default : return 69.0;
            }
        }
        return visit(operand, data);
    }

    public Object visit(ASTexponentiationOperator node, Object data){
        ASTunaryExpression firstOperand = (ASTunaryExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTexponentiationOperator secondOperand = (ASTexponentiationOperator)node.jjtGetChild(1);
            double x = (double)visit(firstOperand, data);
            double y = (double)visit(secondOperand, data);
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "**" : return Math.pow(x, y);
            }
        }
        return visit(firstOperand, data);
    }

    public Object visit(ASTmultiplicativeExpression node, Object data){
        ASTexponentiationOperator firstOperand = (ASTexponentiationOperator)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTmultiplicativeExpression secondOperand = (ASTmultiplicativeExpression)node.jjtGetChild(1);
            double x = (double)visit(firstOperand, data);
            double y = (double)visit(secondOperand, data);
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            double result;
            switch(operator){
                case "*" :
                    System.out.println(x * y); return x * y;
                case "/" :
                    System.out.println(x / y); return x / y;
                case "%" :
                    System.out.println(x % y); return x % y;
            }
        }
        return visit(firstOperand, data);
    }

    public Object visit(ASTadditiveExpression node, Object data){
        ASTmultiplicativeExpression firstOperand = (ASTmultiplicativeExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTadditiveExpression secondOperand = (ASTadditiveExpression)node.jjtGetChild(1);
            double x = (double)visit(firstOperand, data);
            double y = (double)visit(secondOperand, data);
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "+" :
                    System.out.println(x + y); return x + y;
                case "-" :
                    System.out.println(x - y); return x - y;
            }
        }
        return visit(firstOperand, data);
    }

    public Object visit(ASTshiftExpression node, Object data){
        ASTadditiveExpression child = (ASTadditiveExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTrelationalExpression node, Object data){
        ASTshiftExpression child = (ASTshiftExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTequalityExpression node, Object data){
        ASTrelationalExpression child = (ASTrelationalExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTbitwiseAndExpression node, Object data){
        ASTequalityExpression child = (ASTequalityExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTbitwiseXorExpression node, Object data){
        ASTbitwiseAndExpression child = (ASTbitwiseAndExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTbitwiseOrExpression node, Object data){
        ASTbitwiseXorExpression child = (ASTbitwiseXorExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTlogicalAndExpression node, Object data){
        ASTbitwiseOrExpression child = (ASTbitwiseOrExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTlogicalOrExpression node, Object data){
        ASTlogicalAndExpression child = (ASTlogicalAndExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTconditionalExpression node, Object data){
        ASTlogicalOrExpression child = (ASTlogicalOrExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTassignmentExpression node, Object data){
        ASTconditionalExpression child = (ASTconditionalExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTexpression node, Object data){
        ASTassignmentExpression child = (ASTassignmentExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

}
