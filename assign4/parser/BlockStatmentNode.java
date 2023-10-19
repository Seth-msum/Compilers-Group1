package assign4.parser ;

import assign4.visitor.* ;
import java.util.ArrayList ;

/*
 * Block --> Stmts
 * Stmts --> Stmts stmt
 */

public class BlockStatmentNode implements Node {
    
    //public AssignmentNode assign ;
    public ArrayList<AssignmentNode> assignments ;
    public int size = 0;
    //public int class_cursor ;

    // Jayce code: Please Check
    public boolean isWhileBlock;
    public BlockStatmentNode() {
        this.assignments = new ArrayList<AssignmentNode>();
        this.size = 0;
        this.isWhileBlock = false; // By default, it's not a while block.
    }

    public void setAsWhileBlock(boolean isWhileBlock) {
        this.isWhileBlock = isWhileBlock;
    }
    // ^^^

    //Warning, not fully tested.
    public void addAssignmentNode() {
        AssignmentNode assign = new AssignmentNode() ;
        this.assignments.add(assign) ;
        this.size += 1 ;

    }

    public void acceptAssignmentNode(int cursor, ASTVisitor val) {
        AssignmentNode tmp = this.assignments.get(cursor) ;
        tmp.accept(val) ;
        }

    public void accept(ASTVisitor v) {
        v.visit(this) ;
    }
}
