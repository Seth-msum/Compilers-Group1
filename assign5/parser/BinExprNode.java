package assign4.parser;

import assign4.visitor.* ;
import assign4.lexer.* ;

// a =  1 ;
// b = c ;
// d = e + f ;
// x = y - 2 ;

public class BinExprNode extends Node{
    
    public Node         left ;
    public BinExprNode  right ;
    public Token        op ;

    public BinExprNode () {

    }

    public BinExprNode (Node left, BinExprNode right) {

        this.left = left ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
