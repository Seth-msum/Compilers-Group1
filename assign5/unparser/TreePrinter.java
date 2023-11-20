package assign5.unparser;

import assign5.lexer.* ;
import assign5.ast.* ;
import assign5.parser.* ;
import assign5.visitor.* ;


public class TreePrinter extends ASTVisitor {
    
    public Parser parser = null ;

    int JudahsDeclCount = 0 ;

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

        System.out.println();
        System.out.println("Tree Printer starts ...");
        System.out.println();
        System.out.println();
        System.out.println("CompilationUnit") ;

        indentUp() ;
        n.block.accept(this) ;
        indentDown() ;

        System.out.println("...Tree Printer ends.");
        System.out.println();
    }

    public void visit(BlockStatementNode n) {

        printDotDotDot() ;
        System.out.println("BlockStatementNode") ;
        //println("{") ;

        indentUp() ;

        n.decls.accept(this) ;

        indentDown() ;

        indentUp() ;

        n.stmts.accept(this) ;

        indentDown() ;

        //println("}") ;
 
    }

    public void visit (Declarations n) {
        //These are of the same order so they do not need indentUp/Down
        if (n.decls != null) {
            if(JudahsDeclCount == 0) {
                printDotDotDot();
                System.out.println("**Declarations --[--") ;
            }
            JudahsDeclCount++ ;

            n.decl.accept(this) ;

            n.decls.accept(this) ;
            JudahsDeclCount-- ;
            if(JudahsDeclCount == 0) {
                printDotDotDot();
                System.out.println("**Declarations --]-- ") ;
            }
        }
        else  {
        }
    }

    public void visit(DeclarationNode n) {

        printDotDotDot() ;
        System.out.println("DeclarationNode") ;

        indentUp() ;
        n.type.accept(this) ;
        indentDown() ;

        indentUp() ;
        n.id.accept(this) ;
        indentDown() ;
    }

    public void visit(TypeNode n) {

        printDotDotDot() ;
        System.out.println("TypeNode: " + n.basic) ;
        
        if(n.array != null) {

            indentUp() ;
            n.array.accept(this) ;
            indentDown() ;
        }
    } 

    public void visit (ArrayTypeNode n) {

        printDotDotDot() ;
        System.out.println("ArrayTypeNode: " + n.size) ;

        if (n.type != null) {

            indentUp() ;
            n.type.accept(this) ;
            indentDown() ;
        }
    }

    public void visit(Statements n) {

        if (n.stmts != null) {

            if (n.stmt instanceof AssignmentNode)
                ((AssignmentNode)n.stmt).accept(this) ;

            n.stmts.accept(this) ;

        }
        // else
        //     System.out.println("The statements node's stmts is empty.");
    }

    public void visit(AssignmentNode n) {
        
        printDotDotDot() ;
        System.out.println("AssignmentNode") ;
        indentUp() ;
        n.left.accept(this) ;
        indentDown() ;

        printDotDotDot() ;
        System.out.println("Operator: = ");

        indentUp() ;
        if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this) ;
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this) ;
        else //BinExpr ???
            ((BinExprNode)n.right).accept(this) ;

        indentDown() ;
            //println(" ;") ;
    }


    public void visit(BinExprNode n) {


        indentUp() ;
        if(n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else
            ((BinExprNode)n.left).accept(this) ;

        if (n.op != null) {
            printDotDotDot();
            System.out.println("BinExprNode: " + n.op.toString());
        }
            // print(" " + n.op.toString() + " ") ;
        
        if (n.right != null) {

            //System.out.println("&&& n.right in BinExprNode: " + n.right) ;
            //n.right.accept(this) ;
            //if(getPrecedence(n.op) < getPrecedence(n))
            
            if (n.right instanceof IdentifierNode)
                ((IdentifierNode)n.right).accept(this) ;
            else if (n.right instanceof NumNode)
                ((NumNode)n.right).accept(this) ;
            else if (n.right instanceof RealNode)
                ((RealNode)n.right).accept(this) ;
            else
                ((BinExprNode)n.right).accept(this) ;    
        } else {

            // System.out.println("@@@ n.right == null in BinExprNode: " + n.right) ;
        }
        indentDown() ;
        
    }

    public void visit(IdentifierNode n) {

        // printIndent() ;
        printDotDotDot();
        System.out.println("IdentifierNode: " + n.id);
        //print("" + n.id) ;
        // println(" ;") ;
    }

    public void visit(NumNode n) {


        // printIndent() ;
        printDotDotDot();
        System.out.println("NumNode: " + n.value);
        //print("" + n.value) ;
        // println(" ;") ;
    }

    public void visit(RealNode n) {

        printDotDotDot();
        System.out.println("RealNode: " + n.value);
        //print("" + n.value) ;
    }
}
