package assign5.ast;

import assign5.visitor.ASTVisitor;

public class Locations extends Node{

    public IdentifierNode left ;
    public LocationNode right = null ;

    public Locations() {

    }
    public Locations(IdentifierNode left) {
        
        this.left = left ;
    }
    public Locations(IdentifierNode left, LocationNode right) {
        
        this.left = left ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
