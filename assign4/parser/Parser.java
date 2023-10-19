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

    public void visit (CompilationUnit n) {
        System.out.println("ComilationUnit");

        // It goes straight to assuming its a block statemtnt.
        n.block = new BlockStatmentNode() ;
        n.block.accept(this) ;
    }

    public void visit (BlockStatmentNode n) {
        System.out.println("BlockStatentNode") ;
        if (look.tag == '{')
            System.out.println("Matched with '{' :" + look.tag) ;
        match('{') ;
        n.assign = new AssignmentNode() ;
        n.assign.accept(this) ;
        if (look.tag == '}')
            System.out.println("Matched with '}' :" + look.tag) ;
        match('}') ;
    }
    
    public void visit (AssignmentNode n) {
        //Old: This is id = id
        //New: This is id = additionNode
        n.id = new IdentifierNode() ;
        n.id.accept(this) ;
        match('=') ;
        //n.right = new IdentifierNode() ; //Old
        n.right = new AdditionNode() ;
        n.right.accept(this) ;
        match(';') ;
    }

    public void visit (AdditionNode n) {
        //This is additionNode = id + id
        n.left = new IdentifierNode() ;
        n.left.accept(this) ;

        match('+');

        n.right = new IdentifierNode() ;
        n.right.accept(this) ;

    }

    public void visit (IdentifierNode n) {
        n.id = look.toString() ;
        match(Tag.ID) ;
        System.out.println("IdentifierNode: " + n.id);
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
}