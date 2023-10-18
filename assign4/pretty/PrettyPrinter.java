package assign4.pretty ;

import assign4.visitor.* ;
import assign4.parser.* ;

import java.io.* ;

public class PrettyPrinter extends ASTVisitor {
    //The pretty printer should apply stylistic formatting conventions
    //  to the input program, adjusting positioning and spacing. 
    // The formatted output should be saved in a file named "output.txt."

    public Parser parser = null;

    public PrettyPrinter(){

        this.parser = parser;
        visit(this.parser.cu);
    }

    public PrettyPrinter(){

        visit(this.parser.cu);
    }

    ////////////////////////////////////////
    //  Utility methods
    ////////////////////////////////////////

    void print(String s){

        System.out.print(s);
    }

    void println(String s){

        System.out.println(s);
    }

    void printSpace(){

        System.out.print(" ");
    }

    int indent = 0;

    void indentUp(){

        indent++;
    }

    void indentDown(){

        indent--;
    }

    void printIndent(){

        String s = "";
        for (int i=0; i<indent; i++){

            s += " ";
        }

        print(s);
    }

    public void visit(BlockStatementNode n){

        println("{");
        indentUp();
        n.identifier.accept(this);
        indentDown();

        println("}");
    }

    public void visit(AssignmentNode n){

        printIndent();
        n.id.accept(this);
        print(" = ");

        printIndent();
        n.right.accept(this);
        print(";");
    }

    public void visit(IdentifierNode n){

        //printIndent();
        print(n.id);
        //println(";");
    }

}
