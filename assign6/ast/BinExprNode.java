package assign6.ast;

import assign6.lexer.Token;
import assign6.visitor.ASTVisitor;

public class BinExprNode extends ExprNode{

    public Node left ;
    public Token op ;
    public Node right ;

    public BinExprNode() {

    }

    public BinExprNode (Token op, Node left, Node right) {

        this.op = op ;
        this.left = left ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
