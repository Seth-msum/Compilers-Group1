package assign4.parser ;

import assign4.visitor.* ;

public class BlockStatmentNode extends Node {
    
    public AssignmentNode assign ;
    public BlockStatmentNode () {

    }

    public void accept(ASTVisitor v) {
        v.visit(this) ;
    }
}
