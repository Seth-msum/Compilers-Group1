package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

// Decl --> type id ;

public class DeclarationNode extends Node {

    public TypeNode type ;
    public IdentifierNode id ;

    public DeclarationNode() {

    }

    public DeclarationNode (TypeNode type, IdentifierNode id) {

        this.type = type ;
        this.id = id ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
}
