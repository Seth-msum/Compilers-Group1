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
        else 
            ((BinExprNode)n.right).accept(this) ;
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

    public void visit (Node n) {
        System.out.println("There was a mess up!");
    }
}
