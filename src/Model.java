public class Model {
    private View view;

    private RPN rpn;
    private static final int START = 0;
    private static final int AFTER_NUMBER = 1;
    private static final int AFTER_OPERATOR = 2;
    private static final int AFTER_DOT = 3;
    private static final int AFTER_OPEN_PAREN = 4;
    private StringBuilder container;
    private int state = START;
    private int paren;
    private boolean dot;
    private String lastOperator = "";
    private String lastNumber = "";
    private boolean justCalculated;


    public Model(View view) {
      this.view = view;
      rpn = new RPN();
      container = new StringBuilder();
    }

    public void clear() {
      container.setLength(0);
      state = START;
      paren = 0;
      dot = false;
      lastOperator = "";
      lastNumber = "";
      justCalculated = false;
    }

    public void backspace() {
      if (container.length() == 0) {
        return;
      }

      int n = container.length();
      int last = n - 1;

      if (container.charAt(last) == ' ') {
        int end = last - 1;
        container.delete(end, n);
      } else {
          container.deleteCharAt(last);
        }
      rebuildText(container.toString());
    }

    public boolean canEquals() {
      return state == AFTER_NUMBER && paren == 0;
    }


    private void calculate() {
      try {
        if (container.length() == 0) {
          view.update("");
          return ;
        }
        String expression = container.toString();
        char[] postfix = rpn.infixToPostfix(expression.toCharArray());
        String result = rpn.evaluate(postfix);

        saveLastNumber();

        justCalculated = true;
        view.update(result);

        container.setLength(0);
        container.append(result);
        rebuildText(container.toString());
      } catch (ArithmeticException e) {
          view.update("division by zero");
          clear();
        }
    }

    private void saveLastNumber() {
      String expr = container.toString().trim();
      int lastSpace = expr.lastIndexOf(' ');

      if (lastSpace != -1 && lastSpace < expr.length() - 1) {
        lastNumber = expr.substring(lastSpace + 1).trim();
        lastOperator = String.valueOf(expr.charAt(lastSpace - 1));
      }
    }

    private void repeatOperation() {
      try {
        double currentResult = Double.parseDouble(container.toString().replace(',', '.'));
        double operand = Double.parseDouble(lastNumber.replace(',', '.'));
        double newResult = 0;

        if (lastOperator.equals("+")) {
          newResult = currentResult + operand;
        } else if (lastOperator.equals("-")) {
          newResult = currentResult - operand;
        } else if (lastOperator.equals("*")) {
          newResult = currentResult * operand;
        } else if (lastOperator.equals("/")) {
          if (operand == 0) {
            throw new ArithmeticException("Division by zero!");
          }
          newResult = currentResult / operand;
        }

        String result = Double.toString(newResult);

        if (result.contains(".")) {
          while (result.endsWith("0")) {
            result = result.substring(0, result.length() - 1);
          }
          if (result.endsWith(".")) {
            result = result.substring(0, result.length() - 1);
          }
        }

        result = result.replace('.', ',');

        container.setLength(0);
        container.append(result);
        view.update(container.toString());

      } catch (Exception e) {
        view.update("Error");
        justCalculated = false;
      }
    }


    private char getChar(String command) {
      if (command.equals("Zero")) {
        return '0';
      } else if (command.equals("One")) {
        return '1';
      } else if (command.equals("Two")) {
        return '2';
      } else if (command.equals("Three")) {
        return '3';
      } else if (command.equals("Four")) {
        return '4';
      } else if (command.equals("Five")) {
        return '5';
      } else if (command.equals("Six")) {
        return '6';
      } else if (command.equals("Seven")) {
        return '7';
      } else if (command.equals("Eight")) {
        return '8';
      } else if (command.equals("Nine")) {
        return '9';
      }
      return '\0';
    }

    private boolean zeroNum() {
      int i;
      for (i = container.length() - 1; i >= 0; i--) {
        char c = container.charAt(i);
        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(') {
          break;
        }
      }

      int start = i + 1;
      for (; start < container.length(); start++) {
        if (container.charAt(start) != ' ') {
          break;
        }
      }

      int len = container.length() - start;
      if (len == 1 && container.charAt(start) == '0' && !dot) {
        return true;
      }
      return false;
    }

    private boolean appendNum(char c) {
      if (justCalculated) {
        clear();
      }
      autoMultiply();
      if (state == AFTER_NUMBER && c >= '0' && c <= '9') {
        if (zeroNum()) {
          return false;
        }
      }
      container.append(c);
      state = AFTER_NUMBER;
      return true;
    }

    private boolean appendDot() {
      autoMultiply();
      if (state == START || state == AFTER_OPERATOR || state == AFTER_OPEN_PAREN) {
        container.append('0').append(',');
        state = AFTER_DOT;
        dot = true;
        return true;
      }
      else if (state == AFTER_NUMBER && !dot) {
        container.append(',');
        state = AFTER_DOT;
        dot = true;
        return true;
      }
      return false;
    }

    private boolean appendOperator(char op) {
      if (state == AFTER_DOT){
        container.append('0');
        state = AFTER_NUMBER;
      }
      if (op == '-' &&  state == START) {
        container.append('-');
        state = AFTER_OPERATOR;
        dot = false;
        return true;
      } else if (op == '-' && state == AFTER_OPEN_PAREN){
          container.append('-');
          state = AFTER_OPERATOR;
          dot = false;
          return true;
      } else if (state == AFTER_NUMBER) {
          container.append(' ').append(op).append(' ');
          state = AFTER_OPERATOR;
          dot = false;
          justCalculated = false;
          return true;
      }
      return false;
    }

    private boolean openBracket() {
      char с = lastChar();

      if (с == ')' || с >= '0' && с <= '9') {
        appendSpaces();
        state = AFTER_OPERATOR;
      }
      if (state == START || state == AFTER_OPERATOR || state == AFTER_OPEN_PAREN) {
        container.append('(');
        paren++;
        state = AFTER_OPEN_PAREN;
        dot = false;
        return true;
      }
      return false;
    }

    private boolean closeBracket() {
      if (state == AFTER_DOT) {
        container.append('0');
        state = AFTER_NUMBER;
      } else if (paren > 0 && state == AFTER_NUMBER) {
          container.append(')');
          paren--;
          dot = false;
          return true;
      }
      return false;
    }

    private char lastChar() {
      for (int i = container.length() - 1; i >= 0; i--) {
        char c = container.charAt(i);
        if (c != ' ') {
          return c;
        }
      }
      return '\0';
    }

    private void appendSpaces() {
      container.append(' ').append('*').append(' ');
    }

    private void autoMultiply() {
      char c = lastChar();
      if (c == ')') {
          appendSpaces();
          state = AFTER_OPERATOR;
      }
    }

    public void rebuildText(String input) {
      state = START;
      paren = 0;
      dot = false;

      for (int i = 0; i < input.length(); i++) {
        char c = input.charAt(i);

        if (c >= '0' && c <= '9') {
            state = AFTER_NUMBER;
        } else if (c == ',') {
            state = AFTER_DOT;
            dot = true;
        } else if (c == '+' || c == '*' || c == '/' || c == '-') {
            state = AFTER_OPERATOR;
            dot = false;
        } else if (c == '(') {
            paren++;
            state = AFTER_OPEN_PAREN;
            dot = false;
        } else if (c == ')') {
            if (paren > 0) paren--;
            state = AFTER_NUMBER;
            dot = false;
        }
      }
    }

    public void sendToView() {
      view.update(container.toString());
    }

    public void doAction(String command) {
      char c = getChar(command);
      boolean result;

      if (command.equals("Clear")) {
        clear();
        result = true;
      } else if (command.equals("Back")) {
          backspace();
          result = true;
      } else if (command.equals("Equals")) {
          if (justCalculated && !lastOperator.isEmpty() && !lastNumber.isEmpty()) {
            repeatOperation();
          } else if (canEquals()) {
              calculate();
            }
          result = canEquals();
      } else if (command.equals("Comma")) {
          result = appendDot();
      } else if (command.equals("Plus")) {
          result = appendOperator('+');
      } else if (command.equals("Multiply")) {
          result = appendOperator('*');
      } else if (command.equals("Divide")) {
          result = appendOperator('/');
      } else if (command.equals("Minus")) {
          result = appendOperator('-');
      } else if (command.equals("OpenBracket")) {
          result = openBracket();
      } else if (command.equals("CloseBracket")) {
          result = closeBracket();
      } else {
          result = appendNum(c);
      }

      if (result) {
        sendToView();
      }
    }
}
