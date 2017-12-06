package fr.insalyon.agile;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TronconUI {
    private Line line;
    private Color lineColor;

    public TronconUI (double x, double startY, double endY, double marge, double margeMax){
        lineColor = Color.BLACK;
        //.interpolate(Color.GREEN, marge / margeMax);

        line = new Line();
        line.setStroke(lineColor);
        line.setStrokeWidth(2);
        //line.getStrokeDashArray().addAll(4d); //pointillés
        line.setStartX(x);
        line.setStartY(startY);
        line.setEndX(x);
        line.setEndY(endY);
    }

    public void print(Pane pane){
        pane.getChildren().add(line);
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        line.setStroke(lineColor);
    }
}
