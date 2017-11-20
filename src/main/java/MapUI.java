import java.awt.*;
import javax.swing.JPanel;

public class MapUI extends JPanel {

    Plan plan = new Plan();

    public void paintComponent(Graphics g){

        plan.afficher(g);
    }
}