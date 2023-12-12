package assign6.parser ;

import assign6.visitor.* ;
import assign6.lexer.* ;
import assign6.ast.*;

import java.io.* ;

public class Parser extends ASTVisitor {

    public CompilationUnit cu = null ;
    public Lexer lexer        = null ;       

    public Token look = null;

    public Env top = null;

    int level = 0;
    String indent = "...";

    public Parser (Lexer lexer) { 

        this.lexer = lexer ;
        cu = new CompilationUnit() ;

        move();

        visit(cu) ;
    }
    
    public Parser () {

        cu = new CompilationUnit() ;

        move();

        visit(cu) ;
    }

    ////////////////////////////////////////
    //  Utility mothods
    ////////////////////////////////////////

    void move () {

	    try {
	    
	        look = lexer.scan() ;
	    }
	    catch(IOException e) {
	    
	        System.out.println("IOException") ;
	    }
    }

    void error (String s) {

	    //throw new Error ("near line " + lexer.line + ": " + s) ;
        println("Line " + lexer.line + ": " + s);
        exit(1);
    }

    void match (int t) {

	    try {
	    
	        if (look.tag == t)
		        move() ;
            else if (look.tag == Tag.EOF)
                error("Syntax error: \";\" or \"}\" expected");
	        else
		        error("Syntax error: \""  + (char)t + "\" expected") ;
	    }
	    catch(Error e) {
	    
	    }	
    }

    void print(String s) {

        System.out.print(s);
    }

    void println(String s) {

        System.out.println(s);
    }

    void exit(int n) {

        System.exit(n);
    }

    ////////////////////////////////////////
    // Visit Methods
    ////////////////////////////////////////
    
    public void visit (CompilationUnit n) {

        System.out.println("CompilationUnit");

        n.block = new BlockStatementNode();
        level ++;
        n.block.accept(this);
        level --;
    }

    public void visit (BlockStatementNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("BlockStatementNode");

        //Debugging
        //if (look.tag == '{')
        //    System.out.println("Matched with '{': " + look.tag);

        match('{');

        n.sTable = top;
        top = new Env(top);

        n.decls = new Declarations();
        level ++;
        n.decls.accept(this);
        level --;

        n.stmts = new Statements();
        n.stmts.accept(this);

        //Debugging
        //if (look.tag == '}')
        //    System.out.println("Matched with '}': " + look.tag);

        match('}');

        top = n.sTable;
    }

    public void visit (Declarations n) {

        for(int i = 0; i < level; i++) System.out.print(indent);
        System.out.println("Declarations");

        if (look.tag == Tag.BASIC) {

            n.decl = new DeclarationNode();
            level ++;
            n.decl.accept(this);
            level --;

            n.decls = new Declarations();
            n.decls.accept(this);
        }
    }

    public void visit (DeclarationNode n) {

        for(int i = 0; i < level; i++) System.out.print(indent);
        System.out.println("DeclarationNode");

        n.type = new TypeNode();
        level ++;
        n.type.accept(this);
        level --;

        n.id = new IdentifierNode();
        n.id.type = n.type.basic;
        level ++;
        n.id.accept(this);
        level --;

        top.put(n.id.w, n.id);

        IdentifierNode tmp = (IdentifierNode)top.get(n.id.w);
        println("&&&&&& tmp.type: " + tmp.type);
        println("&&&&&& tmp.w: " + tmp.w);
        
        match(';');
    }

    public void visit (TypeNode n) {

        for(int i = 0; i < level; i++) System.out.print(indent);
        System.out.println("TypeNode: " + look);

        if(look.toString().equals("int"))
            n.basic = Type.Int;
        else if(look.toString().equals("float"))    
            n.basic = Type.Float;

        match(Tag.BASIC);

        //If look is"[", this type should be array type
        if(look.tag == '[') {

            n.array = new ArrayTypeNode();
            level ++;
            n.array.accept(this);
            level --;
        }
    }

    public void visit (ArrayTypeNode n) {

        for(int i = 0; i < level; i++) System.out.print(indent);
        System.out.println("ArrayTypeNode");

        match('[');

        n.size = ((Num)look).value;

        for(int i = 0; i < level; i++) System.out.print(indent);
        System.out.println("Array Dimension: " + ((Num)look).value);

        match(Tag.NUM);

        match(']');

        if(look.tag == '[') {

            n.type = new ArrayTypeNode();
            level ++;
            n.type.accept(this);
            level --;
        }
    }

    public void visit (Statements n) {

        if(look.tag != '}' && look.tag != Tag.EOF) {

            level ++;
            n.stmt = parseStatementNode(n.stmt);
            level --;

            n.stmts = new Statements();
            level ++;
            n.stmts.accept(this);
            level --;
        }
    }

