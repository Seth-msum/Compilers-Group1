package assign4.parser;

import assign4.visitor.* ;
import assign4.lexer.* ;

public class AssignmentNode extends Node{

    public IdentifierNode left ;
    public BinExprNode     right ; // for future assignments, this should just be an expression node.

    public AssignmentNode () {

    }

    public AssignmentNode (IdentifierNode id, BinExprNode right ) {

        this.left = left ;
        this.right = right ;
    }
    
    public void  accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
