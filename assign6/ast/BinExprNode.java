package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

// Example:
// Checks right side of assignment operator
// a = 1 ;          1 = literal
// b = c ;          c = identifier
// d = e + f ;      e+f = BinExprNode
// x = y - 2 ;      y-2 = BinExprNode

public class BinExprNode extends ExprNode {
    
    public ExprNode left;
    public ExprNode right;
    public Token op;

    public BinExprNode() {

    }

    public BinExprNode(Token op, ExprNode left, ExprNode right) {

        this.op = op;
        this.left = left;
        this.right = right;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
