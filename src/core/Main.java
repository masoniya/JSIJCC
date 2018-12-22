package core;

import javascriptInterpreter.parser.*;
import javascriptInterpreter.tree.*;
import javascriptInterpreter.visitors.*;

import java.io.*;

public class Main {
    public static void main(String args[]) throws ParseException {
        try{
            InputStream in = new FileInputStream("scripts/expression.js");
            Javascript parser = new Javascript(in);
            SimpleNode s = parser.program();
            System.out.println("Successfully parsed the grammar");
            ExecutionVisitor v = new ExecutionVisitor();
            s.jjtAccept(v, null);
            System.out.println("Successfully executed the program");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static int loopTicks = 5;
}
