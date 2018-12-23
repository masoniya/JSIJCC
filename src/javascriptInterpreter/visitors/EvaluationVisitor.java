package javascriptInterpreter.visitors;

import javascriptInterpreter.parser.*;
import javascriptInterpreter.tree.*;

import static javascriptInterpreter.parser.JavascriptConstants.*;

import static javascriptInterpreter.tree.JavascriptTreeConstants.*;

import static javascriptInterpreter.visitors.JSToJavaUtils.*;

import java.util.ArrayList;

public class EvaluationVisitor extends JavascriptDefaultVisitor {

    public Object visit(ASTprimaryExpression node, Context data) {
        JavascriptType typeObject = null;
        if(node.jjtGetNumChildren() == 0){
            String identifierName = node.jjtGetFirstToken().image;
            //this token
            if(identifierName.equals("this")){
                return 69.0;
            }
            //identifier name for variables
            else {
                return data.getValue(identifierName);
            }
        }
        //other
        else{
            Node child = node.jjtGetChild(0);
            return child.jjtAccept(this, data);
        }
    }

    public Object visit(ASTliteral node, Context data){
        Token t = node.jjtGetFirstToken();
        JavascriptType typeObject = null;

        switch(t.kind){
            case NULL_LITERAL : typeObject = new JavascriptType(JavascriptType.Type.NULL, t.image); break;
            case BOOLEAN_LITERAL : typeObject = new JavascriptType(Boolean.parseBoolean(t.image)); break;
            case NUMERIC_LITERAL : typeObject = new JavascriptType(Double.parseDouble(t.image)); break;
            case STRING_LITERAL : typeObject = new JavascriptType(JavascriptType.Type.STRING, t.image); break;
        }

        return typeObject;
    }

    public Object visit(ASTarrayLiteral node, Context data){
        //create the array
        ArrayList<JavascriptType> arrayLiteral = new ArrayList<>();
        if(node.jjtGetNumChildren() == 0){
            return new JavascriptType(arrayLiteral);
        }

        int arraySize = 1;

        ASTellision first = null;
        ASTelementList second = null;
        ASTellision third = null;

        //all three children
        if(node.jjtGetNumChildren() == 3){
            first = (ASTellision)node.jjtGetChild(0);
            second = (ASTelementList)node.jjtGetChild(1);
            third = (ASTellision)node.jjtGetChild(2);
        }
        //two children
        else if (node.jjtGetNumChildren() == 2){
            //ellision first then elements
            if(node.jjtGetFirstToken().next.image.equals(",")){
                first = (ASTellision)node.jjtGetChild(0);
                second = (ASTelementList)node.jjtGetChild(1);
            }
            //elements first then ellision
            else {
                second = (ASTelementList)node.jjtGetChild(0);
                third = (ASTellision)node.jjtGetChild(1);
            }
        }
        //only one child
        else{
            //only ellision
            if(node.jjtGetFirstToken().next.image.equals(",")){
                first = (ASTellision)node.jjtGetChild(0);
            }
            //only element list
            else{
                second = (ASTelementList)node.jjtGetChild(0);
            }
        }


        if(first != null){
            //pad the array with empty elements for each comma
            JavascriptType emptyElement = new JavascriptType();
            for(int i = 0; i < countCommas(first); i++){
                arrayLiteral.add(emptyElement);
            }
            //arraySize += countCommas(first);
        }

        if(second != null){
            JavascriptType emptyElement = new JavascriptType();
            JavascriptType arrayElement;

            //only one element
            if(second.jjtGetNumChildren() == 1){
                arrayElement = (JavascriptType) second.jjtGetChild(0).jjtGetChild(0).jjtAccept(this, data);
                arrayLiteral.add(arrayElement);
            }
            //several elements
            else{
                arrayElement = (JavascriptType) second.jjtGetChild(0).jjtGetChild(0).jjtAccept(this, data);
                arrayLiteral.add(arrayElement);
                for(int i = 1; i < second.jjtGetNumChildren(); i += 2){

                    //pad the array with empty elements if there is more than one comma
                    for(int j = 1; j < countCommas((ASTellision)second.jjtGetChild(i)); j++){
                        arrayLiteral.add(emptyElement);
                    }

                    arrayElement = (JavascriptType) second.jjtGetChild(i + 1).jjtGetChild(0).jjtAccept(this, data);
                    arrayLiteral.add(arrayElement);

                    //arraySize += countCommas((ASTellision)second.jjtGetChild(i + 1));
                }
            }
        }

        if(third != null){
            //pad the array with empty elements if there is more than one comma also decrease by 1
            JavascriptType emptyElement = new JavascriptType();
            for(int i = 0; i < countCommas(third) - 1; i++){
                arrayLiteral.add(emptyElement);
            }
            //arraySize += countCommas(third) - 1;
        }

        return new JavascriptType(arrayLiteral);
    }

