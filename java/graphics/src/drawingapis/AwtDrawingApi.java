package drawingapis;

import figures.Circle;
import figures.Line;
import figures.Text;
import graphs.GraphMatrix;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class AwtDrawingApi extends Frame implements DrawingApi {
    private double width = 600;
    private double height = 480;
    private double vR = 20;
    private List<Line> lines = new ArrayList<>();
    private List<Circle> circles = new ArrayList<>();
    private List<Text> texts = new ArrayList<>();

    public AwtDrawingApi() {

    }

    @Override
    public void paint(Graphics g) {
        Graphics2D ga = (Graphics2D) g;
        ga.setPaint(Color.black);
        for (Circle circle : circles) {
            ga.fill(new Ellipse2D.Double(circle.x, circle.y, circle.r, circle.r));
        }
        for (Line line : lines) {
            ga.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
        }
        for (Text text : texts) {
            ga.drawString(text.text, (int) text.x, (int) text.y);
        }
    }

    @Override
    public double getDrawingAreaWidth() {
        return width;
    }

    @Override
    public double getDrawingAreaHeight() {
        return height;
    }

    @Override
    public void drawCircle(double x, double y, double r) {
        circles.add(new Circle(x - r / 2, y - r / 2, r));
    }

    @Override
    public void drawLine(double x1, double y1, double x2, double y2) {
        lines.add(new Line(x1, y1, x2, y2));
    }

    @Override
    public void drawText(double x, double y, String text) {
        texts.add(new Text(x, y, text));
    }

    @Override
    public void plot() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        setSize((int) width, (int) height);
        setVisible(true);
    }

    public static void main(String[] args) {
        DrawingApi api = new AwtDrawingApi();
        GraphMatrix graphMatrix = new GraphMatrix(api, new boolean[][]{
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
        });
        graphMatrix.drawGraph();
    }
}