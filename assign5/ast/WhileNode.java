package assign5.ast;


import assign5.visitor.*;

// stmt --> while ( bool ) stmt

public class WhileNode extends StatementNode {
    
    public Node         left ; //This if for the bool, takes in the BinExprNode
    public StatementNode right ; //This is for the stmt

    public WhileNode () {

    }

    public WhileNode(Node left, StatementNode right) {

        this.left = left ;
        this.left = right ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