    public Object visit(ASTobjectLiteral node, Context data){



        return 69.0;
    }

    public Object visit(ASTpropertyDefinition node, javascriptInterpreter.visitors.Context data){

        return defaultVisit(node, data);
    }

    public Object visit(ASTfunctionExpression node, Context data){
        return 69.0;
    }

    public Object visit(ASTparenthesizedExpression node, Context data){
        ASTexpression child = (ASTexpression)node.jjtGetChild(0);
        return visit(child, data);
    }


    public Object visit(ASTleftSideExpression node, Context data){
        Node child = node.jjtGetChild(0);
        if(child.getId() == JJTCALLEXPRESSION){
            return visit((ASTcallExpression)child, data);
        }
        else{
            return visit((ASTnewExpression)child, data);
        }
    }

    public Object visit(ASTcallExpression node, Context data){
        ASTmemberExpression child = (ASTmemberExpression) node.jjtGetChild(0);

        //TODO implement call expression
        return visit(child, data);
    }

    public Object visit(ASTmemberExpression node, Context data){
        ASTprimaryExpression child = (ASTprimaryExpression) node.jjtGetChild(0);

        //TODO implement member expression
        return visit(child, data);
    }

    public Object visit(ASTupdateExpression node, Context data){
        ASTleftSideExpression operand = (ASTleftSideExpression)node.jjtGetChild(0);
        if(node.jjtGetFirstToken() != operand.jjtGetFirstToken()){
            double x = ((JavascriptType)visit(operand, data)).getDouble();
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

    public Object visit(ASTunaryExpression node, Context data){
        ASTupdateExpression operand = (ASTupdateExpression)node.jjtGetChild(0);
        if(node.jjtGetFirstToken() != operand.jjtGetFirstToken()){
            double x = ((JavascriptType)visit(operand, data)).getDouble();
            String operator = node.jjtGetFirstToken().image;
            System.out.print(operator + " " + x);
            switch(operator){
                case "+" : return +x;
                case "-" : return -x;
                case "!" : return !doubleToBoolean(x);
                case "~" : return (double) ~(int)x;
                default : return 69.0;
            }
        }
        return visit(operand, data);
    }

    public Object visit(ASTexponentiationOperator node, Context data){
        ASTunaryExpression firstOperand = (ASTunaryExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTexponentiationOperator secondOperand = (ASTexponentiationOperator)node.jjtGetChild(1);
            double x = ((JavascriptType)visit(firstOperand, data)).getDouble();
            double y = ((JavascriptType)visit(secondOperand, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "**" : return Math.pow(x, y);
            }
        }
        return visit(firstOperand, data);
    }

    public Object visit(ASTmultiplicativeExpression node, Context data){
        ASTexponentiationOperator firstOperand = (ASTexponentiationOperator)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTmultiplicativeExpression secondOperand = (ASTmultiplicativeExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)visit(firstOperand, data)).getDouble();
            double y = ((JavascriptType)visit(secondOperand, data)).getDouble();
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

    public Object visit(ASTadditiveExpression node, Context data){
        JavascriptType typeObject = null;

        ASTmultiplicativeExpression firstOperand = (ASTmultiplicativeExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTadditiveExpression secondOperand = (ASTadditiveExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)visit(firstOperand, data)).getDouble();
            double y = ((JavascriptType)visit(secondOperand, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "+" :
                    System.out.println(x + y);
                    typeObject = new JavascriptType(x + y);
                    return typeObject;
                case "-" :
                    System.out.println(x - y);
                    typeObject = new JavascriptType(x - y);
                    return typeObject;
            }
        }
        return visit(firstOperand, data);
    }

    public Object visit(ASTshiftExpression node, Context data){
        ASTadditiveExpression child = (ASTadditiveExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTrelationalExpression node, Context data){
        ASTshiftExpression child = (ASTshiftExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTequalityExpression node, Context data){
        ASTrelationalExpression child = (ASTrelationalExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTbitwiseAndExpression node, Context data){
        ASTequalityExpression child = (ASTequalityExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTbitwiseXorExpression node, Context data){
        ASTbitwiseAndExpression child = (ASTbitwiseAndExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTbitwiseOrExpression node, Context data){
        ASTbitwiseXorExpression child = (ASTbitwiseXorExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTlogicalAndExpression node, Context data){
        ASTbitwiseOrExpression child = (ASTbitwiseOrExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTlogicalOrExpression node, Context data){
        ASTlogicalAndExpression child = (ASTlogicalAndExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTconditionalExpression node, Context data){
        ASTlogicalOrExpression child = (ASTlogicalOrExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTassignmentExpression node, Context data){
        ASTconditionalExpression child = (ASTconditionalExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

    public Object visit(ASTexpression node, Context data){
        ASTassignmentExpression child = (ASTassignmentExpression)node.jjtGetChild(0);
        return visit(child, data);
    }

}
