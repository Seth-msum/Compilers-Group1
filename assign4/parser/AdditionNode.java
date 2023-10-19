package assign4.parser ;

import assign4.visitor.* ;

public class AdditionNode implements Node {

    public Node left  ;
    public Node right ;

    public AdditionNode () {

    }
    
    public AdditionNode (VariableNode left, Node right) {

        this.left  = left  ;
        this.right = right ;
    }
    public AdditionNode (Node left) {
        this.left = left ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
