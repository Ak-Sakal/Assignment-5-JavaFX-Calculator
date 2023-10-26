public class Calculator {
    private double operand1;
    private double operand2;
    private char operator;
    private double result;

    public void setOperand1(double operand) {
        this.operand1 = operand;
    }

    public void setOperand2(double operand) {
        this.operand2 = operand;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }

    public void calculate() {
        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 == 0) {
                    // Handle division by zero
                    result = Double.NaN;
                } else {
                    result = operand1 / operand2;
                }
                break;
        }
    }

    public double getResult() {
        return result;
    }
}
equalsButton.setOnAction(event -> {
    double operand2 = Double.parseDouble(inputField.getText());
    calculator.setOperand2(operand2);
    calculator.calculate();
    double result = calculator.getResult();

    if (Double.isNaN(result)) {
        resultField.setText("Error: Division by Zero");
    } else {
        resultField.setText(String.valueOf(result));
    }
});
