import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

public class View {
private JTextField textField;
private Model model;
private Controller controller;

public View() {
  model = new Model(this);
  controller = new Controller(model);
  createUI();
}

private void createUI() {
  JFrame frame = new JFrame("Calculator");
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setSize(500, 660);
  frame.setLocationRelativeTo(null);
  frame.setLayout(new BorderLayout());

  JPanel topPanel = new JPanel(new BorderLayout());
  topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

  textField = new JTextField();
  textField.setFont(new Font("Segoe UI", Font.BOLD, 38));
  textField.setForeground(new Color(255, 140, 0));
  textField.setHorizontalAlignment(JTextField.RIGHT);
  textField.setEditable(false);
  textField.setBackground(new Color(250, 250, 250));
  textField.setCaretColor(textField.getForeground());
  textField.setOpaque(true);
  textField.setMargin(new Insets(10, 15, 10, 15));
  topPanel.add(textField, BorderLayout.CENTER);

  JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));
  buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
  buttonPanel.setBackground(new Color(220, 220, 220));

  buttonPanel.add(createButton("C", "Clear", new Color(240, 240, 240), Color.RED));
  buttonPanel.add(createButton("←", "Back", new Color(240, 240, 240), Color.RED));
  buttonPanel.add(createButton("/", "Divide", Color.WHITE, new Color(255, 159, 10)));
  buttonPanel.add(createButton("*", "Multiply", Color.WHITE, new Color(255, 159, 10)));

  buttonPanel.add(createButton("7", "Seven"));
  buttonPanel.add(createButton("8", "Eight"));
  buttonPanel.add(createButton("9", "Nine"));
  buttonPanel.add(createButton("-", "Minus", Color.WHITE, new Color(255, 159, 10)));

  buttonPanel.add(createButton("4", "Four"));
  buttonPanel.add(createButton("5", "Five"));
  buttonPanel.add(createButton("6", "Six"));
  buttonPanel.add(createButton("+", "Plus", Color.WHITE, new Color(255, 159, 10)));

  buttonPanel.add(createButton("1", "One"));
  buttonPanel.add(createButton("2", "Two"));
  buttonPanel.add(createButton("3", "Three"));
  buttonPanel.add(createButton("=", "Equals", Color.WHITE, new Color(255, 159, 10)));

  buttonPanel.add(createButton("0", "Zero"));
  buttonPanel.add(createButton(",", "Comma"));
  buttonPanel.add(createButton("(", "OpenBracket"));
  buttonPanel.add(createButton(")", "CloseBracket"));

  frame.add(topPanel, BorderLayout.NORTH);
  frame.add(buttonPanel, BorderLayout.CENTER);
  frame.setVisible(true);
}

private JButton createButton(String text, String actionCommand, Color fg, Color bg) {
  JButton button = new JButton(text);
  button.setFont(new Font("Segoe UI", Font.BOLD, 26));
  button.setFocusPainted(false);
  button.setActionCommand(actionCommand);
  button.setBorderPainted(false);
  button.setContentAreaFilled(true);
  button.setOpaque(true);
  button.setBackground(bg);
  button.addActionListener(controller);
  button.setForeground(fg);
  return button;
}

private JButton createButton(String text, String action) {
  return createButton(text, action, Color.BLACK, Color.WHITE);
}

public void update(String text) {
  textField.setText(text);
}
}
