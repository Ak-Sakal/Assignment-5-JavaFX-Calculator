
public class DoubleStack {
  private int size;
  private double[] elementsContainer;
  private int topIndex;

  public DoubleStack() {
    size = 16;
    elementsContainer = new double[size];
  }

  public void push(double element) {
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

  public double pop() {
    if (topIndex <= 0) {
      System.out.println("The stack is empty.");
      return 0.0;
    }
    double temp = elementsContainer[topIndex - 1];
    elementsContainer[topIndex - 1] = 0.0;
    --topIndex;
    return temp;
  }

  public double[] copyOf(double[] elementsContainer, int newSize, int oldSize) {
    double[] newConteiner = new double[newSize];
    for (int i = 0 ; i < oldSize ; i++) {
      newConteiner[i] = elementsContainer[i];
    }
    return newConteiner;
  }

  public boolean isEmpty() {
    return topIndex == 0;
  }

  public double peek() {
    if (topIndex <= 0) {
      System.out.println("The stack is empty");
      return 0.0;
    }
    return elementsContainer[topIndex - 1];
  }

  public int size() {
    return topIndex;
  }
}
