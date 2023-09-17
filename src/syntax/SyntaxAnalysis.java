package syntax;

import core.Symbol;
import core.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SyntaxAnalysis {

    private Map<String, Integer> symbolTable;
    private List<Token> tokens;
    private Iterator<Token> iterator;
    private Token currentToken;
    private int currentIndex;

    public SyntaxAnalysis(Map<String, Integer> symbolTable, List<Token> tokens) {
        this.symbolTable = symbolTable;
        this.tokens = tokens;
        this.iterator = tokens.iterator();
        this.currentToken = null;
        this.currentIndex = 0;
    }

    private Token getNextToken() {
        if (iterator.hasNext()){
            return tokens.get(currentIndex);
        }
        return null;
    }

    private void consumeToken() {
        iterator.next();
    }



    // Parse Program

    public Node parseProgram() throws SyntaxError {
        while (iterator.hasNext()){
//            Node command = parseLabel();
        }
        return null;
    }



    // Parse Label

    // Parse Command
    private Node parseCommand() throws SyntaxError {
        currentToken = getNextToken();

        Symbol type = currentToken.getType();
        Node command = null;

        switch (type){
            case INPUT:
            case PRINT:
                command = new Node(currentToken);
                consumeToken();
                currentToken = getNextToken();
                if(currentToken.getType() == Symbol.VARIABLE){
                    command.children.add(new Node(currentToken));
                    consumeToken();
                } else {
                    throw new SyntaxError("Variável esperada.");
                }
                return command;
            case REM:
            case END:
                command = new Node(currentToken);
                consumeToken();
                return command;

            case IF:
                command = new Node(currentToken);
                consumeToken();
                currentToken = getNextToken();
                // Parsing Conditional
                if (currentToken.getType() == Symbol.VARIABLE | currentToken.getType() == Symbol.INTEGER){
                    command.children.add(new Node(currentToken));
                    consumeToken();
                    currentToken = getNextToken();
                    type = currentToken.getType();
                    if (type == Symbol.EQ || type == Symbol.NE || type == Symbol.GT ||
                        type == Symbol.LT || type == Symbol.LE || type == Symbol.GE) {
                        command.children.add(new Node(currentToken));
                        consumeToken();
                        currentToken = getNextToken();
                        if (currentToken.getType() == Symbol.VARIABLE || currentToken.getType() == Symbol.INTEGER){
                            command.children.add(new Node(currentToken));
                            consumeToken();
                            currentToken = getNextToken();
                            if (currentToken.getType() == Symbol.GOTO){
                                command.children.add(new Node(currentToken));
                                consumeToken();
                                currentToken = getNextToken();
                                if (currentToken.getType() == Symbol.INTEGER) {
                                    command.children.add(new Node(currentToken));
                                    consumeToken();
                                    return command;
                                } else {
                                    throw new SyntaxError("Label esperado após goto");
                                }
                            } else {
                                throw new SyntaxError("goto esperado após a condição");
                            }
                        } else {
                            throw new SyntaxError("Operando esperado após o operador relacional");
                        }
                    } else {
                        throw new SyntaxError("Operador relacional esperado após a variável");
                    }
                } else {
                    throw new SyntaxError("Item esperado após if");
                }
            case LET:
                command = new Node(currentToken);
                consumeToken();
                currentToken = getNextToken();
                if (currentToken.getType() != Symbol.VARIABLE)
                    throw new SyntaxError("Variável esperada após let");

                command.children.add(new Node(currentToken));
                consumeToken();
                currentToken = getNextToken();
                if (currentToken.getType() != Symbol.ASSIGNMENT)
                    throw new SyntaxError("Esperado '=' após a variável em let");

                command.children.add(new Node(currentToken));
                consumeToken();
                Node expression = parseExpression();
                command.children.add(expression);
                return command;

            case GOTO:
                command = new Node(currentToken);
                consumeToken();
                currentToken = getNextToken();
                if (currentToken.getType() != Symbol.INTEGER)
                    throw new SyntaxError("Label esperado após goto");

                command.children.add(new Node(currentToken));
                consumeToken();
                return command;
            default:
                throw new SyntaxError("Comando inválido");
        }
    }

    // Parse Expression
    private Node parseExpression() throws SyntaxError {
        Node itemLeft = parseItem();
        currentToken = getNextToken();

        Symbol type = currentToken.getType();

        if (type == Symbol.ADD ||
                type == Symbol.SUBTRACT ||
                type == Symbol.MULTIPLY ||
                type == Symbol.DIVIDE
        ){
            Node operator = new Node(currentToken);
            operator.children.add(itemLeft);
            consumeToken();
            Node itemRight = parseItem();
            operator.children.add(itemRight);
            return operator;
        } else {
            return itemLeft;
        }
    }

    // Parse Conditional
    // Parse Relational
    // Parse Item
    private Node parseItem() throws SyntaxError{
        currentToken = getNextToken();
        if (currentToken.getType() == Symbol.VARIABLE
            || currentToken.getType() == Symbol.INTEGER){
            consumeToken();
            return new Node(currentToken);
        }
        else
            throw new SyntaxError("Item inválido.");
    }
    // Parse Integer




}
