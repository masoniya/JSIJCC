package javascriptInterpreter.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javascriptInterpreter.visitors.JSToJavaUtils.*;

public class JavascriptType {
    public enum Type {
        UNDEFINED(0),
        NULL(1),
        BOOLEAN(2),
        STRING(3),
        NUMBER(4),
        OBJECT(5);

        int id;
        Type(int id){
            this.id = id;
        }
    }

    private Type type;

    private Object value;



    public JavascriptType(){
        this.type = Type.UNDEFINED;
        this.value = null;
    }

    public JavascriptType(Type type, String value){
        this.value = parseValue(type, value);
    }

    public JavascriptType(ArrayList<JavascriptType> arrayLiteral){

    }

    public JavascriptType(HashMap<String, JavascriptType> objectLiteral){

    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public static class JavascriptObject {
        Map<String, JavascriptType> properties;
        public JavascriptObject(){

        }
    }

    private Object parseValue(Type type, String s){
        Object value = null;

        switch(type){
            case UNDEFINED : return null ;
            case NULL : return 0;
            case BOOLEAN : return booleanToDouble(Boolean.parseBoolean(s)) ;
            case STRING : return s;
            case NUMBER : return Double.parseDouble(s);
        }
        return null;
    }


}
