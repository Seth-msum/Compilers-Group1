package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

// Type --> Type [NUM] | basic ;

public class TypeNode extends Node{
    
    public Type basic ;                 // basic type (e.g Type.Float)
    public ArrayTypeNode array = null ; // By default, array type is null
    public IdentifierDescriptors metaData ;
    public TypeNode () {

    }

    public TypeNode (IdentifierDescriptors metaData) {

        this.metaData = metaData ;
    }

    public TypeNode (Type basic, ArrayTypeNode array) {

        this.basic = basic ;
        this.array = array ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
