package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

public class LocationNode extends Node{

    public Node left ;
    public LocationNode right ;

    public LocationNode() {

    }

    public LocationNode (Node left) {

        this.left = left ;
    }

    public LocationNode (Node left, LocationNode right) {

        this.left = left ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
}
