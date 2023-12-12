package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

///////////////////////////////////////
//
// Type --> Type [NUM] | basic;
//
///////////////////////////////////////

public class TypeNode extends Node{
 
    public Type basic;                      //basic type (ie: Type Int)
    public ArrayTypeNode array = null;      // By default, array type is null

    public TypeNode() {

    }

    public TypeNode(Type basic, ArrayTypeNode array) {

        this.basic = basic;
        this.array = array;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);

    }
}
