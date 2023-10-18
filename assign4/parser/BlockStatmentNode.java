package assign4.parser ;

import assign4.visitor.* ;

public class BlockStatmentNode extends Node {
    
    public IdentifierNode identifier ;
    public BlockStatmentNode () {

    }

    public void accept(ASTVisitor v) {
        v.visit(this) ;
    }
}
