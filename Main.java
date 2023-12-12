import assign6.lexer.* ;
import assign6.parser.* ;
import assign6.typechecker.*;
import assign6.unparser.* ;
    
public class Main {

    public static void main (String[] args) {

        Lexer lexer = new Lexer() ;
        Parser parser = new Parser(lexer) ;
        TreePrinter tree = new TreePrinter(parser);
        TypeChecker checker = new TypeChecker(parser);
        Unparser unparser = new Unparser(parser) ;
        
    }
}
