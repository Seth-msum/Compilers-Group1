package assign5.visitor;


import assign5.ast.* ;

//import assign5.parser.* ;


public class ASTVisitor {
    
    public void visit (CompilationUnit n) {
        
        n.block.accept(this) ;
    }   

    public void visit (BlockStatementNode n) {
        
        n.decls.accept(this) ;
        n.stmts.accept(this) ;
    } 

    public void visit (Declarations n) {

        if (n.decls != null) {

            n.decl.accept(this) ;
            n.decls.accept(this) ;
        }
    }

    public void visit (DeclarationNode n) {

        n.type.accept(this) ;
        n.id.accept(this) ;
    }

    public void visit (TypeNode n) {

        //This seems it should check if n.array is empty.
        n.array.accept(this) ;
    }

    public void visit (ArrayTypeNode n) {

        n.type.accept(this) ;
    }

    //added by this lab
    public void visit (Statements n) { // This is the most important node to cover for lab 06
        
        if (n.stmts != null) {

            if (n.stmt instanceof AssignmentNode)
                ((AssignmentNode)n.stmt).accept(this) ;
            else if (n.stmt instanceof WhileNode)
                ((WhileNode)n.stmt).accept(this) ;
            else if (n.stmt instanceof DoNode)
                ((DoNode)n.stmt).accept(this) ;
            n.stmts.accept(this) ;
        }
    } 

    public void visit (AssignmentNode n) {
        
        n.left.accept(this) ;
        //n.right.accept(this) ;

        if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this) ;
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this) ;
        else if (n.right instanceof BoolNode)
            ((BoolNode)n.right).accept(this) ;
        else 
            ((BinExprNode)n.right).accept(this) ;
    } 

    public void visit(WhileNode n) {

        if (n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else if (n.left instanceof BoolNode)
            ((BoolNode)n.left).accept(this) ;
        else 
            ((BinExprNode)n.left).accept(this) ;
        
        if (n.right instanceof AssignmentNode)
            ((AssignmentNode)n.right).accept(this) ;
        else if (n.right instanceof WhileNode)
            ((WhileNode)n.right).accept(this) ;
        else if (n.right instanceof DoNode)
            ((DoNode)n.right).accept(this) ;
    }

    public void visit(DoNode n) {

        if (n.left instanceof AssignmentNode)
            ((AssignmentNode)n.left).accept(this) ;
        else if (n.left instanceof WhileNode)
            ((WhileNode)n.left).accept(this) ;
        else if (n.left instanceof DoNode)
            ((DoNode)n.left).accept(this) ;

        if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this) ;
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this) ;
        else 
            ((BinExprNode)n.right).accept(this) ;
    }
    // stmt --> if ( bool ) stmt [else stmt]
    public void visit (IfNode n) {

        if (n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else if (n.left instanceof BoolNode)
            ((BoolNode)n.left).accept(this) ;
        else 
            ((BinExprNode)n.left).accept(this) ;
        
        if (n.right instanceof AssignmentNode)
            ((AssignmentNode)n.right).accept(this) ;
        else if (n.right instanceof WhileNode)
            ((WhileNode)n.right).accept(this) ;
        else if (n.right instanceof DoNode)
            ((DoNode)n.right).accept(this) ;
        
        if (n.theElse != null) {

            if (n.theElse instanceof AssignmentNode)
                ((AssignmentNode)n.theElse).accept(this) ;
            else if (n.theElse instanceof WhileNode)
                ((WhileNode)n.theElse).accept(this) ;
            else if (n.theElse instanceof DoNode)
                ((DoNode)n.theElse).accept(this) ;
        }

    }

    public void visit (ExprNode n) {

    }

    //added by this lab
    public void visit (BinExprNode n) {

        // n.left.accept(this) ;
        // n.right.accept(this) ;
    
        // if (n.right instanceof IdentifierNode)
        //     ((IdentifierNode)n.right).accept(this) ;
        // else
        //     ((LiteralNode)n.right).accept(this) ;
        
        //     n.right.accept(this) ;
    }

    public void visit (IdentifierNode n) {

    }

    //added by this lab.
    public void visit (NumNode n) {

    }

    public void visit (RealNode n) {

    }
    public void visit (BoolNode n) {
        
    }

    public void visit (Node n) {
        System.out.println("There was a mess up!");
    }
}
