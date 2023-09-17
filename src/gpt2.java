import java.util.ArrayList;
import java.util.List;


class Node1 {
    String value;
    List<Node1> children;

    public Node1(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }
}

public class gpt2 {
    private List<String> tokens;
    private int currentIndex;
    private String currentToken;

    public gpt2(List<String> tokens) {
        this.tokens = tokens;
        this.currentIndex = 0;
        this.currentToken = null;
    }

    public Node1 parseProgram() throws SyntaxError {
        Node1 program = new Node1("Program");
        while (currentIndex < tokens.size()) {
            Node1 command = parseLabel();
            if (command != null) {
                program.children.add(command);
            }
        }
        return program;
    }
    public Node1 parseLabel() throws  SyntaxError{
        currentToken = getNextToken();
        if (currentToken.matches("[0-9]+")){
            Node1 label = new Node1(currentToken);
            consumeToken();
            return parseCommand();
        }
        else {
            throw new SyntaxError("Label esperado no inicio da linha");
        }
    }

    public Node1 parseCommand() throws SyntaxError {
        currentToken = getNextToken();
        if (currentToken.equals("input") || currentToken.equals("print")) {
            Node1 command = new Node1(currentToken);
            consumeToken();
            currentToken = getNextToken();
            if (currentToken.matches("[a-z]")) {
                command.children.add(new Node1(currentToken));
                consumeToken();
            } else {
                throw new SyntaxError("Variável esperada após input ou print");
            }
            return command;
        } else if (currentToken.equals("rem") || currentToken.equals("end")) {
            Node1 command = new Node1(currentToken);
            consumeToken();
            return command;
        } else if (currentToken.equals("if")) {
            Node1 command = new Node1("if");
            consumeToken();
            currentToken = getNextToken();
            if (currentToken.matches("[a-z]")) {
                command.children.add(new Node1(currentToken));
                consumeToken();
                currentToken = getNextToken();
                if (currentToken.equals("==") || currentToken.equals("!=") || currentToken.equals(">") ||
                        currentToken.equals("<") || currentToken.equals(">=") || currentToken.equals("<=")) {
                    command.children.add(new Node1(currentToken));
                    consumeToken();
                    currentToken = getNextToken();
                    if (currentToken.matches("[a-z0-9]+")) {
                        command.children.add(new Node1(currentToken));
                        consumeToken();
                        currentToken = getNextToken();
                        if (currentToken.equals("goto")) {
                            command.children.add(new Node1("goto"));
                            consumeToken();
                            currentToken = getNextToken();
                            if (currentToken.matches("[0-9]*")) {
                                command.children.add(new Node1(currentToken));
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
                throw new SyntaxError("Variável esperada após if");
            }
        } else if (currentToken.equals("let")) {
            Node1 command = new Node1("let");
            consumeToken();
            currentToken = getNextToken();
            if (currentToken.matches("[a-z]")) {
                command.children.add(new Node1(currentToken));
                consumeToken();
                currentToken = getNextToken();
                if (currentToken.equals("=")) {
                    command.children.add(new Node1("="));
                    consumeToken();
                    Node1 expression = parseExpression();
                    command.children.add(expression);
                    return command;
                } else {
                    throw new SyntaxError("Esperava '=' após a variável em let");
                }
            } else {
                throw new SyntaxError("Variável esperada após let");
            }
        } else if (currentToken.equals("goto")) {
            Node1 command = new Node1("goto");
            consumeToken();
            currentToken = getNextToken();
            if (currentToken.matches("[0-9]")) {
                command.children.add(new Node1(currentToken));
                consumeToken();
                return command;
            } else {
                throw new SyntaxError("Label esperado após goto");
            }
        } else {
            throw new SyntaxError("Comando inválido");
        }
    }

    public Node1 parseExpression() throws SyntaxError {
        Node1 item1 = parseItem();
        currentToken = getNextToken();
        if (currentToken.equals("+") || currentToken.equals("-") || currentToken.equals("*") || currentToken.equals("/")) {
            Node1 operator = new Node1(currentToken);
            operator.children.add(item1);
            consumeToken();
            Node1 item2 = parseItem();
            operator.children.add(item2);
            return operator;
        } else {
            return item1;
        }
    }

    public Node1 parseItem() throws SyntaxError {
        currentToken = getNextToken();
        if (currentToken.matches("[a-z]")) {
            consumeToken();
            return new Node1(currentToken);
        } else if (currentToken.matches("-|[0-9]")) {
            consumeToken();
            return new Node1(currentToken);
        } else {
            throw new SyntaxError("Item inválido");
        }
    }

    private String getNextToken() {
        if (currentIndex < tokens.size()) {
            return tokens.get(currentIndex);
        } else {
            return null;
        }
    }

    private void consumeToken() {
        currentIndex++;
    }

    public static void main(String[] args) {
        String input =
                "10 input i\n" +
                "20 let b = 5\n" +
                "30 print b\n" +
                "40 if a == 10 goto 1\n" +
                "50 let c = a + b\n" +
                "60 end";
        String[] tokenArray = input.split("\\s+");
        List<String> tokenList = new ArrayList<>();
        for (String token : tokenArray) {
            tokenList.add(token);
        }
        gpt2 parser = new gpt2(tokenList);
        try {
            Node1 programa = parser.parseProgram();
            System.out.println("Árvore de Análise Sintática:");
            printTree(programa, 0);
        } catch (SyntaxError e) {
            System.out.println("Erro de sintaxe: " + e.getMessage());
        }
    }

    public static void printTree(Node1 node, int indent) {
        if (node != null) {
            System.out.println(" ".repeat(indent) + node.value);
            for (Node1 child : node.children) {
                printTree(child, indent + 2);
            }
        }
    }
}