package assign6.typechecker;

import assign6.ast.*;
import assign6.lexer.*;
import assign6.parser.*;
import assign6.visitor.*;


public class TypeChecker extends ASTVisitor {
    
    public Parser parser = null ;

    int JudahsDeclCount = 0 ;

    int level = 0 ;
    String indent = "..." ;

    int loopCount = 0 ;
    boolean loopExist = false ;

    public TypeChecker (Parser parser) {

        this.parser = parser ;
        visit(this.parser.cu) ;
    }

    public TypeChecker () {

        visit(this.parser.cu) ;
    }

    ////////////////////////
    // Utility Methods
    ////////////////////////

    void error (String s) {
        println(s) ;
        exit(1) ;
    }

    void exit(int n) {
        System.exit(n) ;
    }

    void print(String s){
        System.out.print(s);
    }

    void println(String s){
        System.out.println(s);
    }

    void printSpace(){
        System.out.print(" ");
    }

    // int indent_level = 0;

    // void indentUp(){
    //     indent_level++;
    // }

    // void indentDown(){
    //     indent_level--;
    // }

    // void printIndent(){
    //     String s = "";
    //     for (int i=0; i<indent_level; i++){
    //         s += "  ";
    //     }
    //     print(s);
    // }

    // void printDotDotDot() { //New function of lab07

    //     String s = "" ;
    //     for (int i = 0; i < indent_level; i++ )
    //         s += "..." ;
    //     print(s) ;
    // }

    ////////////////////////////////////////
    // Visit Methods
    ////////////////////////////////////////

    public void visit(CompilationUnit n) {

        System.out.println("*****************************");
        System.out.println("*    TypeChecker starts     *");
        System.out.println("*****************************");
        System.out.println();
        System.out.println("CompilationUnit") ;

        n.block.accept(this) ;

        System.out.println("...Tree Printer ends.");
        System.out.println();
    }

    public void visit(BlockStatementNode n) {

        System.out.println("BlockStatementNode") ;
        //println("{") ;

        for (DeclarationNode decl : n.decls)
            decl.accept(this) ;
        
        for (StatementNode stmt : n.stmts)
            stmt.accept(this) ;
 
    }

    // public void visit (Declarations n) {
    //     //These are of the same order so they do not need indentUp/Down
    //     if (n.decls != null) {

    //         n.decl.accept(this) ;

    //         n.decls.accept(this) ;
    //     }
    // }

    public void visit(DeclarationNode n) {

        System.out.println("DeclarationNode") ;

        n.type.accept(this) ;
        n.id.accept(this) ;

    }

    public void visit(TypeNode n) {

        System.out.println("TypeNode: " + n.basic) ;
        
        if(n.array != null) {
            n.array.accept(this) ;
        }
    } 

    public void visit (ArrayTypeNode n) {

        System.out.println("ArrayTypeNode: " + n.size) ;

        if (n.type != null) {
            n.type.accept(this) ;
        }
    }

    // public void visit(Statements n) {

    //     if (n.stmts != null) {

    //         n.stmt.accept(this) ;

    //         n.stmts.accept(this) ;

    //     }
    // }

    public void visit(ParenthesesNode n) {

        System.out.println("ParenthesesNode") ;

        n.expr.accept(this) ;
    }

    public void visit(IfStatementNode n) {

        System.out.println("IfStatementNode") ;

        n.cond.accept(this) ;

        n.stmt.accept(this) ;

        if (n.else_stmt != null) {

            System.out.println("Else Clause") ;

            n.else_stmt.accept(this) ;
        }
    }

    public void visit (WhileStatementNode n) {
        loopExist = true ;
        loopCount++ ;
        System.out.println("WhileStatementNode") ;

        n.cond.accept(this) ;
        n.stmt.accept(this) ;
        loopCount-- ;
        if (loopCount == 0) {
            loopExist = false ;
        }
    }

    public void visit (DoWhileStatementNode n) {
        loopExist = true ;
        loopCount++ ;
        System.out.println("WhileStatementNode") ;

        n.stmt.accept(this) ;
        n.cond.accept(this) ;
        loopCount-- ;
        if (loopCount == 0) {
            loopExist = false ;
        }
    }

