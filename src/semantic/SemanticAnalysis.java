package semantic;

import core.Symbol;
import core.Token;
import syntax.Node;

import java.util.*;

public class SemanticAnalysis {
    private Set<String> definedVariables;
    private Map<Integer, String> addressTable;
    private List<Node> lines;

    private List<SemanticError> errors;
    private boolean hadSemanticError;

    public SemanticAnalysis(Map<Integer, String> addressTable) {
        definedVariables = new HashSet<>();
        this.addressTable = addressTable;
        errors = new ArrayList<>();
        hadSemanticError = false;
    }

    public boolean hadSemanticError() { return hadSemanticError; }

    public void print(){
        for (Map.Entry<Integer, String> entry : addressTable.entrySet())
            System.out.println("KEY: " + entry.getKey() + "| value: " + entry.getValue());

        for (String name : definedVariables){
            System.out.println("SET: " + name);
        }
    }

    public void printErrors() {
        errors.forEach(semanticError -> System.err.println(semanticError.toString()));
    }


    public void analyze(List<Node> programs) {
        lines = programs;

        for (Node program : programs){
            try{
                String variableName = addressTable.get(program.token.getAddress());
                if (definedVariables.contains(variableName))
                    throw new SemanticError("Variável '" + variableName + "' já definida", program.token);
                definedVariables.add(variableName);
                for (Node command: program.children){
                    analyzeCommand(command);
                }
            } catch (SemanticError e) {
                hadSemanticError = true;
                errors.add(e);
            }
        }

    }

    private void analyzeCommand(Node command) throws SemanticError {
        Symbol commandType = command.token.getType();
        Token token = command.token;
        Node variable = null;

        switch (commandType) {
            case LET:
                analyseAssignmentCommand(command);
                break;
            case IF:
                analyseConditionalCommand(command);
                break;
            case GOTO:
                analyseGotoCommand(command);
                break;
            case INPUT:
                variable = command.children.get(0);
                String variableName = addressTable.get(variable.token.getAddress());
                definedVariables.add(variableName);
                break;
            case PRINT:
                variable = command.children.get(0);
                analyseVariableExists(variable, "Variável");
                break;
            case INTEGER:
                analyseVariableExists(command, "Label");
                break;
        }
    }

    private void analyseAssignmentCommand(Node command) throws SemanticError {
        Node variable = command.children.get(0);
        String variableName = addressTable.get(variable.token.getAddress());

        //Analisando expressão primeiro
        variable = command.children.get(2);
        if (variable.token.getType() != Symbol.VARIABLE) {
            analyseExpression(variable);
        } else {
            if (variable.token.getType() == Symbol.VARIABLE)
                analyseVariableExists(variable, "Variavel");
        }
        definedVariables.add(variableName);
    }

    private void analyseExpression(Node expression) throws SemanticError {
        for (Node child: expression.children){
            if (child.token.getType() == Symbol.VARIABLE)
                analyseVariableExists(child, "Variavel");
        }
    }

    private void analyseConditionalCommand(Node command) throws SemanticError{
        Node condition = command.children.get(0);
        if (condition.token.getType() == Symbol.VARIABLE)
            analyseVariableExists(condition, "Variável");

        condition = command.children.get(2);
        if (condition.token.getType() == Symbol.VARIABLE)
            analyseVariableExists(condition, "Variável");

        analyseGotoCommand(command.children.get(3));
    }

    private void analyseGotoCommand(Node command) throws SemanticError {
        Node label = command.children.get(0);
//        analyseVariableExists(label, "Label");

        String name = addressTable.get(label.token.getAddress());
        int address = label.token.getAddress();

        boolean lineExists = false;
        for (Node node : lines){
            if (node.token.getAddress() == address){
                lineExists = true;
            }
        }

        if (!lineExists){
//            System.err.println("Linha: " + label.token.getLine() + " Coluna: " + label.token.getColumn());
            throw new SemanticError("Linha '" + name + "' não existe", label.token);
        }

//        if ( name == null)
//            definedVariables.add(name);
//            System.err.println("Linha: " + label.token.getLine() + " Coluna: " + label.token.getColumn());
//            throw new SemanticError("Label '" + name + "' não definido");
    }

    private void analyseVariableExists(Node variable, String type) throws SemanticError {
        String variableName = addressTable.get(variable.token.getAddress());
        if(!definedVariables.contains(variableName)){
//            System.err.println("Linha: " + variable.token.getLine() + " Coluna: " + variable.token.getColumn());
            throw new SemanticError(type + " '" + variableName + "' não definido", variable.token);
        }

        definedVariables.add(variableName);
    }

}
