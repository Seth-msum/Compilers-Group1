package assign6.ast;

import java.util.ArrayList;
import java.util.List;

import assign6.lexer.*;
import assign6.visitor.*;

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
