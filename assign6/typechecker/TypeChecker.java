package assign6.typechecker;

import assign6.parser.*;
import assign6.visitor.*;
import assign6.ast.*;
import assign6.lexer.*;

public class TypeChecker extends ASTVisitor {
    
    public Parser parser = null;
    public CompilationUnit cu = null;

    int level = 0;
    String indent = "...";

    public TypeChecker (Parser parser) {

        this.parser = parser;
        cu = parser.cu;
        visit(cu);
    }

    public TypeChecker() {

        visit(this.parser.cu);
    }

    ///////////////////////////////////////
    // Utility Methods
    ///////////////////////////////////////

    void error(String s) {

        println(s);
        exit(1);
    }

    void exit(int n) {

        System.exit(n);
    }
    
    void print(String s) {

        System.out.print(s);
    }

    void println(String s) {

        System.out.println(s);
    }

    void printSpace() {

        System.out.print(" ");
    }

    ////////////////////////////////////////
    // Visit Methods
    ////////////////////////////////////////

    public void visit(CompilationUnit n) {

        System.out.println("************************************");
        System.out.println("*        TypeChecker Starts        *");
        System.out.println("************************************");
        System.out.println();
        System.out.println("CompilationUnit");

        n.block.accept(this);
    }

    public void visit(BlockStatementNode n) {

        System.out.println("BlockStatementNode");

        n.decls.accept(this);
        n.stmts.accept(this);
    }

    public void visit(Declarations n) {

        if (n.decls != null) {

            n.decl.accept(this);
            n.decls.accept(this);
        }
    }

    public void visit(DeclarationNode n) {

        System.out.println("DeclarationNode");

        n.type.accept(this);
        n.id.accept(this);
    }

    public void visit(TypeNode n) {

        System.out.println("TypeNode: " + n.basic);
        
        if(n.array != null) {

            n.array.accept(this);
        }
    }

    public void visit(ArrayTypeNode n) {

        System.out.println("ArrayTypeNode: " + n.size);
        
        if(n.type != null) {

            n.type.accept(this);
        }
    }

    public void visit(Statements n) {

        if (n.stmts != null) {

            n.stmt.accept(this);
            n.stmts.accept(this);
        }
    }

    public void visit(ParenthesesNode n) {

        System.out.println("ParenthesesNode");

        n.expr.accept(this);
    }

    public void visit(IfStatementNode n) {

        System.out.println("IfStatementNode");

        n.cond.accept(this);
        n.stmt.accept(this);

        if(n.else_stmt != null) {

            System.out.println("Else Clause");

            n.else_stmt.accept(this);
        }
    }

    public void visit(WhileStatementNode n) {

        System.out.println("WhileStatementNode");

        n.cond.accept(this);
        n.stmt.accept(this);
    }

    public void visit(DoWhileStatementNode n) {

        System.out.println("DoWhileStatementNode");

        n.cond.accept(this);
        n.stmt.accept(this);
    }

    public void visit(ArrayAccessNode n) {

        System.out.println("ArrayAccessNode");

        n.id.accept(this);
        n.index.accept(this);
    }

    public void visit(ArrayDimsNode n) {

        System.out.println("ArrayDimsNode");

        n.size.accept(this);

        if(n.dim != null) {

            n.dim.accept(this);
        }
    }

    public void visit(BreakStatementNode n) {

        System.out.println("BreakStatementNode");
    }

    public void visit(TrueNode n) {

        System.out.println("TrueNode");
    }

    public void visit(FalseNode n) {

        System.out.println("FalseNode");
    }

    public void visit(AssignmentNode n) {

        System.out.println("AssignmentNode");

        n.left.accept(this);

        IdentifierNode leftId = (IdentifierNode)n.left;
        Type leftType = leftId.type;

        println("In TypeChecker, AssignmentNode's left type: " + leftType);

        Type rightType = null;

        if(n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this);
        else if(n.right instanceof NumNode) {
            ((NumNode)n.right).accept(this);
            rightType = Type.Int;
        }
        else if(n.right instanceof RealNode)
            ((RealNode)n.right).accept(this);
        else if(n.right instanceof ArrayAccessNode)
            ((ArrayAccessNode)n.right).accept(this);
        else if(n.right instanceof ParenthesesNode)
            ((ParenthesesNode)n.right).accept(this);
        else {
            ((BinExprNode)n.right).accept(this);

            rightType = ((BinExprNode)n.right).type;
        }

        if(leftType == Type.Int)
            println("********** leftType is Type.Int");
        
        if(leftType == Type.Float && rightType == Type.Int) { 

            error("The right-hand side of an assignment is incomitible to the left-hand side " + leftId.id);
        }
    }

    public void visit(BinExprNode n) {

        System.out.println("BinExprNode: " + n.op);

        Type leftType = null;
        IdentifierNode leftId = null;

        if(n.left instanceof IdentifierNode) {
            ((IdentifierNode)n.left).accept(this);

            leftId = (IdentifierNode)n.left;
            leftType = leftId.type;
        }
        else if(n.left instanceof NumNode)
            ((NumNode)n.left).accept(this);
        else if(n.left instanceof RealNode)
            ((RealNode)n.left).accept(this);
        else if(n.left instanceof ArrayAccessNode)
            ((ArrayAccessNode)n.left).accept(this);
        else if(n.left instanceof ParenthesesNode)
            ((ParenthesesNode)n.left).accept(this);
        else
            ((BinExprNode)n.left).accept(this);
        
        Type rightType = null;

        if(n.right != null) {

            if(n.right instanceof IdentifierNode) {
                ((IdentifierNode)n.right).accept(this);

                IdentifierNode rightId = (IdentifierNode)n.right;
                rightType = rightId.type;
            }
            else if(n.right instanceof NumNode)
                ((NumNode)n.right).accept(this);
            else if(n.right instanceof RealNode)
                ((RealNode)n.right).accept(this);
            else if(n.right instanceof ArrayAccessNode)
                ((ArrayAccessNode)n.right).accept(this);
            else if(n.right instanceof ParenthesesNode)
                ((ParenthesesNode)n.right).accept(this);
            else {
                ((BinExprNode)n.right).accept(this);
            }

        } else {

        }

        if(leftType == Type.Float || rightType == Type.Float) {

            n.type = Type.Float;
        } else {

            n.type = Type.Int;
        }
    }

    public void visit(IdentifierNode n) {

        Type idType = n.type;

        System.out.println("IdentifierNode: " + n.id);
        println("****** In TypeChecker, IdentifierNode's type: " + idType);
    }

    public void visit (NumNode n) {      
          
        System.out.println("NumNode: " + n.value);
    }
    
    public void visit (RealNode n) {

        System.out.println("RealNode: " + n.value);
    }
}
