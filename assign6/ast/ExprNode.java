package assign6.ast;

import assign6.lexer.*;
import assign6.visitor.*;

// expr --> || | && | == | != | < | <= | >= | >
//          | + | - | * | / | ! | unnary -
//          | id | num | real | true | false
//          | ( expr )

public class ExprNode  extends Node {

    public Type type = null ;
    
    public ExprNode () {

    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
