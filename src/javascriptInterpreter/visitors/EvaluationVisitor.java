package javascriptInterpreter.visitors;

import javascriptInterpreter.parser.*;
import javascriptInterpreter.tree.*;

import static javascriptInterpreter.parser.JavascriptConstants.*;

import static javascriptInterpreter.tree.JavascriptTreeConstants.*;

import static javascriptInterpreter.visitors.JSToJavaUtils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class EvaluationVisitor extends JavascriptDefaultVisitor {

    static private JavascriptType undefinedObject = null;

    public JavascriptType visit(ASTprimaryExpression node, Context data) {
        JavascriptType typeObject = null;
        if(node.jjtGetNumChildren() == 0){
            String identifierName = node.jjtGetFirstToken().image;
            //this token
            if(identifierName.equals("this")){
                return undefinedObject;
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

    public JavascriptType visit(ASTliteral node, Context data){
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

    public JavascriptType visit(ASTarrayLiteral node, Context data){
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

    public JavascriptType visit(ASTobjectLiteral node, Context data){
        HashMap<String, JavascriptType> properties = new HashMap<>();
        String key;
        JavascriptType value;
        for(int i = 0; i < node.jjtGetNumChildren(); i++){
            ASTpropertyDefinition child = (ASTpropertyDefinition)node.jjtGetChild(i);
            key = child.jjtGetFirstToken().image;
            value = (JavascriptType) child.jjtAccept(this, data);

            properties.put(key, value);
        }

        return new JavascriptType(properties);
    }

    public JavascriptType visit(ASTpropertyDefinition node, Context data){
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    public JavascriptType visit(ASTfunctionExpression node, Context data){
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    public JavascriptType visit(ASTparenthesizedExpression node, Context data) {
        ASTexpression child = (ASTexpression)node.jjtGetChild(0);
        return child.jjtAccept(this, data);
    }


    public JavascriptType visit(ASTleftSideExpression node, Context data){
        Node child = node.jjtGetChild(0);
        if(child.getId() == JJTCALLEXPRESSION){
            return child.jjtAccept(this, data);
        }
        else{
            return child.jjtAccept(this, data);
        }
    }

    public JavascriptType visit(ASTcallExpression node, Context data){
        ASTmemberExpression child = (ASTmemberExpression) node.jjtGetChild(0);

        //TODO implement call expression
        return child.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTmemberExpression node, Context data){
        ASTprimaryExpression child = (ASTprimaryExpression) node.jjtGetChild(0);

        //TODO implement member expression
        return child.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTupdateExpression node, Context data){
        ASTleftSideExpression operand = (ASTleftSideExpression)node.jjtGetChild(0);
        if(node.jjtGetFirstToken() != operand.jjtGetFirstToken()){
            JavascriptType variableReference = operand.jjtAccept(this, data);
            double x = variableReference.getDouble();
            String operator;

            //postfix update
            System.out.println(node.jjtGetLastToken().image);
            System.out.println(operand.jjtGetLastToken().image);

            if(node.jjtGetLastToken().image.equals(operand.jjtGetLastToken().image)){
                operator = node.jjtGetLastToken().image;
                System.out.println(x + " " + operator);
                switch(operator){
                    case "++" : if(variableReference.getIdentifierName() != null){
                                    data.assignIdentifier(variableReference.getIdentifierName(), new JavascriptType(x + 1));
                                } return new JavascriptType(x + 1);
                    case "--" : if(variableReference.getIdentifierName() != null){
                                    data.assignIdentifier(variableReference.getIdentifierName(), new JavascriptType(x + 1));
                                } return new JavascriptType(x - 1);
                }
            }

            //prefix update
            else{
                operator = node.jjtGetFirstToken().image;
                System.out.println(operator + " " + x);
                switch(operator){
                    case "++" : if(variableReference.getIdentifierName() != null){
                                    data.assignIdentifier(variableReference.getIdentifierName(), new JavascriptType(x + 1));
                                }
                                return new JavascriptType(x + 1);
                    case "--" : if(variableReference.getIdentifierName() != null){
                                    data.assignIdentifier(variableReference.getIdentifierName(), new JavascriptType(x - 1));
                                }return new JavascriptType(x - 1);
                }
            }
        }
        return operand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTunaryExpression node, Context data){
        ASTupdateExpression operand = (ASTupdateExpression)node.jjtGetChild(0);
        if(node.jjtGetFirstToken() != operand.jjtGetFirstToken()){
            double x = ((JavascriptType)operand.jjtAccept(this, data)).getDouble();
            String operator = node.jjtGetFirstToken().image;
            System.out.print(operator + " " + x);
            switch(operator){
                case "+" : return new JavascriptType(+x);
                case "-" : return new JavascriptType(-x);
                case "!" : return new JavascriptType(!doubleToBoolean(x));
                case "~" : return new JavascriptType(~(int)x);
                default : return undefinedObject;
            }
        }
        return operand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTexponentiationOperator node, Context data){
        ASTunaryExpression firstOperand = (ASTunaryExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTexponentiationOperator secondOperand = (ASTexponentiationOperator)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "**" : return new JavascriptType(Math.pow(x, y));
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTmultiplicativeExpression node, Context data){
        ASTexponentiationOperator firstOperand = (ASTexponentiationOperator)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTmultiplicativeExpression secondOperand = (ASTmultiplicativeExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "*" :
                    return new JavascriptType(x * y);
                case "/" :
                    return new JavascriptType(x / y);
                case "%" :
                    return new JavascriptType(x % y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }


    public JavascriptType visit(ASTadditiveExpression node, Context data){
        JavascriptType typeObject;

        ASTmultiplicativeExpression firstOperand = (ASTmultiplicativeExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTadditiveExpression secondOperand = (ASTadditiveExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "+" :
                    System.out.println(x + y);
                    return new JavascriptType(x + y);
                case "-" :
                    System.out.println(x - y);
                    return new JavascriptType(x - y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTshiftExpression node, Context data){
        JavascriptType typeObject;

        ASTadditiveExpression firstOperand = (ASTadditiveExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTshiftExpression secondOperand = (ASTshiftExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case ">>" :
                    System.out.println((int)x >> (int)y);
                    return new JavascriptType((int)x >> (int)y);
                case "<<" :
                    System.out.println((int)x << (int)y);
                    return new JavascriptType((int)x << (int)y);
                case ">>>" :
                    System.out.println((int)x >>> (int)y);
                    return new JavascriptType((int)x >>> (int)y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }


    public JavascriptType visit(ASTrelationalExpression node, Context data){
        JavascriptType typeObject;

        ASTshiftExpression firstOperand = (ASTshiftExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTrelationalExpression secondOperand = (ASTrelationalExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "<" :
                    System.out.println(x < y);
                    return new JavascriptType(x < y);
                case ">" :
                    System.out.println(x > y);
                    return new JavascriptType(x > y);
                case "<=" :
                    System.out.println(x <= y);
                    return new JavascriptType(x <= y);
                case ">=" :
                    System.out.println(x >= y);
                    return new JavascriptType(x >= y);
                case "in" :
                    /*System.out.println(x + " in " + y);
                    return new JavascriptType(() ? : );*/
                    return undefinedObject;
                case "instanceof" :
                    return undefinedObject;
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTequalityExpression node, Context data){
        JavascriptType typeObject;

        ASTrelationalExpression firstOperand = (ASTrelationalExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTequalityExpression secondOperand = (ASTequalityExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "==" :
                    System.out.println(x < y);
                    return new JavascriptType(x == y);
                case "!=" :
                    System.out.println(x > y);
                    return new JavascriptType(x != y);
                case "===" :
                    return undefinedObject;
                case "!==" :
                    return undefinedObject;
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTbitwiseAndExpression node, Context data){
        JavascriptType typeObject;

        ASTequalityExpression firstOperand = (ASTequalityExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTbitwiseAndExpression secondOperand = (ASTbitwiseAndExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "&" :
                    System.out.println((int)x & (int)y);
                    return new JavascriptType((int)x & (int)y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTbitwiseXorExpression node, Context data){
        JavascriptType typeObject;

        ASTbitwiseAndExpression firstOperand = (ASTbitwiseAndExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTbitwiseXorExpression secondOperand = (ASTbitwiseXorExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "^" :
                    System.out.println((int)x ^ (int)y);
                    return new JavascriptType((int)x ^ (int)y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTbitwiseOrExpression node, Context data){
        JavascriptType typeObject;

        ASTbitwiseXorExpression firstOperand = (ASTbitwiseXorExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTbitwiseOrExpression secondOperand = (ASTbitwiseOrExpression)node.jjtGetChild(1);
            double x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getDouble();
            double y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "|" :
                    System.out.println((int)x | (int)y);
                    return new JavascriptType((int)x | (int)y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTlogicalAndExpression node, Context data){
        JavascriptType typeObject;

        ASTbitwiseOrExpression firstOperand = (ASTbitwiseOrExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTlogicalAndExpression secondOperand = (ASTlogicalAndExpression)node.jjtGetChild(1);
            boolean x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getBoolean();
            boolean y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getBoolean();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "&&" :
                    System.out.println(x && y);
                    return new JavascriptType(x && y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTlogicalOrExpression node, Context data){
        JavascriptType typeObject;

        ASTlogicalAndExpression firstOperand = (ASTlogicalAndExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTlogicalOrExpression secondOperand = (ASTlogicalOrExpression)node.jjtGetChild(1);
            boolean x = ((JavascriptType)firstOperand.jjtAccept(this, data)).getBoolean();
            boolean y = ((JavascriptType)secondOperand.jjtAccept(this, data)).getBoolean();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "||" :
                    System.out.println(x || y);
                    return new JavascriptType(x || y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTconditionalExpression node, Context data){
        ASTlogicalOrExpression firstChild = (ASTlogicalOrExpression)node.jjtGetChild(0);

        if(node.jjtGetNumChildren() > 1){
            ASTassignmentExpression secondChild = (ASTassignmentExpression)node.jjtGetChild(1);
            ASTassignmentExpression thirdChild = (ASTassignmentExpression)node.jjtGetChild(2);

            boolean condition = ((JavascriptType)firstChild.jjtAccept(this, data)).getBoolean();

            if(condition){
                return secondChild.jjtAccept(this, data);
            }
            else{
                return thirdChild.jjtAccept(this, data);
            }
        }

        return firstChild.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTassignmentExpression node, Context data){
        //all three children
        if(node.jjtGetChild(0).getId() == JJTLEFTSIDEEXPRESSION){
            ASTleftSideExpression firstChild = (ASTleftSideExpression)node.jjtGetChild(0);
            JavascriptType variableReference = (JavascriptType) firstChild.jjtAccept(this, data);

            ASTassignmentExpression second = (ASTassignmentExpression)node.jjtGetChild(1);
            JavascriptType assignedValue = (JavascriptType)second.jjtAccept(this, data);

            String operator = ((ASTleftSideExpression) node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.println(operator);
            //assignment
            switch(operator){
                case "=" : if(variableReference.getIdentifierName() == null){
                                return assignedValue;
                            }
                            else{
                                data.assignIdentifier(variableReference.getIdentifierName(), assignedValue);
                                return assignedValue;
                            }
                default : return undefinedObject;
            }
        }
        //only one child conditional
        else{
            ASTconditionalExpression child = (ASTconditionalExpression)node.jjtGetChild(0);

            return child.jjtAccept(this, data);
        }
    }

    public JavascriptType visit(ASTexpression node, Context data){
        ASTassignmentExpression firstChild = (ASTassignmentExpression)node.jjtGetChild(0);
        double x = ((JavascriptType)firstChild.jjtAccept(this, data)).getDouble();
        for(int i = 1; i < node.jjtGetNumChildren(); i++){
            node.jjtGetChild(i).jjtAccept(this, data);
        }

        return new JavascriptType(x);
    }

}
