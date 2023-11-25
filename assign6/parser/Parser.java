package assign6.parser;

import java.io.* ;
//import java.util.ArrayList;
// import java.util.Hashtable;

import assign6.ast.*;
import assign6.lexer.*;
import assign6.visitor.*;


public class Parser extends ASTVisitor {

    public CompilationUnit      cu   = null ;
    public Lexer                lexer       = null ;
    public Token                look        = null ;

    //Varaibles related to scoping.
    public Env                  top = null ;
    public BlockStatementNode   enclosingBlock = null ; 
    public          boolean eclosingExample = true ;

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

    void move() {
        try { look = lexer.scan() ; }
        catch (IOException e) {
            System.out.println("IOException") ;
        }
    }
    void error (String s) {
        //throw new Error("near line " + lexer.line + ": " + s) ;
        println("Line " + lexer.line + " " + s ) ;
        exit(1) ;
    }

    void match (int t) {
        try {
            if (look.tag == t) {
                move() ;
            }
            else if (look.tag == Tag.EOF)
                error("Syntax error: \";\" or \"}\" expected") ;
            else
                error("Syntax error: \"" + (char)t + "\" expected") ;
        }
        catch (Error e) {
            System.out.println("Unnown error in match");
            System.exit(-1) ;
        }
    }
    void match (int t, String neatError) {
        try {
            if (look.tag == t) {
                move() ;
            }
            else
                error("or line " + (lexer.line-1) + ": " + neatError + "\""+(char)t + "\"") ;
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

    void exit(int n) {
        System.exit(n) ;
    }

    private boolean opt(int... tags) { //WTF is this syntax?
            // its variable length argument like Pythons *args
        
        for (int tag : tags)
            if (look.tag == tag)
                return true ;
        return false ;
    }
    int             level       = 0 ;
    String          indent      = "..." ;
    public void visit(CompilationUnit n) {
        System.out.println("CompilationUnit") ;
        n.block = new BlockStatementNode(null) ;
        level++ ;
        n.block.accept(this) ;
        level-- ;
        }
    public void visit (BlockStatementNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("BlockStatmentNode");
        match('{', "expected: ") ; //J
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
        while (opt(Tag.ID, Tag.IF, Tag.WHILE, Tag.DO, Tag.BREAK, Tag.BASIC)) {
            n.stmts.add(parseStatementNode()) ;
        }
        level-- ;
        match('}', "expected: ") ; //J
        top = n.sTable ;
        enclosingBlock = n.parent ;
    }
    public void visit(DeclarationNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("DeclarationNode") ;
        n.type = new TypeNode() ;
        level++ ;
        n.type.accept(this) ;
        level-- ;
        n.id = new IdentifierNode() ;
        n.id.type = n.type.basic ;
        level++ ;
        n.id.accept(this) ;
        level-- ;

        top.put(n.id.w, n.id) ;
        IdentifierNode tmp = (IdentifierNode)top.get(n.id.w) ;
        println("&&&&&& tmp.type: " + tmp.type) ;
        println("&&&&&& tmp.w: " + tmp.w) ;
        match(';', "Declaration expected: ") ; //J
    }
    public void visit (TypeNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("TypeNode: " + look) ;
        if (look.toString().equals("int"))
            n.basic = Type.Int ;
        else if (look.toString().equals("float"))
            n.basic = Type.Float ;
        else if (look.toString().equals("bool"))
            n.basic = Type.Bool ;
        else if (look.toString().equals("char"))
            n.basic = Type.Char ;
        else {
            System.out.println("Missing or unnown type in TypeNode: " + look.toString()) ;
            System.exit(-1) ;
        }
        //custom error for this might not be needed since all basics are accounted for.
        match(Tag.BASIC, "expected int, float, bool, char, ID: ") ; //J

        if (look.tag == '[') {
            n.array = new ArrayTypeNode() ;
            level++ ;
            n.array.accept(this) ;
            level-- ;
        }
    }
    public void visit(ArrayTypeNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("ArrayTypeNode") ;
        match('[') ;
        if (look.tag == Tag.ID)
            error("array declaraion using other variables is not supported yet") ;
        if (look.tag != Tag.NUM)
            error("ArrayTypeNode expected a Num inside the []") ;
        n.size = ((Num)look).value ;
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("Array Dimentsion: " + ((Num)look).value) ;
        match(Tag.NUM) ;
        match(']') ;
        if (look.tag == '[') {
            n.type = new ArrayTypeNode() ;
            level++ ;
            n.type.accept(this) ;
            level-- ;
        }

    }
    public StatementNode parseStatementNode () {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("**** parseStatementNode") ;
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
                    error("adding declarations after statements are not supported yet") ;
                default:
                    error("Syntax error: Statement needed") ;
                    return null ;
            }   
    }
    public void visit (ParenthesesNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("ParenthesesNode") ;
        match('(') ;
        if (look.tag == '(') {
            n.expr = new ParenthesesNode() ;
            level++ ;
            n.expr.accept(this) ;
            level-- ;
        } else if (look.tag == Tag.ID) {
            System.out.println("Testing Judahs Idea");
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

            level++ ;
            n.expr = parseBinExprNode(n.expr, 0) ;
            level-- ;
        }
        match(')') ;
    }
    public void visit (IfStatementNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("IfAtatementNode");
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
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("operator: if");
        n.cond = new ParenthesesNode() ;
        level++ ;
        n.cond.accept(this) ;
        level-- ;
        if (look.tag == '{')  {
            n.stmt = new BlockStatementNode(enclosingBlock) ;
            level++ ;
            n.stmt.accept(this) ;
            level-- ;
        } else {
            n.stmt = parseStatementNode() ;
        }
        if (look.tag == Tag.ELSE) {
            match(Tag.ELSE);
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("operator: else") ;
            if (look.tag == '{') {
                n.else_stmt = new BlockStatementNode(enclosingBlock) ;
                level++ ;
                n.else_stmt.accept(this) ;
                level-- ;
            }
            else {
                n.else_stmt = parseStatementNode() ;
            }
        }
    }
    public void visit(WhileStatementNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("WhileStatementNode");
        match(Tag.WHILE) ;
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("operator: while");
        n.cond = new ParenthesesNode() ;
        level++ ;
        n.cond.accept(this) ;
        level-- ;
        if (look.tag == '{') {
            n.stmt = new BlockStatementNode(enclosingBlock) ;
            level++ ;
            n.stmt.accept(this) ;
            level-- ;
        } else {
            n.stmt = parseStatementNode() ;
        }
    }
    public void visit(DoWhileStatementNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("DoWhileStatementNode");
        match(Tag.DO);
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("operator: do");
        if (look.tag == '{') {
            n.stmt = new BlockStatementNode(enclosingBlock) ;
            level++ ;
            n.stmt.accept(this) ;
            level-- ;
        }
        else {
            n.stmt = parseStatementNode() ;
        }

        match(Tag.WHILE) ;
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("operator: while");
        n.cond = new ParenthesesNode() ;
        level++ ;
        n.cond.accept(this) ;
        level-- ;
        match(';') ;
    }
    public void visit(ArrayAccessNode n) {
    }
    public void visit(ArrayDimsNode n ) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("ArrayDimsNode") ;
        match('[') ;
        ExprNode index = null ;
        if (look.tag == '(') {
            index = new ParenthesesNode() ;
            level++ ;
            ((ParenthesesNode)index) .accept(this) ;
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
            level++ ;
            index = parseBinExprNode(index,0) ;
            level-- ;
        }
        match(']') ;
        n.size = index ;
        if (look.tag == '[') {
            n.dim = new ArrayDimsNode() ;
            level++ ;
            n.dim.accept(this) ;
            level-- ;
        }
    }
    ExprNode parseArrayAccessNode (IdentifierNode id) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("parseArrayAccessNode") ;
        ArrayDimsNode index = new ArrayDimsNode() ;
        level++ ;
        index.accept(this) ;
        level-- ;
        return new ArrayAccessNode(id, index) ;
    }
    public void visit (AssignmentNode n) { // major changes
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("AssignmentNode");
        n.left = new IdentifierNode() ;        
        level++ ;
        n.left.accept(this) ;
        level-- ;
        IdentifierNode id =  (IdentifierNode)top.get(((IdentifierNode)n.left).w) ;
        if (id == null)
            error("Declaration error: " + ((IdentifierNode)n.left).w + " was not declared or declared properly") ;
        println("In Parser, AssignmentNode's left type: "+ id.type) ;
        ((IdentifierNode)n.left).type = id.type ;
        if (look.tag == '[') {
            n.left = parseArrayAccessNode((IdentifierNode)n.left) ;
        }
        match('=') ;
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("operator: =") ;
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
        else {
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("operator: " + look) ;
            level++ ;
            n.right = (BinExprNode) parseBinExprNode( rhs_assign, 0) ;
            level-- ;
            System.out.println("**** Root Node operator: " + ((BinExprNode)n.right).op) ;
        }
        match(';');
    }
    public void visit(BreakStatementNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("BreakStatementNode: break") ;
        match(Tag.BREAK) ;
        match(';') ;
    }
    public void visit (TrueNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("TrueNode") ;
        match(Tag.TRUE) ;
    }
    public void visit (FalseNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("FalseNode") ;
        match(Tag.FALSE) ;
    }
    public void visit(BinExprNode n) {
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("BinExprNode") ;
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
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("&&&&&& operator: " + look) ;
        System.out.println("&&&&&& n.left: " + n.left) ;
        level++ ;
        BinExprNode binary = (BinExprNode) parseBinExprNode((ExprNode)n.left, 0) ;
        n.op = binary.op ;
        n.right = binary.right ;
        level -- ;
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
            case '-':
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
        println("****** n.type: " + n.type) ;
        if (look.tag != Tag.ID)
            error("Syntax error: Identifier or variable needed, instead of " + n.id) ;
        match(Tag.ID) ;
        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
    }  
}
