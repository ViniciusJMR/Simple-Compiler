package syntax;

import core.Token;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public Token token;
    public List<Node> children;

    public Node(Token token) {
        this.token = token;
        children = new ArrayList<>();
    }
}
