package assign4.parser ;

import assign4.visitor.* ;

public class AdditionNode extends Node {

    public IdentifierNode left  ;
    public IdentifierNode right ;

    public AdditionNode () {

    }
    
    public AdditionNode (IdentifierNode left, IdentifierNode right) {

        this.left  = left  ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
