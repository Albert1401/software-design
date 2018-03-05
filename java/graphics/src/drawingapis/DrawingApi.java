package drawingapis;

public interface DrawingApi {
    double getDrawingAreaWidth();

    double getDrawingAreaHeight();

    void drawCircle(double x, double y, double r);

    void drawLine(double x1, double y1, double x2, double y2);

    void drawText(double x, double y, String text);

    void plot();
}