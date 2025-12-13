import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Interface extends JFrame
{
private JLabel clientIDLabel;
private JLabel NIPLabel;
private JTextField clientIDInput;
private JTextField NIPInput;
private JButton logInBtn;
private JTextField rfidInput;
private JButton scanBtn;
private JButton finishBtn;
private JButton returnBtn;
private JButton confirmBtn;
private JButton cancelBtn;
private JButton catalogueBtn;
private JButton myLoansBtn;
private JTextArea displayArea;
private Borne borne;

public Interface()
{
    super("Emprunter Livre");
    setLayout(new FlowLayout());

    borne = new Borne();

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

    rfidInput = new JTextField(10);
    add(rfidInput);
    rfidInput.setVisible(false);

    scanBtn = new JButton("Scanner RFID");
    add(scanBtn);
    scanBtn.setVisible(false);

    confirmBtn = new JButton("Confirmer Emprunt");
    add(confirmBtn);
    confirmBtn.setVisible(false);

    returnBtn = new JButton("Retourner Livre");
    add(returnBtn);
    returnBtn.setVisible(false);

    myLoansBtn = new JButton("Voir Mes Emprunts");
    add(myLoansBtn);
    myLoansBtn.setVisible(false);

    catalogueBtn = new JButton("Voir Catalogue");
    add(catalogueBtn);
    catalogueBtn.setVisible(false);

 

    finishBtn = new JButton("Terminer Saisie");
    add(finishBtn);
    finishBtn.setVisible(false);

    cancelBtn = new JButton("Annuler");
    add(cancelBtn);
    cancelBtn.setVisible(false);

    displayArea = new JTextArea(20, 80);
    JScrollPane scrollPane = new JScrollPane(displayArea);
    add(scrollPane);
    scrollPane.setVisible(false);

    logInBtn.addActionListener(event -> LogIn());
    scanBtn.addActionListener(event -> Scan());
    finishBtn.addActionListener(event -> Finish());
    confirmBtn.addActionListener(event -> Confirm());
    returnBtn.addActionListener(event -> ReturnBook());
    catalogueBtn.addActionListener(event -> ViewCatalogue());
    myLoansBtn.addActionListener(event -> ViewMesEmprunts());
    cancelBtn.addActionListener(event -> Cancel());
}

private void LogIn(){
    String Id = clientIDInput.getText();
    String NIP = NIPInput.getText();
    try {
        int nipInt = Integer.parseInt(NIP);
        borne.validerCompte(Id);
        borne.validerNIP(nipInt);
        // If successful, show scan interface
        clientIDLabel.setVisible(false);
        clientIDInput.setVisible(false);
        NIPLabel.setVisible(false);
        NIPInput.setVisible(false);
        logInBtn.setVisible(false);
        rfidInput.setVisible(true);
        scanBtn.setVisible(true);
        finishBtn.setVisible(true);
        returnBtn.setVisible(true);
        catalogueBtn.setVisible(true);
        myLoansBtn.setVisible(true);
        cancelBtn.setVisible(true);
        displayArea.setVisible(true);
        displayArea.setText("Authentification réussie. Scannez les RFID pour emprunter ou retourner des livres.\n");
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "NIP doit être un nombre.");
    }
}

private void Scan() {
    String rfidStr = rfidInput.getText();
    try {
        int rfid = Integer.parseInt(rfidStr);
        String result = borne.saisirExemplaire(rfid);
        if (result.startsWith("Erreur") || result.equals("Exemplaire non trouvé.") || result.equals("Le livre est déjà emprunté.")) {
            JOptionPane.showMessageDialog(this, result, "Erreur", JOptionPane.ERROR_MESSAGE);
        } else {
            rfidInput.setText("");
            updateDisplay();
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "RFID doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

private void Finish() {
    borne.terminerSaisie();
    updateDisplay();
    scanBtn.setVisible(false);
    finishBtn.setVisible(false);
    confirmBtn.setVisible(true);
}

private void Confirm() {
    String[] options = {"Courriel", "Imprimé"};
    int choice = JOptionPane.showOptionDialog(this, "Choisissez le type de reçu:", "Confirmation",
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    String type = choice == 0 ? "courriel" : "imprimé";
    borne.confirmerEmprunt(type);
    updateDisplay();
    resetInterface();
}

private void ReturnBook() {
    String rfidStr = rfidInput.getText();
    try {
        int rfid = Integer.parseInt(rfidStr);
        boolean success = borne.retournerLivre(rfid);
        if (success) {
            JOptionPane.showMessageDialog(this, "Livre retourné avec succès.");
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors du retour du livre.");
        }
        rfidInput.setText("");
        updateDisplay();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "RFID doit être un nombre.");
    }
}

private void ViewCatalogue() {
    // Create table model
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("RFID");
    model.addColumn("Titre");
    model.addColumn("Auteur");
    model.addColumn("Année");
    model.addColumn("Pages");
    model.addColumn("Disponible");

    // Populate table
    for (Livre l : borne.getCatalogue().values()) {
        String dispo = l.getEstEmprunte() ? "Non" : "Oui";
        model.addRow(new Object[]{
            l.getRFID(),
            l.getTitre(),
            l.getAuteur(),
            l.getDateParu(),
            l.getNbPage(),
            dispo
        });
    }

    // Create table
    JTable table = new JTable(model);
    // table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    // table.getColumnModel().getColumn(0).setPreferredWidth(60);
    // table.getColumnModel().getColumn(1).setPreferredWidth(200);
    // table.getColumnModel().getColumn(2).setPreferredWidth(150);
    // table.getColumnModel().getColumn(3).setPreferredWidth(60);
    // table.getColumnModel().getColumn(4).setPreferredWidth(60);
    // table.getColumnModel().getColumn(5).setPreferredWidth(80);

    // Show in dialog
    JDialog dialog = new JDialog(this, "Catalogue des Livres", true);
    dialog.add(new JScrollPane(table));
    dialog.setSize(800, 600);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void ViewMesEmprunts() {
    // Create table model
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("RFID");
    model.addColumn("Titre");
    model.addColumn("Auteur");
    model.addColumn("Date Emprunt");
    model.addColumn("Date Retour Prévue");

    // Populate table
    for (String[] emprunt : borne.getMesEmprunts()) {
        model.addRow(emprunt);
    }

    // Create table
    JTable table = new JTable(model);

    // Show in dialog
    JDialog dialog = new JDialog(this, "Mes Emprunts", true);
    dialog.add(new JScrollPane(table));
    dialog.setSize(800, 400);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void Cancel() {
    borne.annulerSaisie();
    updateDisplay();
    resetInterface();
}

private void updateDisplay() {
    displayArea.setText(borne.getSessionSummary());
}

private void resetInterface() {
    clientIDLabel.setVisible(true);
    clientIDInput.setVisible(true);
    NIPLabel.setVisible(true);
    NIPInput.setVisible(true);
    logInBtn.setVisible(true);
    rfidInput.setVisible(false);
    scanBtn.setVisible(false);
    finishBtn.setVisible(false);
    confirmBtn.setVisible(false);
    returnBtn.setVisible(false);
    catalogueBtn.setVisible(false);
    myLoansBtn.setVisible(false);
    cancelBtn.setVisible(false);
    displayArea.setVisible(false);
    clientIDInput.setText("");
    NIPInput.setText("");
}
public static void main(String[] args)
{
    Interface Interface = new Interface();
    Interface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Interface.setSize(1000, 600);
    Interface.setVisible(true);
}//Fin de la méthode main
}//Fin de la classe LabelFrame