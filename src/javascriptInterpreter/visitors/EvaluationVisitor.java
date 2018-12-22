package javascriptInterpreter.visitors;

import javascriptInterpreter.tree.*;

import static javascriptInterpreter.tree.JavascriptTreeConstants.*;

public class EvaluationVisitor extends JavascriptDefaultVisitor {

    public Object visit(ASTprimaryExpression node, Object data){
        if(node.jjtGetNumChildren() == 0){
            return 69;
        }
        Node child = node.jjtGetChild(0);
        return visit((ASTliteral) child, data);
    }

    public Object visit(ASTliteral node, Object data){
        return Double.parseDouble(node.jjtGetFirstToken().image);
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
        if(node.jjtGetNumChildren() > 1){
            Double x = (Double)visit(operand, data);
            String operator = "";
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
        if(node.jjtGetNumChildren() > 1){
            Double x = (Double)visit(operand, data);
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.println(operator + " " + x);
            switch(operator){
                case "+" : return +x;
                case "-" : return -x;
                case "!" : return x == 0 ? 1 : 0;
                case "~" : return ~x.intValue();
                default : return 69;
            }
        }
        return visit(operand, data);
    }

    public Object visit(ASTexponentiationOperator node, Object data){
        ASTunaryExpression firstOperand = (ASTunaryExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTexponentiationOperator secondOperand = (ASTexponentiationOperator)node.jjtGetChild(1);
            Double x = (Double)visit(firstOperand, data);
            Double y = (Double)visit(secondOperand, data);
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.println(x + " " + operator + " " + y);
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
            Double x = (Double)visit(firstOperand, data);
            Double y = (Double)visit(secondOperand, data);
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.println(x + " " + operator + " " + y);
            switch(operator){
                case "*" : return x * y;
                case "/" : return x / y;
                case "%" : return x % y;
            }
        }
        return visit(firstOperand, data);
    }

    public Object visit(ASTadditiveExpression node, Object data){
        ASTmultiplicativeExpression firstOperand = (ASTmultiplicativeExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTadditiveExpression secondOperand = (ASTadditiveExpression)node.jjtGetChild(1);
            Double x = (Double)visit(firstOperand, data);
            Double y = (Double)visit(secondOperand, data);
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.println(x + " " + operator + " " + y);
            switch(operator){
                case "+" :
                    System.out.println(x + y); return x + y;
                case "-" :
                    System.out.println(x - y); return x - y;
            }
        }
        return visit(firstOperand, data);
    }

}
