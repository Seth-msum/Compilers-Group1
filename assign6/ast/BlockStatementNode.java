package assign6.ast;

import assign6.visitor.* ;
import assign6.parser.* ;

import java.util.* ;

public class BlockStatementNode extends StatementNode {
    
    // public Declarations decls ;
    // public Statements stmts ;
    public List<DeclarationNode>    decls = new ArrayList<DeclarationNode>() ;
    public List<StatementNode>      stmts = new ArrayList<StatementNode>() ;

    public BlockStatementNode parent ;

    public Env sTable ;

    public BlockStatementNode (BlockStatementNode parent) {
        this.decls = new ArrayList<DeclarationNode>() ;
        this.stmts = new ArrayList<StatementNode>() ;

        this.parent = parent ;
    }

    public BlockStatementNode(List<DeclarationNode> decls, List<StatementNode> stmts) {

        this.decls = decls ;
        this.stmts = stmts ;

        this.parent = parent ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
}
