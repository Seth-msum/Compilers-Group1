package assign5;

import assign5.lexer.* ;
import assign5.parser.* ;
import assign5.unparser.* ;

//lazy class delete:
//  rm assign5/lexer/*.class; rm assign5/parser/*.class; rm assign5/unparser/*.class; rm assign5/visitor/*.class; rm assign5/*.class

public class Main {
    public static void main (String[] args) {

        Lexer lexer = new Lexer() ;
        Parser parser = new Parser(lexer) ;
        // Unparser unparser = new Unparser(parser) ;
        TreePrinter tree = new TreePrinter(parser) ;
    }
}