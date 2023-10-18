package assign4.parser ;

import assign4.visitor.* ;
import assign4.lexer.* ;

public class IdentifierNode extends Node {

    public String id ;
    public Word w ;

    public IdentifierNode () {

    }

    public IdentifierNode (Word w) {
        
        this.id = w.lexeme ;
        this.w = w ;
    }

    public void accept(ASTVisitor v) {
        //Told to do work, do it in the visitor (parser most likely)
        v.visit(this) ;
    }

    void printNode() {
        System.out.println("IdentificationNode: " + id) ;
    }
}