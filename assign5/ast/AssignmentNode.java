package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

public class AssignmentNode extends StatementNode{

    public Locations left ;
    //public BinExprNode     right ;
    public Node         right ; //This if for the bool

    public AssignmentNode () {

    }

    // public AssignmentNode (IdentifierNode id, BinExprNode right ) {
    public AssignmentNode (Locations left, Node right ) {

        this.left = left ;
        this.right = right ;
    }
    
    public void  accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
