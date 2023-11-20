package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;

// expr --> || | && | == | != | < | <= | >= | >
//          | + | - | * | / | ! | unnary -
//          | id | num | real | true | false
//          | ( expr )

public class ExprNode  extends Node {
    
    public ExprNode () {

    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
