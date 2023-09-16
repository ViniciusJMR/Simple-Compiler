import java.util.ArrayList;
import java.util.List;

class SyntaxError extends Exception {
    public SyntaxError(String message) {
        super(message);
    }
}

class Node {
    String value;
    List<Node> children;

    public Node(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }
}

public class gpt1 {
    private List<String> tokens;
    private int currentIndex;
    private String currentToken;

    public gpt1(List<String> tokens) {
        this.tokens = tokens;
        this.currentIndex = 0;
        this.currentToken = null;
    }

    public Node parsePrograma() throws SyntaxError {
        List<Node> comandos = new ArrayList<>();
        while (currentIndex < tokens.size()) {
            Node comando = parseComando();
            comandos.add(comando);
        }
        return new Node("Programa") {{
            children.addAll(comandos);
        }};
    }

    public Node parseComando() throws SyntaxError {
        if (currentToken.equals("se")) {
            return parseExpressaoCondicional();
        } else {
            return parseAtribuicao();
        }
    }

    public Node parseAtribuicao() throws SyntaxError {
        Node variavel = parseVariavel();
        if (!currentToken.equals("=")) {
            throw new SyntaxError("Esperava '=' após a variável");
        }
        consumeToken();
        Node expressao = parseExpressao();
        return new Node("Atribuicao") {{
            children.add(variavel);
            children.add(expressao);
        }};
    }

    public Node parseVariavel() throws SyntaxError {
        String token = currentToken;
        if (!token.matches("[a-zA-Z][a-zA-Z0-9]*")) {
            throw new SyntaxError("Variável inválida");
        }
        consumeToken();
        return new Node("Variavel") {{
            value = token;
        }};
    }

    public Node parseExpressaoCondicional() throws SyntaxError {
        consumeToken();  // Consume "se"
        Node condicao = parseCondicao();
        if (!currentToken.equals("entao")) {
            throw new SyntaxError("Esperava 'entao' após a condição");
        }
        consumeToken();
        Node comando1 = parseComando();
        if (currentToken.equals("senao")) {
            consumeToken();
            Node comando2 = parseComando();
            if (!currentToken.equals("fim")) {
                throw new SyntaxError("Esperava 'fim' após o comando senao");
            }
            consumeToken();
            return new Node("ExpressaoCondicional") {{
                children.add(condicao);
                children.add(comando1);
                children.add(comando2);
            }};
        } else if (currentToken.equals("fim")) {
            consumeToken();
            return new Node("ExpressaoCondicional") {{
                children.add(condicao);
                children.add(comando1);
            }};
        } else {
            throw new SyntaxError("Esperava 'senao' ou 'fim' após o comando entao");
        }
    }

    public Node parseCondicao() throws SyntaxError {
        Node expressao1 = parseExpressao();
        String operador = currentToken;
        if (!operador.matches("==|!=|<|>|<=|>=")) {
            throw new SyntaxError("Operador de condicao inválido");
        }
        consumeToken();
        Node expressao2 = parseExpressao();
        return new Node("Condicao") {{
            value = operador;
            children.add(expressao1);
            children.add(expressao2);
        }};
    }

    public Node parseExpressao() throws SyntaxError {
        Node termo1 = parseTermo();
        while (currentToken.equals("+") || currentToken.equals("-")) {
            String operador = currentToken;
            consumeToken();
            Node termo2 = parseTermo();
            Node finalTermo = termo1;
            termo1 = new Node("Expressao") {{
                value = operador;
                children.add(finalTermo);
                children.add(termo2);
            }};
        }
        return termo1;
    }

    public Node parseTermo() throws SyntaxError {
        Node fator1 = parseFator();
        while (currentToken.equals("*") || currentToken.equals("/")) {
            String operador = currentToken;
            consumeToken();
            Node fator2 = parseFator();
            Node finalFator = fator1;
            fator1 = new Node("Termo") {{
                value = operador;
                children.add(finalFator);
                children.add(fator2);
            }};
        }
        return fator1;
    }

    public Node parseFator() throws SyntaxError {
        if (currentToken.matches("\\d+")) {
            Node numero = new Node("Numero") {{
                value = currentToken;
            }};
            consumeToken();
            return numero;
        } else if (currentToken.equals("(")) {
            consumeToken();
            Node expressao = parseExpressao();
            if (!currentToken.equals(")")) {
                throw new SyntaxError("Falta o ')'");
            }
            consumeToken();
            return expressao;
        } else {
            Node variavel = parseVariavel();
            return variavel;
        }
    }

    private void consumeToken() {
        currentIndex++;
        if (currentIndex < tokens.size()) {
            currentToken = tokens.get(currentIndex);
        } else {
            currentToken = null;
        }
    }

    public static void main(String[] args) {
        String input = "x = 5\n" +
                "y = 10\n" +
                "se x > y entao\n" +
                "  resultado = x - y\n" +
                "senao\n" +
                "  resultado = x + y\n" +
                "fim";
        String[] tokenArray = input.split("\\s+");
        List<String> tokenList = new ArrayList<>();
        for (String token : tokenArray) {
            tokenList.add(token);
        }
        gpt1 parser = new gpt1(tokenList);
        try {
            Node programa = parser.parsePrograma();
            System.out.println("Árvore de Análise Sintática:");
            printTree(programa, 0);
        } catch (SyntaxError e) {
            System.out.println("Erro de sintaxe: " + e.getMessage());
        }
    }

    public static void printTree(Node node, int indent) {
        if (node != null) {
            System.out.println(" ".repeat(indent) + node.value);
            for (Node child : node.children) {
                printTree(child, indent + 2);
            }
        }
    }
}
