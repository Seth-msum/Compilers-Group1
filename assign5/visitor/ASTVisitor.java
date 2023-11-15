package assign4.visitor;

import assign4.parser.* ;

public class ASTVisitor {
    
    public void visit (CompilationUnit n) {
        
        n.block.accept(this) ;
    }   

    public void visit (BlockStatementNode n) {
        
        n.stmts.accept(this) ;
    } 

    //added by this lab
    public void visit (Statements n) { // This is the most important node to cover for this video.
        
        if (n.stmts != null) {

            n.assign.accept(this) ;
            n.stmts.accept(this) ;
        }
    } 

    public void visit (AssignmentNode n) {
        
        n.left.accept(this) ;
        n.right.accept(this) ;
    } 

    //added by this lab
    public void visit (BinExprNode n) {

        // n.left.accept(this) ;
        // n.right.accept(this) ;
    }

    // public void visit (LiteralNode n) {

    // }

    public void visit (IdentifierNode n) {

    }

    //added by this lab.
    public void visit (LiteralNode n) {

    }
}
