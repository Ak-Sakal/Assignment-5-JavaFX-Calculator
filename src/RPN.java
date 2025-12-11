public class RPN {

  // Check if a char is an operator
  public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
  }

  // Return the precedence of an operator
  public int precedence(char c) {
        if (c == '+' || c == '-') {
          return 1;
        }

        if (c == '*' || c == '/') {
          return 2;
        }

        return 0;
  }

  // Converts an infix expression into postfix form (Reverse Polish Notation)
  public char[] infixToPostfix(char[] infix) {

    for(int i = 0; i < infix.length; i++) {
      if(infix[i] == ',') {
        infix[i] = '.';
      }
    }

    CharStack operatorStack = new CharStack();
    CharStack outputStack = new CharStack();

    for (int i = 0; i < infix.length; i++) {
        char token = infix[i];

        if (token == ' ') {
            continue;
        }

        if (Character.isDigit(token) || token == '.') {
            outputStack.push(token);
            continue;
        }

        // Handle unary minus (0 - number)
        if (token == '-') {
            boolean isUnary = (i == 0) || (infix[i - 1] == '(') || isOperator(infix[i - 1]);

            if (isUnary) {
                outputStack.push(' ');
                outputStack.push('0');
                outputStack.push(' ');
                operatorStack.push('-');
                continue;
            }
        }

        // If it is an opening bracket, push it to the operator stack
        if (token == '(') {
            operatorStack.push(token);
            continue;
        }

        // If it is a closing bracket, pop operators until the opening bracket is found
        if (token == ')') {
            outputStack.push(' ');
            while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                outputStack.push(operatorStack.pop());
                outputStack.push(' ');
            }
            if (!operatorStack.isEmpty()) {
                operatorStack.pop();
            }
            continue;
        }

        // Pop operators with higher or equal precedence from the stack
        if (isOperator(token)) {
            outputStack.push(' ');

            while (!operatorStack.isEmpty()) {
                char top = operatorStack.peek();
                if (top == '(') {
                    break;
                }
                if (precedence(top) < precedence(token)) {
                    break;
                }
                outputStack.push(operatorStack.pop());
                outputStack.push(' ');
            }

            operatorStack.push(token);
        }
    }

    // Pop all remaining operators
    while (!operatorStack.isEmpty()) {
        outputStack.push(' ');
        outputStack.push(operatorStack.pop());
    }

    // Reverse the output stack to form correct postfix expression
    int size = outputStack.size();
    char[] postfix = new char[size];
    for (int i = size - 1; i >= 0; i--) {
        postfix[i] = outputStack.pop();
    }

    return postfix;
  }

  // Evaluates a postfix expression and return the String result
  public String evaluate(char[] postfix) {
          DoubleStack stack = new DoubleStack();
          double currentNumber = 0.0;
          double decimalPlace = 0.1;
          boolean readingNumber = false;
          boolean hasDot = false;

          for (int i = 0; i < postfix.length; i++) {
              char token = postfix[i];

              /*
              * If the token is digit, keep building the current number
              * If it is a dot, start reading fractional part
              * If it is a space, finish current number, push it to the stack
              * If it is an operator, pop two numbers and calculate
              */

              if (Character.isDigit(token)) {
                  readingNumber = true;
                  if (hasDot) {
                      currentNumber += (token - '0') * decimalPlace;
                      decimalPlace *= 0.1;
                  } else {
                      currentNumber = currentNumber * 10 + (token - '0');
                  }
              } else if (token == '.') {
                  hasDot = true;
              } else if (token == ' ') {
                  if (readingNumber) {
                      stack.push(currentNumber);
                      currentNumber = 0.0;
                      hasDot = false;
                      decimalPlace = 0.1;
                      readingNumber = false;
                  }
              } else if (isOperator(token)) {
                  double num2 = stack.pop();
                  double num1 = stack.pop();
                  double result = 0.0;

                  if (token == '+') {
                    result = num1 + num2;
                  } else if(token == '-') {
                    result = num1 - num2;
                  } else if(token == '*') {
                    result = num1 * num2;
                  } else if(token == '/') {
                    if (num2 == 0) {
                      throw new ArithmeticException("Division by zero!");
                    }
                    result = num1 / num2;
                  }

                  stack.push(result);
              }
          }

          // If the expression ended while reading a number, push it
          if (readingNumber) {
              stack.push(currentNumber);
          }

          // Get the final result
          String result;
          double finalResult = stack.pop();
          finalResult = Math.round(finalResult * 100000000.0) / 100000000.0;

          // Format the result
          if (finalResult == (long) finalResult) {
              result = Long.toString((long) finalResult);
          } else {
              result = Double.toString(finalResult);
          }

          // Replace dot with a comma
          return result.replace('.', ',');
      }
}
