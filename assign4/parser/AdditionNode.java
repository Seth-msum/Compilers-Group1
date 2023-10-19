package assign4.parser ;

import assign4.visitor.* ;

public class AdditionNode extends Node {

    public VariableNode left  ;
    public VariableNode right ;

    public AdditionNode () {

    }
    
    public AdditionNode (VariableNode left, VariableNode right) {

        this.left  = left  ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
