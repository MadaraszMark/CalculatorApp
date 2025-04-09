package application.controller;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalculatorController {

    @FXML
    private Label lblMain;

    private StringBuilder expression = new StringBuilder();
    private boolean startNewNumber = false;

    @FXML
    private void initialize() {
        lblMain.setText("");
    }

    @FXML
    private void handleNumberPress(Event event) {
        Button btn = (Button) event.getSource();
        String digit = btn.getText();

        if (startNewNumber) {
            expression.setLength(0);
            startNewNumber = false;
        }

        expression.append(digit);
        lblMain.setText(expression.toString());
    }

    @FXML
    private void onSymbolPress(Event event) {
        Button btn = (Button) event.getSource();
        String symbol = btn.getText();

        switch (symbol) {
            case "=":
                evaluateExpression();
                break;

            case "Clear":
                expression.setLength(0);
                lblMain.setText("");
                break;

            case "Backspace":
                if (expression.length() > 0) {
                    expression.setLength(expression.length() - 1);
                    lblMain.setText(expression.toString());
                }
                break;

            case ",":
                expression.append(",");
                lblMain.setText(expression.toString());
                break;

            default: // +, -, X, /
                expression.append(convertOperator(symbol));
                lblMain.setText(expression.toString());
                break;
        }
    }

    private String convertOperator(String symbol) {
        switch (symbol) {
            case "X":
                return "*";
            case "/":
                return "/";
            case "+":
                return "+";
            case "-":
                return "-";
            default:
                return "";
        }
    }

    private void evaluateExpression() {
        try {
            String exprToEval = expression.toString().replace(",", ".");
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            Object result = engine.eval(exprToEval);

            String resultStr = formatResult(result.toString());
            lblMain.setText(resultStr);
            expression.setLength(0);
            expression.append(resultStr.replace(",", "."));
        } catch (Exception e) {
            lblMain.setText("Hiba!");
            expression.setLength(0);
        }
        startNewNumber = true;
    }

    private String formatResult(String result) {
        if (result.endsWith(".0")) {
            return result.replace(".0", "");
        } else {
            return result.replace(".", ",");
        }
    }
}



