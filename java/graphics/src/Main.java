import drawingapis.AwtDrawingApi;
import drawingapis.DrawingApi;
import drawingapis.JavaFXDrawingApi;
import graphs.GraphList;
import graphs.GraphMatrix;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DrawingApi api1 = new JavaFXDrawingApi();
        DrawingApi api2 = new AwtDrawingApi();

        boolean[][] gr = new boolean[][]{
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
                {true, true, true, true, true},
        };
        List<Pair<Integer, Integer>> gr2 = Arrays.asList(
                new Pair<>(0, 5),
                new Pair<>(2, 3),
                new Pair<>(5, 9),
                new Pair<>(8, 3),
                new Pair<>(4, 4),
                new Pair<>(4, 1)
        );
        new GraphList(api1, gr2).drawGraph();
        new GraphList(api2, gr2).drawGraph();

//        new GraphMatrix(api1, gr).drawGraph();
//        new GraphMatrix(api2, gr).drawGraph();


    }
}
