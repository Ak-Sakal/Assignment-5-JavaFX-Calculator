
public class CharStack {
  private int size;
  private char[] elementsContainer;
  private int topIndex;

  public CharStack() {
    size = 16;
    elementsContainer = new char[size];
  }

  public void push(char element) {
    if (topIndex < size) {
      elementsContainer[topIndex++] = element;
      return;
    }
    int oldSize = size;
    size *= 2;
    int newSize = size;
    elementsContainer = copyOf(elementsContainer, newSize, oldSize);
    elementsContainer[topIndex++] = element;
  }

  public char pop() {
    if (topIndex <= 0) {
      System.out.println("The stack is empty..");
      return '\0';
    }
    char temp = elementsContainer[topIndex - 1];
    elementsContainer[topIndex - 1] = '\0';
    --topIndex;
    return temp;
  }

  public char[] copyOf(char[] elementsContainer, int newSize, int oldSize) {
    char[] newConteiner = new char[newSize];
    for (int i = 0 ; i < oldSize ; i++) {
      newConteiner[i] = elementsContainer[i];
    }
    return newConteiner;
  }

  public boolean isEmpty() {
    return topIndex == 0;
  }

  public char peek() {
    if (topIndex <= 0) {
      System.out.println("The stack is empty.");
      return '\0';
    }
    return elementsContainer[topIndex - 1];
  }

  public int size() {
    return topIndex;
  }
}
