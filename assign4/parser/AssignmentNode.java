package assign4.parser ;

import assign4.visitor.* ;

public class AssignmentNode implements Node {

    public IdentifierNode  id  ;
    //public IdentifierNode right ;
    public AdditionNode right ;

    public AssignmentNode () {
        
    }
    public AssignmentNode (IdentifierNode id, AdditionNode right) {

        this.id  = id  ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
