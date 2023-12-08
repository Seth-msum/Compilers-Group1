package assign6.parser;

import java.io.* ;
//import java.util.ArrayList;
// import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

import assign6.ast.*;
import assign6.lexer.*;
import assign6.visitor.*;


// Make sure Word.minus is recognised as a minus.
public class Parser extends ASTVisitor {

    public CompilationUnit      cu   = null ;
    public Lexer                lexer       = null ;
    public Token                look        = null ;
    public Token                prelook     = null ;

    // Varaibles related to scoping.
    public Env                  top = null ;
    public BlockStatementNode   enclosingBlock = null ;
    // Example 
    public          boolean eclosingExample = false ;

    // Diagnostic tools
    public          boolean diagnostic = false ;

    // AST depth variables
    int             level       = 0 ;
    String          indent      = "..." ;

    public Parser (Lexer lexer) {
        this.lexer = lexer ;
        cu = new CompilationUnit() ;
        move() ;
        visit(cu) ;
    }

    public Parser () {
        cu = new CompilationUnit() ;
        move() ;
        visit(cu) ;
    }

    ////////////////////////////////////////
    //  Utility methods
    ////////////////////////////////////////
    
    void move() {
        prelook = look ;
        try { 
            look = lexer.scan() ; 
        }
        catch (IOException e) {
            System.out.println("IOException") ;
        }
    }

    void error (String s) {
        //throw new Error("near line " + lexer.line + ": " + s) ;
        println("Line " + lexer.line + " " + s ) ;
        exit(1) ;
    }
    void error (String s, String s2) {
        println(s + " on line "+ lexer.line + ": " + s2) ;
        exit(1) ;
    }

    void match (int t) {
        try {
            if (look.tag == t) {
                move() ;
            }
            else if (look.tag == Tag.EOF)
                error("Syntax error: \";\" or \"}\" expected after " + prelook.toString()) ;
            else
                error("Syntax error: \"" + (char)t + "\" expected after " + prelook.toString()) ;
        }
        catch (Error e) {
            System.out.println("Unnown error in match");
            e.printStackTrace();
            System.exit(-1) ;
        }
    }

    void match (int t, String neatError) {
        try {
            if (diagnostic) {
                println("match test want: " + t + " ") ;
                println("match test got : " + look.tag + " " + look.toString()) ;
            }
            
            if (look.tag == t) {
                move() ;
            }
            else
                // error("or line " + (lexer.line-1) + ": " + neatError + "\""+(char)t + "\"") ;
                error(neatError) ;
        }
        catch (Error e) {
            System.out.println("Unnown error in neat match");
            System.exit(-1) ;
        }
    }

    void print(String s){
        System.out.print(s);
    }

    void println(String s){
        System.out.println(s);
    }

