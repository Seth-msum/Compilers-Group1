package assign6.visitor;


import assign6.ast.*;

//import assign5.parser.* ;


public class ASTVisitor {
    
    public void visit (CompilationUnit n) {
        
        n.block.accept(this) ;
    }   

    public void visit (BlockStatementNode n) {
        
        for (DeclarationNode decl : n.decls)
            decl.accept(this) ;
        
        for (StatementNode stmt : n.stmts)
            stmt.accept(this) ;
    } 

    // public void visit (Declarations n) {

    //     if (n.decls != null) {

    //         n.decl.accept(this) ;
    //         n.decls.accept(this) ;
    //     }
    // }

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

    // //added by this lab
    // public void visit (Statements n) { 
        
    //     if (n.stmts != null) {


    //         // Judah: Dr. Lee did not have this code
    //         // Looking further, this type of code is not needed except in the parser. 
    //         // if (n.stmt instanceof AssignmentNode)
    //         //     ((AssignmentNode)n.stmt).accept(this) ;
    //         // else if (n.stmt instanceof WhileNode)
    //         //     ((WhileNode)n.stmt).accept(this) ;
    //         // else if (n.stmt instanceof DoNode)
    //         //     ((DoNode)n.stmt).accept(this) ;
    //         // n.stmts.accept(this) ;

    //         n.stmt.accept(this) ;
    //         n.stmts.accept(this) ;
    //     }
    // } 

    public void visit (StatementNode n) {

    }

    public void visit(ParenthesesNode n) {

        n.expr.accept(this) ;
    }

    public void visit (IfStatementNode n) {

        n.cond.accept(this) ;
        n.stmt.accept(this) ;

        if (n.else_stmt != null)
            n.else_stmt.accept(this) ;
    }

    // Judah: I was not far off from Dr.Lees approachs
    // public void visit (IfNode n) {
    //     if (n.left instanceof IdentifierNode)
    //         ((IdentifierNode)n.left).accept(this) ;
    //     else if (n.left instanceof NumNode)
    //         ((NumNode)n.left).accept(this) ;
    //     else if (n.left instanceof RealNode)
    //         ((RealNode)n.left).accept(this) ;
    //     else if (n.left instanceof BoolNode)
    //         ((BoolNode)n.left).accept(this) ;
    //     else 
    //         ((BinExprNode)n.left).accept(this) ;
    //     if (n.right instanceof AssignmentNode)
    //         ((AssignmentNode)n.right).accept(this) ;
    //     else if (n.right instanceof WhileNode)
    //         ((WhileNode)n.right).accept(this) ;
    //     else if (n.right instanceof DoNode)
    //         ((DoNode)n.right).accept(this) ;
    //     if (n.theElse != null) {
    //         if (n.theElse instanceof AssignmentNode)
    //             ((AssignmentNode)n.theElse).accept(this) ;
    //         else if (n.theElse instanceof WhileNode)
    //             ((WhileNode)n.theElse).accept(this) ;
    //         else if (n.theElse instanceof DoNode)
    //             ((DoNode)n.theElse).accept(this) ;
    //     }
    // }

    public void visit (WhileStatementNode n) {

        n.cond.accept(this) ;
        n.stmt.accept(this) ;
    }

    // public void visit(WhileNode n) {
    //     if (n.left instanceof IdentifierNode)
    //         ((IdentifierNode)n.left).accept(this) ;
    //     else if (n.left instanceof NumNode)
    //         ((NumNode)n.left).accept(this) ;
    //     else if (n.left instanceof RealNode)
    //         ((RealNode)n.left).accept(this) ;
    //     else if (n.left instanceof BoolNode)
    //         ((BoolNode)n.left).accept(this) ;
    //     else 
    //         ((BinExprNode)n.left).accept(this) ;
    //     if (n.left instanceof AssignmentNode)
    //         ((AssignmentNode)n.left).accept(this) ;
    //     else if (n.left instanceof WhileNode)
    //         ((WhileNode)n.left).accept(this) ;
    //     else if (n.left instanceof DoNode)
    //         ((DoNode)n.left).accept(this) ;
    // }

    public void visit (DoWhileStatementNode n) {

        n.stmt.accept(this) ;
        n.cond.accept(this) ;
    }

    // public void visit(DoNode n) {
    //     if (n.left instanceof AssignmentNode)
    //         ((AssignmentNode)n.left).accept(this) ;
    //     else if (n.left instanceof WhileNode)
    //         ((WhileNode)n.left).accept(this) ;
    //     else if (n.left instanceof DoNode)
    //         ((DoNode)n.left).accept(this) ;
    //     if (n.right instanceof IdentifierNode)
    //         ((IdentifierNode)n.right).accept(this) ;
    //     else if (n.right instanceof NumNode)
    //         ((NumNode)n.right).accept(this) ;
    //     else if (n.right instanceof RealNode)
    //         ((RealNode)n.right).accept(this) ;
    //     else 
    //         ((BinExprNode)n.right).accept(this) ;
    // }

    // a[i][j]
    public void visit(ArrayAccessNode  n) {


    }

    public void visit (ArrayDimsNode n) {

        n.size.accept(this) ;

        if (n.dim != null)
            n.dim.accept(this) ;
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
        // else if (n.right instanceof BoolNode)
        //     ((BoolNode)n.right).accept(this) ;
        else 
            ((BinExprNode)n.right).accept(this) ;
    } 

    public void visit (BreakStatementNode n) {

    }

    // public void visit (BreakNode n) {

    // }

    public void visit (TrueNode n) {

    }

    public void visit (FalseNode n) {

    }

    public void visit (ExprNode n) {

    }

    public void visit (BinExprNode n) {

    }

    // public void visit(Locations n) {

    //     n.left.accept(this) ;
    //     if (n.right != null) {
    //         n.right.accept(this) ;
    //     }
    // }

    // public void visit(LocationNode n) {
    //     if (n.left instanceof IdentifierNode)
    //         ((IdentifierNode)n.left).accept(this) ;
    //     else if (n.left instanceof NumNode)
    //         ((NumNode)n.left).accept(this) ;
    //     else if (n.left instanceof RealNode)
    //         ((RealNode)n.left).accept(this) ;
    //     else if (n.left instanceof BoolNode)
    //         ((BoolNode)n.left).accept(this) ;
    //     else 
    //         ((BinExprNode)n.left).accept(this) ;
        
    //     if (n.right != null) {
    //         n.right.accept(this) ;
    //     }
    // }

    public void visit (IdentifierNode n) {

    }

    public void visit (NumNode n) {

    }

    public void visit (RealNode n) {

    }
    // public void visit (BoolNode n) {
        
    // }

   

    public void visit (Node n) {
        System.out.println("There was a mess up! ASTVisitor instance needs a function for: " + n.getClass().getSimpleName());
        System.out.println("Or the node does not have an access function.");
        System.exit(-1) ;
    }
}
