package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

public class AssignmentNode extends StatementNode{

    public IdentifierNode left ;
    //public BinExprNode     right ;
    public Node         right ;

    public AssignmentNode () {

    }

    // public AssignmentNode (IdentifierNode id, BinExprNode right ) {
    public AssignmentNode (IdentifierNode left, Node right ) {

        this.left = left ;
        this.right = right ;
    }
    
    public void  accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