    public StatementNode parseStatementNode(StatementNode stmt) {
        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("**** parseStatementNode");

        // Add this line to print the current token
        System.out.println("Current token: " + look);

        switch(look.tag) {
            case Tag.ID:
                stmt = new AssignmentNode();
                ((AssignmentNode)stmt).accept(this);
                return stmt;
        
            case Tag.IF:
                stmt = new IfStatementNode();
                ((IfStatementNode)stmt).accept(this);
                return stmt;
        
            case Tag.WHILE:
                stmt = new WhileStatementNode();
                ((WhileStatementNode)stmt).accept(this);
                return stmt;
        
            case Tag.DO:
                stmt = new DoWhileStatementNode();
                ((DoWhileStatementNode)stmt).accept(this);
                return stmt;
        
            case Tag.BREAK:
                stmt = new BreakStatementNode();
                ((BreakStatementNode)stmt).accept(this);
                return stmt;
        
            default:
                // Add this line to print the unrecognized token
                System.out.println("Unrecognized token: " + look.tag);
                error("Syntax error: Statement needed");
                return stmt;
        }
    
}


    public void visit (ParenthesesNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("ParenthesesNode");

        match('(');

        if(look.tag == '(') {

            n.expr = new ParenthesesNode();
            level ++;
            n.expr.accept(this);
            level --;

        } else if(look.tag == Tag.ID) {

            n.expr = new IdentifierNode();
            level ++;
            n.expr.accept(this);
            level --;

            if(look.tag == '[') {

                n.expr = parseArrayAccessNode((IdentifierNode)n.expr);
            }

        } else if(look.tag == Tag.NUM) {

            n.expr = new NumNode();
            level ++;
            n.expr.accept(this);
            level --;

        } else if(look.tag == Tag.REAL) {

            n.expr = new RealNode();
            level ++;
            n.expr.accept(this);
            level --;

        } else if(look.tag == Tag.TRUE) {

            n.expr = new TrueNode();
            level ++;
            n.expr.accept(this);
            level --;
            
        } else if(look.tag == Tag.FALSE) {

            n.expr = new FalseNode();
            level ++;
            n.expr.accept(this);
            level --;
            
        }

        if (look.tag != ')') {

            level ++;
            n.expr = parseBinExprNode(n.expr, 0);
            level --;
        }

        match(')');

    }

    public void visit (IfStatementNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("IfStatementNode");

        match(Tag.IF);
        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("operator: if");

        n.cond = new ParenthesesNode();
        level ++;
        n.cond.accept(this);
        level --;

        if(look.tag == '{') {

            n.stmt = new BlockStatementNode();

            level ++;
            n.stmt.accept(this);
            level --;

        } else {

            n.stmt = parseStatementNode(n.stmt);
        }

        if (look.tag == Tag.ELSE) {

            match(Tag.ELSE);

            for(int i=0; i<level; i++) System.out.print(indent);
            System.out.println("operator: else");

            if(look.tag == '{') {

                n.else_stmt = new BlockStatementNode();

                level ++;
                n.else_stmt.accept(this);
                level --;

            } else {

                n.else_stmt = parseStatementNode(n.else_stmt);
            }
        }

    }

    public void visit (WhileStatementNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("WhileStatementNode");

        match(Tag.WHILE);
        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("operator: while");

        n.cond = new ParenthesesNode();
        level ++;
        n.cond.accept(this);
        level --;

        if(look.tag == '{') {

            n.stmt = new BlockStatementNode();

            level ++;
            n.stmt.accept(this);
            level --;

        } else {

            n.stmt = parseStatementNode(n.stmt);
        }
    }

    public void visit (DoWhileStatementNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("DoWhileStatementNode");

        match(Tag.DO);
        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("operator: do");

        if(look.tag == '{') {

            n.stmt = new BlockStatementNode();

            level ++;
            n.stmt.accept(this);
            level --;

        } else {

            n.stmt = parseStatementNode(n.stmt);
        }

        match(Tag.WHILE);
        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("operator: while");

        n.cond = new ParenthesesNode();
        level ++;
        n.cond.accept(this);
        level --;

        match(';');
    }

    public void visit (ArrayAccessNode n) {

    }

