package assign6.ast;

import assign6.lexer.*;
import assign6.visitor.*;

public class IdentifierNode extends ExprNode {

    public String id ;
    public Word w ;     //Do I need w?
    public Type type ;

    public IdentifierNode () {

    }

    public IdentifierNode (Word w) {

        this.id = w.lexeme ;
        this.w = w ;
    }
    public IdentifierNode (Word w, Type type) {
        this.id = w.lexeme ;
        this.w = w ;
        this.type = type ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }

    public void printNode() {

        System.out.println("IdentifierNode: " + id) ;
    }
    
}
