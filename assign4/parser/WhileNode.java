package assign4.parser;

import assign4.visitor.ASTVisitor;

public class WhileNode implements Node {
    public IdentifierNode condition;
    public BlockStatmentNode body;

    public WhileNode(IdentifierNode condition, BlockStatmentNode body) {
        this.condition = condition;
        this.body = body;
    }

    public void accept(ASTVisitor v) {
        v.visit(this);
    }
}
