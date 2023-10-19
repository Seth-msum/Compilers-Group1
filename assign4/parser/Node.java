package assign4.parser ;

import assign4.visitor.* ;

public interface Node {

    // public Node () {
        
    // }
    public void accept(ASTVisitor v);
}
