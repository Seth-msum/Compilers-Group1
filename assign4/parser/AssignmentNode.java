package assign4.parser ;

import assign4.visitor.* ;

public class AssignmentNode extends Node {

    public LiteralNode  left  ;
    public AdditionNode right ;

    public AssignmentNode () {
        
    }
    public AssignmentNode (LiteralNode left, AdditionNode right) {

        this.left  = left  ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
