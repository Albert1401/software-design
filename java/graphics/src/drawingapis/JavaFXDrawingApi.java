package drawingapis;

import figures.Circle;
import figures.Line;
import figures.Text;
import graphs.GraphMatrix;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class JavaFXDrawingApi extends Application implements DrawingApi {
    private static double width = 600;
    private static double height = 480;
    private static List<Line> lines = new ArrayList<>();
    private static List<Circle> circles = new ArrayList<>();
    private static List<Text> texts = new ArrayList<>();

    public JavaFXDrawingApi() {
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
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drawing circle");
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (Circle c : circles) {
            gc.fillOval(c.x, c.y, c.r, c.r);
        }

        for (Line line : lines) {
            gc.strokeLine(line.x1, line.y1, line.x2, line.y2);
        }

        for (Text text : texts) {
            gc.fillText(text.text, text.x, text.y);
        }

        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


}
