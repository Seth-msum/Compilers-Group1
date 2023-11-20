package assign5.ast;

import assign5.visitor.* ;

// stmt --> if ( bool ) stmt [else stmt]
public class IfNode extends StatementNode {

    public Node left ;
    public StatementNode right ;
    public StatementNode theElse = null ;

    public IfNode() {}

    public IfNode(Node left, StatementNode right) {
        this.left = left ;
        this.right = right ;
    }

    public IfNode(Node left, StatementNode right, StatementNode theElse) {
        this.left = left ;
        this.right = right ;
        this.theElse = theElse ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }

    
}