    public void visit (ArrayDimsNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("ArrayDimsNode");

        match('[');

        ExprNode index = null;

        if(look.tag == '(') {

            index = new ParenthesesNode();
            level ++;
            ((ParenthesesNode)index).accept(this);
            level --;
        
        } else if(look.tag == Tag.ID) {

            index = new IdentifierNode();
            level ++;
            ((IdentifierNode)index).accept(this);
            level --;
        
        } else if(look.tag == Tag.NUM) {

            index = new NumNode();
            level ++;
            ((NumNode)index).accept(this);
            level --;
        
        } 

        if(look.tag != ']') {

            level ++;
            index = parseBinExprNode(index, 0);
            level --;
        }

        match(']');

        n.size = index;

        if(look.tag == '[') {

            n.dim = new ArrayDimsNode();
            level ++;
            n.dim.accept(this);
            level --;
        }
    }

    ExprNode parseArrayAccessNode(IdentifierNode id) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("parseArrayAccessNode");

        ExprNode index = new ArrayDimsNode();
        level ++;
        index.accept(this);
        level --;

        return new ArrayAccessNode(id, index);
    }

    public void visit (AssignmentNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("AssignmentNode");

        n.left = new IdentifierNode();

        level ++;
        n.left.accept(this);
        level --;

        IdentifierNode id = (IdentifierNode)top.get(((IdentifierNode)n.left).w);
        println("In Parser, AssignmentNode's left type: " + id.type);

        ((IdentifierNode)n.left).type = id.type;

        if (look.tag == '[') {

            n.left = parseArrayAccessNode((IdentifierNode)n.left);
        }
        
        match('=');
        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("operator: =");

        ExprNode rhs_assign = null;

        if(look.tag == '(') {

            rhs_assign = new ParenthesesNode();
            level ++;
            ((ParenthesesNode)rhs_assign).accept(this);
            level --;

        } else if(look.tag == Tag.ID) {
            
            rhs_assign = new IdentifierNode();
            level ++;
            ((IdentifierNode)rhs_assign).accept(this);
            level --;

            if (look.tag == '[') {

                rhs_assign = parseArrayAccessNode(((IdentifierNode)rhs_assign));
            }  

        } else if(look.tag == Tag.NUM) {

            rhs_assign = new NumNode();
            level ++;
            ((NumNode)rhs_assign).accept(this);
            level --;

        } else if(look.tag == Tag.REAL) {

            rhs_assign = new RealNode();
            level ++;
            ((RealNode)rhs_assign).accept(this);
            level --;
        }

        if(look.tag == ';') {   // ex: a = 19 ;

            n.right = rhs_assign;

        } else if(isOperator(look.tag)) {    // ex: b = a + b * c ;

            for(int i=0; i<level; i++) System.out.print(indent);
            System.out.println("operator: " + look);

            level ++;
            //Build AST for binary expressions with operator precedence
            n.right = (BinExprNode) parseBinExprNode (rhs_assign, 0);
            level --;

            //System.out.println("**** Root Node Operator: " + ((BinExprNode)n.right).op);
        } else {

            error("Syntax error in AssignmentNode");
        }

        match(';');
    }

    boolean isOperator(int op) {

        switch(op) {
            case '*': case '/': case '%':
            case '+': case '-':
            case '<': case '>':
            case Tag.LE: case Tag.GE:
            case Tag.EQ: case Tag.NE: return true;

            default: 
                return false;
        }
    }

    public void visit (BreakStatementNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("BreakStatementNode: break");

        if(look.tag != Tag.BREAK)
            error("Syntax error: \"break\" needed");

        match(Tag.BREAK);
        match(';');
    }

    public void visit (TrueNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("TrueNode");

        if(look.tag != Tag.TRUE)
            error("Syntax error: \"true\" needed");

        match(Tag.TRUE);
    }

    public void visit (FalseNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("FalseNode");
        
        if(look.tag != Tag.FALSE)
            error("Syntax error: \"false\" needed");

        match(Tag.FALSE);
    }

    public void visit (BinExprNode n) {

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("BinExprNode");

        if(look.tag == '(') {

            n.left = new ParenthesesNode();
            level ++;
            ((ParenthesesNode)n.left).accept(this);
            level --;

        } else if(look.tag == Tag.ID) {

            n.left = new IdentifierNode();
            level ++;
            ((IdentifierNode)n.left).accept(this);
            level --;

            if (look.tag == '[') {

                n.left = parseArrayAccessNode((IdentifierNode)n.left);
            }
            
        } else if(look.tag == Tag.NUM) {

            n.left = new NumNode();
            level ++;
            ((NumNode)n.left).accept(this);
            level --;
            
        } else if(look.tag == Tag.REAL) {

            n.left = new RealNode();
            level ++;
            ((RealNode)n.left).accept(this);
            level --;
            
        }

        for(int i=0; i<level; i++) System.out.print(indent);
        System.out.println("&&&&&& operator: " + look);
        System.out.println("&&&&&& n.left: " + n.left);

        level ++;
        BinExprNode binary = (BinExprNode) parseBinExprNode(n.left, 0);
        n.op = binary.op;
        n.right = binary.right;
        level --;

    }
    
    

    /*
      < Operator Precedence >

      Operators are listed top to bottom in ascending precedence.

      01. assignment: =, +=, -=, *=, /=, %=, &=, ^=, \=, <<=, >>=, >>>=
      02. tenary: ? :
      03. logical: OR ||
      04. logical: AND &&
      05. bitwise inclusive: OR |
      06. bitwise exclusive: OR ^
      07. bitwise: AND &
      08. equality: ==, !=
      09. relational: <, >, <=, >=
      10. shift: <<, >>, >>>
      11. additive: +, -
      12. multiplicative: *, /, %
      13. unary: ++expr, --expr, +expr, -expr
      14. postfix: expr++, expr--
     */

    int getPrecedence(int op) {

        switch(op) {

            case '*': case '/': case '%': return 12; // multiplicative
            case '+': case '-':           return 11; // additive
            case '<': case '>':           return 9; // relational
            case Tag.LE: case Tag.GE:     return 9; // relational
            case Tag.EQ: case Tag.NE:     return 8; // equality

            default:
            return -1; // ';'
        }
    }

    ExprNode parseBinExprNode(ExprNode lhs, int precedence) {

        // If the current op's precedence is higher than that of
        // the previous, then keep traveling down to create a new
        // BinExprNode for binary expressions with higher precedence
        // Otherwise, create a new BinExprNode for current lhs and rhs.
        while(getPrecedence(look.tag) >= precedence) {

            Token token_op = look;
            int op = getPrecedence(look.tag);

            move();

            for(int i=0; i<level;  i++) System.out.print(indent);
       
            //Debugging
            //if (look.tag == Tag.NUM)
            //    System.out.println("&&&& NumNode");
            //else if (look.tag == Tag.ID)
            //    System.out.println("**** IdentifierNode");

            ExprNode rhs = null;

            if(look.tag == '(') {

                rhs = new ParenthesesNode();
                level ++;
                ((ParenthesesNode)rhs).accept(this);
                level --;

            } else if(look.tag == Tag.ID) {

                rhs = new IdentifierNode();
                level ++;
                ((IdentifierNode)rhs).accept(this);
                level --;

                if(look.tag == '[') {

                    rhs = parseArrayAccessNode(((IdentifierNode)rhs));
                }

            } else if(look.tag == Tag.NUM) {

                rhs = new NumNode();
                level ++;
                ((NumNode)rhs).accept(this);
                level --;

            } else if(look.tag == Tag.REAL) {

                rhs = new RealNode();
                level ++;
                ((RealNode)rhs).accept(this);
                level --;
            }

            //System.out.println("op = " + op);
            //System.out.println("token_op = " + token_op);
            //System.out.println("next_op = " + getPrecedence(look.tag));

            // Whenever the next op's precedence is higher than that
            // of the current operator, keep recursively calling itself
            while (getPrecedence(look.tag) > op) {

                rhs = parseBinExprNode(rhs, getPrecedence(look.tag));
            }

            lhs = new BinExprNode(token_op, lhs, rhs);

            //System.out.println("**** Created BinExprNode with op " + token_op)
        }

        return lhs;
    }

    public void visit (NumNode n) {

        n.value = ((Num)look).value;

        if(look.tag != Tag.NUM)
            error("Syntax error: Integer number needed, instead of " + n.value);

        match(Tag.NUM);     //expect look.tag == Tag.NUM

        for (int i=0; i<level; i++) System.out.print(indent);
        n.printNode();
        //System.out.println("look in IdentifierNode: " + look);
    }

    public void visit (RealNode n) {

        n.value = ((Real)look).value;

        if(look.tag != Tag.REAL)
            error("Syntax error: Real number needed, instead of " + n.value);

        match(Tag.REAL);     //expect look.tag == Tag.REAL

        for (int i=0; i<level; i++) System.out.print(indent);
        n.printNode();
        //System.out.println("look in IdentifierNode: " + look);
    }

    public void visit (IdentifierNode n) {

        n.id = look.toString();
        n.w = (Word)look;
        println("***** n.type: " + n.type);

        if(look.tag != Tag.ID)
            error("Syntax error: Identifier or variable needed, instead of " + n.id);

        match(Tag.ID);      //expect look.tag == Tag.ID

        for (int i=0; i<level; i++) System.out.print(indent);
        n.printNode();
        //System.out.println("IdentifierNode: " + n.id);
        //System.out.println("look in IndentifierNode: " + look);
    }

}
