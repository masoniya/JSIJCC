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

    public JavascriptType(int value){
        this((double)value);
    }

    public JavascriptType(double value){
        this.type = Type.NUMBER;
        this.value = value;
    }

    public JavascriptType(boolean value){
        this.type = Type.BOOLEAN;
        this.value = booleanToDouble(value);
    }

    public JavascriptType(Type type, String value){
        this.type = type;
        this.value = parseValue(type, value);
    }

    public JavascriptType(ArrayList<JavascriptType> arrayLiteral){
        this.type = Type.OBJECT;

        //create a javascript object
        JavascriptObject object = new JavascriptObject();

        //add the indices as properties
        for(int i = 0; i < arrayLiteral.size(); i++){
            object.addProperty(String.valueOf(i), arrayLiteral.get(i));
        }

        //add length as property for array
        object.addProperty("length", new JavascriptType(arrayLiteral.size()));

        this.value = object;
    }

    public JavascriptType(HashMap<String, JavascriptType> objectLiteral){
        this.type = Type.OBJECT;

        //TODO object literal
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public static class JavascriptObject {
        private Map<String, JavascriptType> properties;

        public JavascriptObject(){

        }

        public void addProperty(String name, JavascriptType value){
            properties.put(name, value);
        }

        public void assignProperty(String name, JavascriptType value){
            properties.replace(name, value);
        }

        public JavascriptType getProperty(String name){
            return properties.get(name);
        }
    }


    private Object parseValue(Type type, String s){
        Object value = null;

        switch(type){
            case UNDEFINED : return null ;
            case NULL : return 0.0;
            case STRING : return s;
        }
        return null;
    }

    public String getString(){
        return (String)value;
    }

    public double getDouble(){
        return (double)value;
    }

    public boolean getBoolean(){
        return doubleToBoolean((double)value);
    }

    public JavascriptObject getJavascriptObject(){
        return (JavascriptObject)value;
    }

    /*public int getInt(){
        return (int)value;
    }*/

}
