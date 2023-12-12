package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

//////////////////////////////////////////
//
// Type --> Type[NUM] | basic;
//
//////////////////////////////////////////

public class ArrayTypeNode extends TypeNode {
    
    public TypeNode type;   //array of type
    public int size = 1;    //number of elements

    public ArrayTypeNode () {

    }

    public ArrayTypeNode(int size, TypeNode type) {

        this.size = size;
        this.type = type;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
