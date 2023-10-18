package assign4.parser ;

import assign4.visitor.* ;
import assign4.lexer.* ;

import java.io.* ;

public class Parser extends ASTVisitor {

    public CompilationUnit cu = null ;
    public Lexer lexer        = null ;  
    public static Token look = null ;     

    public Parser (Lexer lexer) { 

        this.lexer = lexer ;
        cu = new CompilationUnit() ;
        visit(cu) ;
    }
    
    public Parser () {

        cu = new CompilationUnit() ;
        visit(cu) ;
    }

    ////////////////////////////////////////
    //  Utility methods
    ////////////////////////////////////////

    void move () {
        try {
            
            look = lexer.scan() ;
        }
        catch(IOException e) {
            
            System.out.println("IOException") ;
        }
    }

    void error (String s) {
	    throw new Error ("near line " + lexer.line + ": " + s) ;
    }

    void match (int t) {
        try {
            if (look.tag == t)
            move() ;
            else
            error("Syntax error") ;
        }
        catch(Error e) {
            
        }	
    }

    ////////////////////////////////////////
    
    public void visit (CompilationUnit n) {
        n.assign = new AssignmentNode() ;
        n.assign.accept(this) ;
    }

    // public void visit (AssignmentNode n) {
    //     n.left = new LiteralNode() ;
    //     n.left.accept(this) ;
        
    //     n.right = new AdditionNode() ;
    //     n.right.accept(this) ;
    // }

    // public void visit (AdditionNode n) {
    //     n.left = new LiteralNode() ;
    //     n.left.accept(this) ;
        
    //     n.right = new LiteralNode() ;
    //     n.right.accept(this) ;
    // }

    // public void visit (LiteralNode n) {

    //     // What should visit(LiteralNode) do? 
    //     // One part of the next assignment.
    // }

}
