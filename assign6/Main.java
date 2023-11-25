package assign6;

import java.io.* ;

import assign6.ast.*;
import assign6.lexer.*;
import assign6.parser.*;
import assign6.typechecker.*;
import assign6.unparser.*;

//lazy class delete:
  
 //    rm assign6/*.class assign6/*.java~ assign6/*/*.class assign6/*/*.java~
/*
    javac assign6/Main.java ; 
    java assign6.Main ;
*/
public class Main {
    
    public static boolean printToOutput = true ;
    public static void main (String[] args) throws FileNotFoundException {

        if (printToOutput) {
            PrintStream o = new PrintStream(new File("output.txt")) ;
            PrintStream console = System.out ;
            System.setOut(o) ;
        }
        
        Lexer lexer = new Lexer() ;
        Parser parser = new Parser(lexer) ;
        TreePrinter tree = new TreePrinter(parser) ;
        TypeChecker checker = new TypeChecker(parser) ;
        //Add intermediate code gen here
        Unparser unparser = new Unparser(checker) ;

    }
}