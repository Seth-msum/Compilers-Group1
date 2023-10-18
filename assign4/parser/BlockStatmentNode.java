package assign4.parser ;

import org.omg.CORBA.IdentifierHelper;

import assign4.visitor.* ;

public class BlockStatmentNode extends Node {
    
    public IdentifierNode identifier ;
    public BlockStatmentNode () {

    }

    public void accept(ASTVisitor v) {
        v.visit(this) ;
    }
}
