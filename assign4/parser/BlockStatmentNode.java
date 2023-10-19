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

    public BlockStatmentNode () {
        this.assignments = new ArrayList<AssignmentNode>() ;
        this.size = 0;
        //this.class_cursor = 0;
    }

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
