package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

public class IdentifierNode extends Node {

    public String id ;
    public Word w ;     //Do I need w?

    public IdentifierNode () {

    }

    public IdentifierNode (Word w) {

        this.id = w.lexeme ;
        this.w = w ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }

    public void printNode() {

        System.out.println("IdentifierNode: " + id) ;
    }
    
}