    public void visit(ArrayAccessNode n) {
        System.out.println("ArrayAccessNode") ;

        n.id.accept(this) ;

        n.index.accept(this) ;
    }

    public void visit(ArrayDimsNode n) {

        System.out.println("ArrayDimsNode") ;

        n.size.accept(this) ;

        if (n.dim != null) {

            n.dim.accept(this) ;
        }
    }

    public void visit(BreakStatementNode n) {
        if(loopExist)
            System.out.println("BreakStatementNode") ;
        else
            error("Break is outside of a loop.") ;
    }

    public void visit (TrueNode n) {

        System.out.println("TrueNode") ;
    }
    
    public void visit(FalseNode n) {

        System.out.println("FalseNode");
    }

    public void visit(AssignmentNode n) {

        System.out.println("AssignmentNode") ;

        n.left.accept(this) ;
        IdentifierNode leftId ;
        if (n.left instanceof ArrayAccessNode) { //Judah: This is how to do it! Lets goo!
            leftId = ((ArrayAccessNode)n.left).id ;
        }
        else {
            leftId = (IdentifierNode)n.left ;
        }
        Type leftType = leftId.type ;

        println("In TypeChecker, AssignmentNode's left type: " + leftType) ;


        Type rightType = null ;

        if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof NumNode) {
            ((NumNode)n.right).accept(this) ;
            rightType = Type.Int ;
        }
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this) ;
        else if (n.right instanceof ArrayAccessNode)
            ((ArrayAccessNode)n.right).accept(this) ;
        else if (n.right instanceof ParenthesesNode)
            ((ParenthesesNode)n.right).accept(this);
        else {//BinExpr ???
            ((BinExprNode)n.right).accept(this) ;

            rightType = ((BinExprNode)n.right).type ;
        }

        if (leftType == Type.Int)
            println("********* leftType is Type.Int") ;
        if (leftType == Type.Float && rightType == Type.Int) {

            error("The right-hand side of an assignment is incompatiable to the left-hand size " + ((IdentifierNode)leftId).id) ;
        }

    }

    public void visit(BinExprNode n) {

        System.out.println("BinExprNode: " + n.op) ; 

        Type leftType = null ;
        IdentifierNode leftId = null ;

        if(n.left instanceof IdentifierNode) {
            ((IdentifierNode)n.left).accept(this) ;

            leftId = (IdentifierNode)n.left ;
            leftType = leftId.type ;
        }
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else if (n.left instanceof ArrayAccessNode) 
            ((ArrayAccessNode)n.left).accept(this) ;
        else if (n.left instanceof ParenthesesNode)
            ((ParenthesesNode)n.left).accept(this) ;
        else
            ((BinExprNode)n.left).accept(this) ;
        
        Type rightType = null ;

        if (n.right != null) {
            
            if (n.right instanceof IdentifierNode) {
                ((IdentifierNode)n.right).accept(this) ;
                
                IdentifierNode rightId = (IdentifierNode)n.right ;
                rightType = rightId.type ;
            }
            else if (n.right instanceof NumNode)
                ((NumNode)n.right).accept(this) ;
            else if (n.right instanceof RealNode)
                ((RealNode)n.right).accept(this) ;
            else if (n.right instanceof ArrayAccessNode)
                ((ArrayAccessNode)n.right).accept(this) ;
            else if (n.right instanceof ParenthesesNode)
                ((ParenthesesNode)n.right).accept(this) ;
            else
                ((BinExprNode)n.right).accept(this) ;    
        } else {

            // System.out.println("@@@ n.right == null in BinExprNode: " + n.right) ;
        }

        if (leftType == Type.Float || rightType == Type.Float) {

            n.type = Type.Float ;
        } else {
            n.type = Type.Int ;
        }
        
    }

    public void visit(IdentifierNode n) {

        // printIndent() ;
        System.out.println("IdentifierNode: " + n.id);
        //print("" + n.id) ;
        // println(" ;") ;
    }

    public void visit(NumNode n) {


        // printIndent() ;
        System.out.println("NumNode: " + n.value);
        //print("" + n.value) ;
        // println(" ;") ;
    }

    public void visit(RealNode n) {

        System.out.println("RealNode: " + n.value);
        //print("" + n.value) ;
    }
}
