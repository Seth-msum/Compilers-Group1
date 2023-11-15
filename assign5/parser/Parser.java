package assign4.parser;

import assign4.visitor.* ;
import assign4.lexer.* ;

import java.io.* ;


public class Parser extends ASTVisitor {

    public CompilationUnit cu = null ;
    public Lexer lexer        = null ;

    public Token look = null ;

    public Parser (Lexer lexer) {

        this.lexer = lexer ;
        cu = new CompilationUnit() ;

        move() ;

        visit(cu) ;
    }

    public Parser () {

        cu = new CompilationUnit() ;

        move() ;

        visit(cu) ;
    }

    ///////////
    //  Utility methods
    ///////////

    void move() {
        try {
            look = lexer.scan() ;
        }
        catch (IOException e) {

            System.out.println("IOException") ;
        }
    }

    void error (String s) {

        throw new Error("near line " + lexer.line + ": " + s) ;
    }

    void match (int t) {

        try {
            if (look.tag == t) 
                move() ;
            else
                error("Syntax error") ;
        }
        catch (Error e) {

        }
    }

    ///////////////////////////////////

    public void visit(CompilationUnit n) {

        System.out.println("CompilationUnit") ;

        n.block = new BlockStatementNode() ;
        n.block.accept(this) ;
    }

    public void visit (BlockStatementNode n) {
 
        System.out.println("BlockStatmentNode");

        if (look.tag == '{')
            System.out.println("Matched with '{':  " + look.tag);
        match('{') ;

        n.stmts = new Statements() ;
        n.stmts.accept(this) ;

        if (look.tag == '}')
            System.out.println("Matched with '}':  " + look.tag);
        match('}') ;
    }

    ////////////////////////////////
    //
    // Stmts --> Stmts Stmt
    // Stmt --> id = expr
    //
    ///////////////////////////////

    public void visit(Statements n) {

        if (!look.toString().equals("}")) {
            //If it's not the end bracket, then its another assignemt

            switch (look.tag) {   

                default:
                    n.assign = new AssignmentNode() ;
                    n.assign.accept(this) ;
                    
                    n.stmts = new Statements() ;
                    n.stmts.accept(this) ;

                    break;
            }
        }
    }

    public void visit (AssignmentNode n) {

        n.left = new IdentifierNode() ;        
        n.left.accept(this) ;

        match('=') ;

        //System.out.println("***** BinExprNode");
        n.right = new BinExprNode() ;
        n.right.accept(this) ;
        //System.out.println("@@@@ BinExprNode") ;
        
        match(';');
    }

    public void visit(BinExprNode n) {

        System.out.println("****** " + look.toString()) ;
        System.out.println("****** " + look) ;
    
        if (look.tag == Tag.ID) {
            System.out.println("look.tag == Tag.ID: "+  look.toString());
            n.left = new IdentifierNode() ;
            ((IdentifierNode)n.left).accept(this) ;

        } else if (look.tag == Tag.NUM) {
            System.out.println("look.tag == Tag.NUM: " + look.toString()) ;
            n.left = new LiteralNode() ;
            ((LiteralNode)n.left).accept(this) ;
        
        }

        System.out.println("look.toString() in BinExprNode: " + look.toString()) ;
        
        if (look.toString().equals("+") || look.toString().equals("-")) {

            //n.op = look.toString() ;
            n.op = look ;
            move() ;

            n.right = new BinExprNode() ;
            n.right.accept(this) ;
        }
    }

    // public void visit (IdentifierNode n) {
 
    //     n.id = look.toString() ;
        
    //     match(Tag.ID) ;

    //     System.out.println("IdentifierNode: " + n.id);
        
    // }

    public void visit(LiteralNode n) {

        n.literal = ((Num)look).value ;

        match(Tag.NUM) ;  // expext look.tag == Tag.NUM

        n.printNode() ;

        //System.out.println("look in IdentifierNode: " +  look);
    }

    public void visit(IdentifierNode n) {

        n.id = look.toString() ;

        match(Tag.ID) ; // expect look.tag == Tag.ID

        n.printNode() ;

        //System.out.println("IdentifierNode: " + n.id) ;
        //System.out.println("look in IdentifierNode: " + look) ;
    }
    
}
