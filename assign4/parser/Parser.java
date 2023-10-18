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

        move() ; // load a token into look


        visit(cu) ;
    }
    
    public Parser () {

        cu = new CompilationUnit() ;

        move() ;// to load a token into look

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
        //first compares, then it calls move. 
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
    
    // public void visit (CompilationUnit n) {
    //     n.assign = new AssignmentNode() ;
    //     n.assign.accept(this) ;
    // }

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
    public void visit (CompilationUnit n) {
        System.out.println("ComilationUnit");

        // It goes straight to assuming its a block statemtnt.
        n.block = new BlockStatmentNode() ;
        n.block.accept(this) ;
    }



    // public void visit (AdditionNode n) {
        
    //     n.left.accept(this) ;
    //     n.right.accept(this) ;
    // }

    public void visit (BlockStatmentNode n) {
        // {stmt} <- this is a BlockStatementNode
        System.out.println("BlockStatentNode") ;
        // starting point for block statement

        if (look.tag == '{')
            System.out.println("Matched with '{' :" + look.tag) ;
        match('{') ;
        
        // This used to only assume that the block statement stores an identificaiton
        //  now it assumes that an assignment is stored.
        //n.assign = new IdentifierNode(); 
        n.assign = new AssignmentNode() ;
        n.assign.accept(this) ; //Do work on this identifier node.


        // if (look.tag == ';')
        //     System.out.println("Matched with ';' :" + look.tag) ;
        // match(';') ;

        if (look.tag == '}')
            System.out.println("Matched with '}' :" + look.tag) ;
        match('}') ;
    }

    public void visit (AssignmentNode n) {
        
        n.id = new IdentifierNode() ;
        n.id.accept(this) ;
        
        match('=') ;

        n.right = new IdentifierNode() ;
        n.right.accept(this) ;
                
    
        // if (look.tag == ';')
        //     System.out.println("Matched with ';' :" + look.tag) ;
        match(';') ;
    }

    public void visit (IdentifierNode n) {

        n.id = look.toString() ;

        match(Tag.ID) ;

        System.out.println("IdentifierNode: " + n.id);
    }
}
