package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

// Decls --> Decls Decl
// Decl  --> type id ;

public class Declarations extends Node {

    public DeclarationNode decl ;
    public Declarations decls ;

    public Declarations() {

    }

    public Declarations(DeclarationNode decl, Declarations decls) {
        this.decl = decl ;
        this.decls = decls ;
    }
    
    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
