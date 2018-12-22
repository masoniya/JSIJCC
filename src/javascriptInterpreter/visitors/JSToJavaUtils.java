package javascriptInterpreter.visitors;

public class JSToJavaUtils {
    public static boolean doubleToBoolean(double d){
        return d != 0;
    }

    public static double booleanToDouble(boolean b){
        return b ? 1 : 0;
    }
}
