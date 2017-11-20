import java.awt.*;

public class Plan implements Drawable
{

    @Override
    public void afficher(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(100,100,1000,600);
        g2d.drawLine(70,600,300,400);

    }
}