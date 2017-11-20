import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame
{
    public MainWindow()
    {
        setTitle("Deliver'IF");
        setSize(1200, 800);
        setLocationRelativeTo(null);

        MapUI map = new MapUI();
        this.setContentPane(map);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
