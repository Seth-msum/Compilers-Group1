package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

//////////////////////////////////////////
//
// Decl --> Type id;
//
//////////////////////////////////////////

public class DeclarationNode extends Node {
    
    public TypeNode type;
    public IdentifierNode id;

    public DeclarationNode () {

    }

    public DeclarationNode(TypeNode type, IdentifierNode id) {

        this.type = type;
        this.id = id;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
