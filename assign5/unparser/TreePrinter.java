package assign5.unparser;

import assign5.parser.* ;
import assign5.visitor.* ;


public class TreePrinter extends ASTVisitor {
    
    public Parser parser = null ;

    int level = 0 ; //Why is this needed?
    String indent = "..." ;

    public TreePrinter (Parser parser) {

        this.parser = parser ;
        visit(this.parser.cu) ;
    }

    public TreePrinter () {

        visit(this.parser.cu) ;
    }

    ////////////////////////
    // Utility Methods
    ////////////////////////

    void print(String s){
        System.out.print(s);
    }

    void println(String s){
        System.out.println(s);
    }

    void printSpace(){
        System.out.print(" ");
    }

    int indent_level = 0;

    void indentUp(){
        indent_level++;
    }

    void indentDown(){
        indent_level--;
    }

    void printIndent(){
        String s = "";
        for (int i=0; i<indent_level; i++){
            s += "  ";
        }
        print(s);
    }

    void printDotDotDot() { //New function of lab07

        String s = "" ;
        for (int i = 0; i < indent_level; i++ )
            s += "..." ;
        print(s) ;
    }

    public void visit(CompilationUnit n) {

        System.out.println("Tree Printer starts ...");
        System.out.println();
        System.out.println();
        System.out.println("CompilationUnit") ;

        indentUp() ;
        n.block.accept(this) ;
        indentDown() ;
    }

    public void visit(BlockStatementNode n) {

        printDotDotDot() ;
        System.out.println("BlockStatementNode") ;
        //println("{") ;

        indentUp() ;

        n.stmts.accept(this) ;

        indentDown() ;

        //println("}") ;
 
    }

    public void visit(Statements n) {

        if (n.stmts != null) {

            n.assign.accept(this) ;

            n.stmts.accept(this) ;

        }
    }

    public void visit(AssignmentNode n) {

        printDotDotDot();
        System.out.println("AssignmentNode") ;


        //printIndent();
        indentUp() ;
        n.left.accept(this) ;
        indentDown() ;

        printDotDotDot() ;
        println("operator: = ") ;

        // indentUp() ;
        // n.right.accept(this) ;
        // indentDown() ;

        indentUp();

        // if (n.right == null)
        //     System.out.println("*** n.right == null") ;
        // else
        //     System.out.println("### n.right in AssighnemtNode: " + n.right) ;


        if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof LiteralNode)
            ((LiteralNode)n.right).accept(this) ;
        else //BinExpr ???
            ((BinExprNode)n.right).accept(this) ;

        indentDown() ;
        //println(" ;") ;
    }

    public void visit(BinExprNode n) {

        printDotDotDot() ;
        System.out.println("BinExprNode: " + n.op) ;

        // System.out.println("*** n.left in BinExprNode; " + n.left) ;

        indentUp() ;

        if(n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof LiteralNode)
            ((LiteralNode)n.left).accept(this) ;
        else
            ((BinExprNode)n.left).accept(this) ;

        if (n.right != null) {

            //System.out.println("&&& n.right in BinExprNode: " + n.right) ;
            //n.right.accept(this) ;
            
            if (n.right instanceof IdentifierNode)
                ((IdentifierNode)n.right).accept(this) ;
            else if (n.right instanceof LiteralNode)
                ((LiteralNode)n.right).accept(this) ;
            else
                ((BinExprNode)n.right).accept(this) ;    
        } else {

            // System.out.println("@@@ n.right == null in BinExprNode: " + n.right) ;
        }

        indentDown() ;
        
    }

    public void visit(IdentifierNode n) {

        // for (int i = 0; i < indent_level; i++) System.out.print(indent) ;
        printDotDotDot() ;
        System.out.println("IdentifierNodeNode: " + n.id) ;

        // printIndent() ;
        // print("" + n.id) ;
        // println(" ;") ;
    }

    public void visit(LiteralNode n) {

        // for (int i = 0; i < indent_level; i++) System.out.print(indent) ;
        printDotDotDot() ;
        System.out.println("LiteralNode: " + n.literal) ;

        // printIndent() ;
        // print("" + n.literal) ;
        // println(" ;") ;
    }
}
