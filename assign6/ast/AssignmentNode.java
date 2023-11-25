package assign6.ast;

import assign6.lexer.*;
import assign6.visitor.*;

public class AssignmentNode extends StatementNode{

    public ExprNode left ;
    //public BinExprNode     right ;
    public Node         right ; //This if for the bool

    public AssignmentNode () {

    }

    // public AssignmentNode (IdentifierNode id, BinExprNode right ) {
    public AssignmentNode (ExprNode left, Node right ) {

        this.left = left ;
        this.right = right ;
    }
    
    public void  accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
