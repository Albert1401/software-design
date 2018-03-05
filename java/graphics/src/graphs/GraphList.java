package graphs;

import drawingapis.DrawingApi;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;

public class GraphList extends Graph {
    private final List<Pair<Integer, Integer>> edges;

    public GraphList(DrawingApi drawingApi, List<Pair<Integer, Integer>> edges) {
        super(drawingApi);
        this.edges = edges;
        for (Pair<Integer, Integer> edge : edges) {
            if (edge.getKey() < 0 || edge.getValue() < 0) {
                throw new IllegalArgumentException("Vertex index can't be less than zero");
            }
        }
    }

    public GraphList(DrawingApi drawingApi) {
        super(drawingApi);
        edges = Collections.emptyList();
    }

    @Override
    public void drawGraph() {
        if (edges.isEmpty()) {
            drawingApi.plot();
            return;
        }
        int n = 0;
        for (Pair<Integer, Integer> edge : edges) {
            n = Math.max(n, edge.getKey());
            n = Math.max(n, edge.getValue());
        }
        n++;

        double cx = drawingApi.getDrawingAreaWidth() / 2;
        double cy = drawingApi.getDrawingAreaHeight() / 2;
        double r = Math.min(drawingApi.getDrawingAreaHeight(), drawingApi.getDrawingAreaWidth()) / 3;

        long vR = 20;

        double nodesCoords[][] = new double[n][2];

        for (int i = 0; i < n; i++) {
            double ang = i * 2 * Math.PI / n;
            nodesCoords[i][0] = cx + Math.sin(ang) * r;
            nodesCoords[i][1] = cy + Math.cos(ang) * r;
        }

        for (int i = 0; i < nodesCoords.length; i++) {
            double x = nodesCoords[i][0];
            double y = nodesCoords[i][1];
            drawingApi.drawCircle(x, y, vR);
            drawingApi.drawText(x - vR / 4, y - vR * 1.2, Integer.toString(i));
        }
        for (Pair<Integer, Integer> edge : edges) {
            double x1 = nodesCoords[edge.getKey()][0];
            double y1 = nodesCoords[edge.getKey()][1];
            double x2 = nodesCoords[edge.getValue()][0];
            double y2 = nodesCoords[edge.getValue()][1];
            drawingApi.drawLine(x1, y1, x2, y2);
        }
        drawingApi.plot();
    }

//    public static void main(String[] args) {
//        DrawingApi api1 = new AwtDrawingApi();
//        DrawingApi api2 = new JavaFXDrawingApi();
//        GraphMatrix graphMatrix = new GraphMatrix(api2, new boolean[][]{
//                {true, true, true, true, true},
//                {true, true, true, true, true},
//                {true, true, true, true, true},
//                {true, true, true, true, true},
//                {true, true, true, true, true},
//        }
//        GraphList graphList = new GraphList(api2, Arrays.asList(
//                new Pair<>(0, 1),
//                new Pair<>(2, 3),
//                new Pair<>(10, 4),
//                new Pair<>(4, 7),
//                new Pair<>(0, 4)
//        ));
//        graphList.drawGraph();
//
//
//        GraphList graphList1 = new GraphList(api1, Arrays.asList(
//                new Pair<>(0, 1),
//                new Pair<>(2, 3),
//                new Pair<>(10, 4),
//                new Pair<>(4, 7),
//                new Pair<>(0, 4)
//        ));
//        graphList1.drawGraph();
//    }
}
