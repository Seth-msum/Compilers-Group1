package assign5.ast;

import assign5.visitor.* ;

public class DoNode extends StatementNode {

    public StatementNode left ;
    public Node right ;

    public DoNode () {

    }

    public DoNode(StatementNode left, Node right) {

        this.left = left ;
        this.right = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
