import javax.swing.JFrame;

public class Main{
    public static void main(String[] args)
{
    Interface gui = new Interface();
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui.setSize(850, 300);
    gui.setVisible(true);
}
}