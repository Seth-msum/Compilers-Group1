package assign5;

import assign5.lexer.* ;
import assign5.parser.* ;
import assign5.ast.* ;
import assign5.unparser.* ;

//lazy class delete:
//  rm assign5/lexer/*.class assign5/parser/*.class assign5/unparser/*.class assign5/visitor/*.class assign5/*.class assign5/ast/*.class; javac assign5/Main.java ; java assign5.Main

public class Main {
    public static void main (String[] args) {

        Lexer lexer = new Lexer() ;
        Parser parser = new Parser(lexer) ;
        //TreePrinter tree = new TreePrinter(parser) ;
        Unparser unparser = new Unparser(parser) ;
    }
}