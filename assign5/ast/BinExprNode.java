package assign5.ast;

import assign5.lexer.Token;
import assign5.visitor.ASTVisitor;

public class BinExprNode extends StatementNode{

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
