package syntax;

import core.Symbol;
import core.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SyntaxAnalysis {

    private Map<String, Integer> symbolTable;
    private List<Token> tokens;
    private List<Node> nodes;
    private Token currentToken;
    private int currentIndex;

    public SyntaxAnalysis(Map<String, Integer> symbolTable, List<Token> tokens) {
        this.symbolTable = symbolTable;
        this.tokens = tokens;
        this.currentToken = null;
        this.currentIndex = 0;
        this.nodes = new ArrayList<>();
    }

    private Token getNextToken() {
        if (currentIndex < tokens.size()){
            return tokens.get(currentIndex);
        }
        return null;
    }

    private void consumeToken() {
        currentIndex++;
    }

    public void print(){
        _print(nodes.get(0), 0);
    }

    private void _print(Node node, int indent){
        if(node != null){
            Symbol type = node.token.getType();
            int address = node.token.getAddress();
            String value = node.token.getType().name();
            if (type == Symbol.VARIABLE || type == Symbol.INTEGER)
                for (Map.Entry<String, Integer> entry : symbolTable.entrySet())
                    if (entry.getValue() == address)
                        value = entry.getKey();

            System.out.println(" ".repeat(indent) + value);
            for (Node child: node.children){
                _print(child, indent + 2);
            }
        }
    }

    public Node parse() throws SyntaxError {
        try{
           return parseProgram();
        } catch (SyntaxError syntaxError){
            int errorLine = currentToken.getLine();
            int errorColumn = currentToken.getColumn();
            String newMsg = syntaxError.getMessage() +
                    "\nNa linha: " + errorLine +
                    "\nNa coluna: " + errorColumn +
                    "\nToken: " + currentToken.toString();
            throw new SyntaxError(newMsg);
        }
    }


    // Parse Program

    public Node parseProgram() throws SyntaxError {
        Node program = new Node(new Token(Symbol.LF, -1,-1));
        while (currentIndex < tokens.size()){
            Node command = parseLabel();
            if(command != null)
                program.children.add(command);

            if (currentToken.getType() == Symbol.ETX){
                currentToken = getNextToken();
                if(currentToken == null)
                    throw new SyntaxError("Fim do programa esperado");
                consumeToken();
            } else {
                currentToken = getNextToken();
                if(currentToken.getType() != Symbol.LF)
                    throw new SyntaxError("Fim de linha esperado");
                consumeToken();
            }
            nodes.add(program);
        }

        return program;
    }



    // Parse Label
    public Node parseLabel() throws SyntaxError{
        currentToken = getNextToken();
        if (currentToken.getType() == Symbol.INTEGER){
            Node label = new Node(currentToken);
            consumeToken();
            Node command = parseCommand();
            label.children.add(command);
            return label;
        } else {
            throw new SyntaxError("Label esperado no início da linha");
        }
    }

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
                if(currentToken.getType() != Symbol.VARIABLE)
                    throw new SyntaxError("Variável esperada.");

                command.children.add(new Node(currentToken));
                consumeToken();


                return command;
            case REM:
                consumeToken();
                return null;
            case END:
                command = new Node(currentToken);
                consumeToken();
                currentToken = getNextToken();

                if (currentToken.getType() != Symbol.ETX)
                    throw new SyntaxError("Fim do arquivo esperado");

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
        if (currentToken.getType() == Symbol.INTEGER
           || currentToken.getType() == Symbol.SUBTRACT){
            Node item = parseInteger();
            return item;
        }
        if (currentToken.getType() == Symbol.VARIABLE){
            consumeToken();
            return new Node(currentToken);
        }
        else
            throw new SyntaxError("Item inválido.");
    }
    // Parse Integer
    private Node parseInteger() throws SyntaxError {
        currentToken = getNextToken();
        if (currentToken.getType() == Symbol.SUBTRACT) {
            Node subtract = new Node(currentToken);
            consumeToken();
            currentToken = getNextToken();
            if (currentToken.getType() != Symbol.INTEGER) {
                throw new SyntaxError("Label esperado após operador");
            }
            Node integer = new Node(currentToken);
            consumeToken();
            subtract.children.add(integer);
            return subtract;
        } else if (currentToken.getType() == Symbol.INTEGER) {
            Node integer = new Node(currentToken);
            consumeToken();
            return integer;
        } else {
            throw new SyntaxError("Inteiro esperado");
        }

    }

}
