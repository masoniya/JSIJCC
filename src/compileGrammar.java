public class compileGrammar {
        public static void main(String[] args) {
            try{
                javacc.main(new String[]{"javascriptGrammar.jj"});
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
}
