package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;
 
public class AssignmentNode extends StatementNode {

    public ExprNode left;
    public Node right;

    public AssignmentNode() {

    }

    //public AssignmentNode(IdentifierNode left, BinExprNode right) {
    public AssignmentNode(ExprNode left, Node right) {

        this.left = left;
        this.right = right;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }

}