import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Interface extends JFrame
{
private JLabel clientIDLabel;
private JLabel NIPLabel;
private JTextField clientIDInput;
private JTextField NIPInput;
private JButton logInBtn;

public Interface()
{
    super("Emprunter Livre");
    setLayout(new FlowLayout());

    clientIDLabel = new JLabel("Entrez votre ID:");
    add(clientIDLabel);

    clientIDInput = new JTextField(15);
    add(clientIDInput);

    NIPLabel= new JLabel("Entrez votre NIP: ");
    add(NIPLabel);

    NIPInput = new JTextField(5);
    add(NIPInput);

    logInBtn = new JButton("Login");
    add(logInBtn);

    logInBtn.addActionListener(event -> LogIn());
}

private void LogIn(){
    String Id = clientIDInput.getText();
    String NIP = NIPInput.getText();
}
public static void main(String[] args)
{
    Interface Interface = new Interface();
    Interface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Interface.setSize(850, 300);
    Interface.setVisible(true);
}//Fin de la méthode main
}//Fin de la classe LabelFrame