    void printBlock(String s) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println(s);
    }

    void exit(int n) {
        System.exit(n) ;
    }

    private boolean opt(int... tags) {
            // its variable length argument like Pythons *args
        
        for (int tag : tags)
            if (look.tag == tag)
                return true ;
        return false ;
    }

    ////////////////////////////////////////
    // Visit Methods
    ////////////////////////////////////////
    
    public void visit(CompilationUnit n) {
        printBlock("CompilationUnit");

        n.block = new BlockStatementNode(null) ;

        level++ ;
        n.block.accept(this) ;
        level-- ;

        }

    public void visit (BlockStatementNode n) {
        /*
         * This block first matches the '{'(Tag.BST), then 
         */

        printBlock("BlockStatmentNode");

        match(Tag.BST, "missing '{' before start of statements ") ;

        // scope into new set of variables.
        n.sTable = top ;
        top = new Env(top) ; 
        enclosingBlock = n ;

        level++ ;
        while (opt(Tag.BASIC)) {
            DeclarationNode decl = new DeclarationNode() ;
            n.decls.add(decl) ;
            decl.accept(this) ;
        }
        level-- ;

        level++ ;
        int check = 0 ;
        StatementNode tmp ;
        while (opt(Tag.ID, Tag.IF, Tag.WHILE, Tag.DO, Tag.BREAK, Tag.BST, Tag.BASIC)) {
            tmp = parseStatementNode(n) ;
            if (tmp == null) break ;
            n.stmts.add(tmp) ;
            check = 1 ;
        }
        if (check == 0)
            error("blockstatement missing a single statement in brackets '{}'.") ;
        level-- ;
        if(diagnostic)
            println(look.toString()) ;
        match(Tag.BET, "missing closing bracket '}' ") ;

        //scope back out a level of variables.
        top = n.sTable ;
        enclosingBlock = n.parent ;
    }

    public void visit(DeclarationNode n) {
        printBlock("DeclarationNode");

        n.type = new TypeNode() ;
        level++ ;
        n.type.accept(this) ;
        level-- ;
        
        n.id = new IdentifierNode() ;
        n.id.type = n.type.basic ; //add the type to the character node
        level++ ;
        n.id.accept(this) ;
        level-- ;
        // The IdentifierNode should also take in the array characteristic.

        // Store in current scope enviroment variables.
        top.put(n.id.w, n.id) ;

        match(';',"Illegal declaration statement, Needs a semicolon. ") ; //J
    }

    public void visit (TypeNode n) {
        printBlock("TypeNode: " + look) ;
        if (look.toString().equals("int"))
            n.basic = Type.Int ;
        else if (look.toString().equals("float"))
            n.basic = Type.Float ;
        else if (look.toString().equals("bool"))
            n.basic = Type.Bool ;
        else if (look.toString().equals("char"))
            n.basic = Type.Char ;
        else {
            error("unnown type or missing type for declaration, please only use supported type");
        }
        //custom error for this might not be needed since all basics are accounted for.
        match(Tag.BASIC) ; //J

        //If look is "[", this type should be array type
        level++ ;
        while(opt('[')) {
            ArrayTypeNode dim = new ArrayTypeNode() ;
            n.dimensions.add(dim) ;
            dim.accept(this) ;
        }
        level-- ;
    }

    public void visit(ArrayTypeNode n) {
        printBlock("ArrayTypeNode");
        
        match('[') ;

        if (look.tag != Tag.NUM)
            error("Declaration of array requires an Integer inside the []") ;

        n.size = ((Num)look).value ;
        
        printBlock("Array Dimentsion: " + ((Num)look).value);

        //println(look.toString());
        match(Tag.NUM) ;
        //println(look.toString());

        match(']', "recieved " + look.toString()) ;

    }

    // This is depreciated because of updates from lab 10 and 11
    // public void visit (Statements n) {

    //     if(look.tag != '}' && look.tag != Tag.EOF) {

    //         level ++;
    //         n.stmt = parseStatementNode(n.stmt);
    //         level --;

    //         n.stmts = new Statements();
    //         level ++;
    //         n.stmts.accept(this);
    //         level --;
    //     }
    // }

    // This one accepts additional declarations
    public StatementNode parseStatementNode (BlockStatementNode n) {
        printBlock("****N parseStatementNode") ;
        
        StatementNode stmt ;
            switch (look.tag) {  
                case Tag.ID:
                    stmt = new AssignmentNode() ;
                    ((AssignmentNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.IF:
                    stmt = new IfStatementNode() ;
                    ((IfStatementNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.WHILE:
                    stmt = new WhileStatementNode() ;
                    ((WhileStatementNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.DO:
                    stmt = new DoWhileStatementNode() ;
                    ((DoWhileStatementNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.BREAK:
                    stmt = new BreakStatementNode() ;
                    ((BreakStatementNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.BASIC:
                    while (opt(Tag.BASIC)) {
                        DeclarationNode decl = new DeclarationNode() ;
                        n.decls.add(decl) ;
                        decl.accept(this) ;
                    }
                    return parseStatementNode(n) ;
                case Tag.BST:
                    stmt = new BlockStatementNode(n) ;
                    ((BlockStatementNode)stmt).accept(this) ;
                    return stmt ;
                    //error("adding declarations after statements are not supported yet") ;
                default:
                    //error("Syntax error: Statement needed") ;
                    System.out.println("returned empty");
                    return null ;
            }   
    }

    // This does not accect declarations. 
    public StatementNode parseStatementNode () {
        printBlock("**** parseStatementNode") ;
        
        StatementNode stmt ;
            switch (look.tag) {  
                case Tag.ID:
                    stmt = new AssignmentNode() ;
                    ((AssignmentNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.IF:
                    stmt = new IfStatementNode() ;
                    ((IfStatementNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.WHILE:
                    stmt = new WhileStatementNode() ;
                    ((WhileStatementNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.DO:
                    stmt = new DoWhileStatementNode() ;
                    ((DoWhileStatementNode)stmt).accept(this) ;
                    return stmt ;
                case Tag.BREAK:
                    stmt = new BreakStatementNode() ;
                    ((BreakStatementNode)stmt).accept(this) ;
                    return stmt ;
                // case Tag.BASIC:
                //     error("adding declarations after statements are not supported yet") ;
                default:
                    //error("Syntax error: Statement needed") ;
                    return null ;
            }   
    }

    public void visit (ParenthesesNode n) {
        printBlock("ParenthesesNode");

        match('(') ;

        if (look.tag == '(') {
            n.expr = new ParenthesesNode() ;
            level++ ;
            n.expr.accept(this) ;
            level-- ;
        } 
        if (look.tag == Tag.ID) {
            n.expr = new IdentifierNode() ;
            level++ ;
            n.expr.accept(this) ;
            level-- ;
            if (look.tag == '[') {
                n.expr = parseArrayAccessNode((IdentifierNode)n.expr) ;
            }
        }
        else if (look.tag == Tag.NUM) {
            n.expr = new NumNode() ;
            level++ ;
            n.expr.accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.REAL) {
            n.expr = new RealNode() ;
            level++ ;
            n.expr.accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.TRUE) {
            n.expr = new TrueNode() ;
            level++ ;
            n.expr.accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.FALSE) {
            n.expr = new FalseNode() ;
            level++ ;
            n.expr.accept(this) ;
            level-- ;
        }
        if (look.tag != ')') {
            if (!isOperator(look.tag))
                error("expected a binary operation") ;
            level++ ;
            n.expr = parseBinExprNode(n.expr, 0) ;
            level-- ;
            System.out.println("**** Root Node operator: " + ((BinExprNode)n.expr).op) ;
        }
        match(')') ;
    }

    public void visit (IfStatementNode n) {

        printBlock("IfStatementNode");

        //This came from one of the labs and it explains how to build in code by the parser.
        if (eclosingExample) {
            IdentifierNode leftID = new IdentifierNode(new Word("i", Tag.ID), Type.Int) ;
            AssignmentNode newAssign1 = new AssignmentNode(leftID, new NumNode(new Num(2))) ;
            AssignmentNode newAssign2 = new AssignmentNode(leftID, new NumNode(new Num(19))) ;
            AssignmentNode newAssign3 = new AssignmentNode(leftID, new NumNode(new Num(212))) ;
            enclosingBlock.stmts.add(newAssign1) ;
            enclosingBlock.stmts.add(newAssign2) ;
            enclosingBlock.stmts.add(newAssign3) ;
            AssignmentNode newAssign4 = new AssignmentNode(leftID, new NumNode(new Num(518))) ; 
            int idx = enclosingBlock.stmts.indexOf(newAssign2) ;
            enclosingBlock.stmts.add(idx, newAssign4) ;
            for (StatementNode s : enclosingBlock.stmts)
                System.out.println(s) ;
            if (enclosingBlock.stmts.contains(n))
                System.out.println("********* enclosingBlock has this IfStatementNode") ;
            else
                System.out.println("######### enclosingBlock doesn't have this IfStatementNode") ;
        }   

        match(Tag.IF) ;
        
        printBlock("operator: if");

        n.cond = new ParenthesesNode() ;
        level++ ;
        n.cond.accept(this) ;
        level-- ;

        if (look.tag == Tag.BST)  {
            n.stmt = new BlockStatementNode(enclosingBlock) ;
            level++ ;
            n.stmt.accept(this) ;
            level-- ;
        } else {
            level++ ;
            n.stmt = parseStatementNode() ;
            //match(';') ;
            level-- ;
        }

        if (look.tag == Tag.ELSE) {
            match(Tag.ELSE);
            
            printBlock("operator: else");

            if (look.tag == Tag.BST) {
                n.else_stmt = new BlockStatementNode(enclosingBlock) ;
                level++ ;
                n.else_stmt.accept(this) ;
                level-- ;
            }
            else {
                level++ ;
                n.else_stmt = parseStatementNode() ;
                level-- ;
            }
        }
    }

    public void visit(WhileStatementNode n) {
        printBlock("WhileStatementNode");

        match(Tag.WHILE) ;

        printBlock("operator: while");
        
        n.cond = new ParenthesesNode() ;
        level++ ;
        n.cond.accept(this) ;
        level-- ;

        if (look.tag == Tag.BST) { // {
            n.stmt = new BlockStatementNode(enclosingBlock) ;
            level++ ;
            n.stmt.accept(this) ;
            level-- ;
        } else {
            level++ ;
            n.stmt = parseStatementNode() ;
            level-- ;
        }
    }

    public void visit(DoWhileStatementNode n) {
        
        printBlock("DoWhileStatementNode");

        match(Tag.DO);
        
        printBlock("operator: do");

        if (look.tag == Tag.BST) {
            n.stmt = new BlockStatementNode(enclosingBlock) ;
            level++ ;
            n.stmt.accept(this) ;
            level-- ;
        }
        else {
            n.stmt = parseStatementNode() ;
        }

        match(Tag.WHILE) ;

        printBlock("operator: while");

        n.cond = new ParenthesesNode() ;
        level++ ;
        n.cond.accept(this) ;
        level-- ;

        match(';') ;
    }

    public void visit(ArrayAccessNode n) {

    }

    public void visit(ArrayDimsNode n ) {
        
        printBlock("ArrayDimsNode");

        match('[') ;

        ExprNode index = null ;
        if (look.tag == '(') {
            index = new ParenthesesNode() ;
            level++ ;
            ((ParenthesesNode)index).accept(this) ;
            level-- ;
        }
        else if ( look.tag == Tag.ID) {
            index = new IdentifierNode() ;
            level++ ;
            ((IdentifierNode)index).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.NUM) {
            index = new NumNode() ;
            level++ ;
            ((NumNode)index).accept(this) ;
            level-- ;
        }

        if (look.tag != ']') {
            if (!isOperator(look.tag))
                error("expected a binary operation");
            level++ ;
            index = parseBinExprNode(index,0) ;
            level-- ;

            System.out.println("**** Root Node operator: " + ((BinExprNode)index).op) ;
        }

        match(']') ;

        n.size = index ;

    }

    ArrayAccessNode parseArrayAccessNode (IdentifierNode id) {

        printBlock("parseArrayAccessNode") ;

        List<ArrayDimsNode> dimsArray = new ArrayList<ArrayDimsNode>() ;
        ArrayDimsNode tmp ;
        while (look.tag == '[') {
            tmp = new ArrayDimsNode() ;
            level++ ;
            tmp.accept(this) ;
            level-- ;
            dimsArray.add(tmp) ;
        }
        //ArrayDimsNode index = new ArrayDimsNode() ;

        return new ArrayAccessNode(id, dimsArray) ;
    }

    public void visit (AssignmentNode n) { // major changes
        
        printBlock("AssignmentNode");

        n.left = new IdentifierNode() ;        
        level++ ;
        n.left.accept(this) ;
        level-- ;

        IdentifierNode id =  (IdentifierNode)top.get(((IdentifierNode)n.left).w) ;

        if (id == null) error("Declaration error: " + ((IdentifierNode)n.left).w + " was not declared or declared properly") ;
        if (diagnostic) println("In Parser, AssignmentNode's left type: "+ id.type) ;
        
        ((IdentifierNode)n.left).type = id.type ;

        if (look.tag == '[') {

            n.left = parseArrayAccessNode((IdentifierNode)n.left) ;
        }

        match('=') ;

        printBlock("operator: =");

        ExprNode rhs_assign = null ;
        if (look.tag == '(') {
            rhs_assign = new ParenthesesNode() ;
            level++ ;
            ((ParenthesesNode)rhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.ID) {
            rhs_assign = new IdentifierNode() ;
            level++ ;
            ((IdentifierNode)rhs_assign).accept(this) ;
            level-- ;
            if (look.tag == '[') {
                rhs_assign = parseArrayAccessNode(((IdentifierNode)rhs_assign)) ;
            }
        } 
        else if (look.tag == Tag.NUM) {
            rhs_assign = new NumNode() ;
            level++ ;
            ((NumNode)rhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.REAL) {
            rhs_assign = new RealNode() ;
            level++ ;
            ((RealNode)rhs_assign).accept(this) ;
            level-- ;
        }

        if (look.tag == ';') {
            n.right = rhs_assign ;
        }
        else if(isOperator(look.tag)){
            printBlock("operator: " + look);

            level++ ;
            n.right = (BinExprNode) parseBinExprNode( rhs_assign, 0) ;
            level-- ;
            System.out.println("**** Root Node operator: " + ((BinExprNode)n.right).op) ;
        }
        else {
            error("expected ';' or a binary operator") ;
        }

        match(';');
    }

    boolean isOperator(int op) {

        switch(op) {
            case '*': case '/': case '%':
            case '+': case Tag.MINUS:
            case '<': case '>':
            case Tag.LE: case Tag.GE:
            case Tag.EQ: case Tag.NE: return true;

            default: 
                return false;
        }
    }

    public void visit(BreakStatementNode n) {
    
        printBlock("BreakStatementNode: break");

        match(Tag.BREAK) ;

        match(';') ;
    }
    
    public void visit (TrueNode n) {
    
        printBlock("TrueNode");
        match(Tag.TRUE) ;
    }
    
    public void visit (FalseNode n) {
        
        printBlock("FalseNode");
        match(Tag.FALSE) ;
    }

    public void visit(BinExprNode n) {
        
        printBlock("BinExprNode");

        if (look.tag == '(') {
            n.left = new ParenthesesNode() ;
            level++ ;
            ((ParenthesesNode)n.left).accept(this) ;
            level-- ;
        }

        else if (look.tag == Tag.ID) {
            n.left = new IdentifierNode() ;
            level++ ;
            ((IdentifierNode)n.left).accept(this) ;
            level-- ;
            if (look.tag == '[') {
                n.left = parseArrayAccessNode((IdentifierNode)n.left) ;
            }
        } 

        else if (look.tag == Tag.NUM) {
            n.left = new NumNode() ;
            level++ ;
            ((NumNode)n.left).accept(this) ;
            level-- ;
        }

        // if (diagnostic) {
            System.out.println("&&&&&& operator: " + look) ;
            System.out.println("&&&&&& n.left: " + n.left) ;
        // }

        if (!isOperator(look.tag))
            error("expected a binary operation");
        level++ ;
        BinExprNode binary = (BinExprNode) parseBinExprNode((ExprNode)n.left, 0) ;
        n.op = binary.op ;
        n.right = binary.right ;
        level -- ;
        System.out.println("**** Root Node operator: " + ((BinExprNode)n.right).op) ;
    }

    /*
        < Operator Precedence >
        Operators are listed top to bottom in ascending precedence.

        01. assignment =, +=, -=, *=, /=, %=, &=, ^=, \=, <<=, >>=, >>>=
        02. tenary ? :
        03. logical OR ||
        04. logical AND &&
        05. bitwise inclusive OR |
        06. bitwise exclusive OR ^
        07. bitwise AND &
        08. equality ==, !=
        09. relational <, >, <=, >=
        10. shift <<, >>, >>>
        11. additive +, -
        12. multiplicative *, /, %
        13. unary ++expr, --expr, +expr, -expr
        14. postfix expr++, expr--
     */
    int getPrecedence (int op) {
        switch ( op ) {
            case '*':           
            case '/':
            case '%':
                return 12 ;     //multiplicative
            case '+':
            case Tag.MINUS:
                return 11 ;     //additive
            case '<':
            case '>':
            case Tag.LE:
            case Tag.GE:
                  return 9 ;  //relational
            case Tag.EQ:
            case Tag.NE:
                  return 8 ;  //equality
            case ';':
                return -1 ;
            default:
                return -2 ; // This is a test on my end: Judah
        }
    }

    ExprNode parseBinExprNode( ExprNode lhs, int precedence) {
        //printBlock("parseBinExprNode");
        while ( getPrecedence(look.tag) >= precedence) {
            Token token_op = look ;
            int op = getPrecedence(look.tag) ;
            move() ;
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            ExprNode rhs = null ;
            if (look.tag == '(') {
                rhs = new ParenthesesNode() ;
                level++ ;
                ((ParenthesesNode)rhs).accept(this) ;
                level-- ;
            }else if (look.tag == Tag.ID) {
                rhs = new IdentifierNode() ;
                level++ ;
                ((IdentifierNode)rhs).accept(this) ;
                level-- ;
                if (look.tag == '[') {
                    rhs = parseArrayAccessNode(((IdentifierNode)rhs)) ;
                }
            }
            else if (look.tag == Tag.NUM) {
                rhs = new NumNode() ;
                level++ ;
                ((NumNode)rhs).accept(this) ;
                level-- ;
            }
            else if (look.tag == Tag.REAL) {
                rhs = new RealNode() ;
                level++ ;
                ((RealNode)rhs).accept(this) ;
                level-- ;
            }
            while ( getPrecedence(look.tag) > op ) {
                rhs = parseBinExprNode( rhs, getPrecedence(look.tag) ) ;
            }
            lhs = new BinExprNode(token_op, lhs, rhs);
        } 
        return lhs ;
    }

    public void visit(NumNode n) {
        n.value = ((Num)look).value ;
        if (look.tag != Tag.NUM)
            error("Syntax error: Integer number needed, instead of " + n.value) ;
        match(Tag.NUM) ;
        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
    }

    public void visit(RealNode n) {
        n.value = ((Real)look).value ;
        if (look.tag != Tag.REAL)
            error("Syntax error: Real number needed, instead of " + n.value) ;
        match(Tag.REAL) ;
        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
    }

    public void visit(IdentifierNode n) {
        n.id = look.toString() ;
        n.w = (Word)look ;
        //println("****** n.type: " + n.type) ;
        if (look.tag != Tag.ID)
            error("Syntax error: Identifier or variable needed, instead of " + n.id) ;
        match(Tag.ID) ;
        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
    }  
}
