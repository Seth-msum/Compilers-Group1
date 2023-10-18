package assign4 ;

import assign4.lexer.* ;
import assign4.parser.* ;
import assign4.pretty.* ;
    
public class Main {

    public static void main (String[] args) {

        Lexer lexer = new Lexer() ;
        Parser parser = new Parser(lexer) ;
        PrettyPrinter pretty = new PrettyPrinter(parser) ;
	
    }
}