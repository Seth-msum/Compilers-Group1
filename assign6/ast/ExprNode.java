package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

//////////////////////////////////////////
//
// expr --> || | && | == | != | < | <= | >= | >
//          | + | - | * | / | ! \ | unary -
//          | id | num | real | true | false
//          | (expr)
//
//////////////////////////////////////////

public class ExprNode extends Node {

    public Type type = null;

    public ExprNode() {

    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
