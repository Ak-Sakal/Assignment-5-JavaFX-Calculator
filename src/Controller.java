import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    private Model model;

    public Controller(Model model) {
        this.model = model;

    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        model.doAction(command);
    }
}
