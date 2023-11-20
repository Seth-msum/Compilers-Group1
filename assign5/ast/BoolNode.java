package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

public class BoolNode extends Node {

    public boolean value ;
    public Word v ;

    public BoolNode() {

    }

    public BoolNode (int bool) {

        if (bool == Tag.TRUE) {
            this.value = true ;
            this.v = Word.True ;
        }
        else if (bool == Tag.FALSE) {
            this.value = false ;
            this.v = Word.False ;
        }
        else 
            System.out.println("BoolNode error");
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
    public void printNode() {
        System.out.println("BoolNode: " + value) ;
    }
}
