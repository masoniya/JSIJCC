public class GenerateTree {
    public static void main(String[] args) {
        try{
            jjtree.main(new String[]{"javascriptGrammar.jjt"});
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
