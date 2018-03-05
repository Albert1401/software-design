package graphs;

import drawingapis.DrawingApi;

public class GraphMatrix extends Graph {
    private final boolean[][] adjMatrix;

    public GraphMatrix(DrawingApi drawingApi, boolean[][] matrix) {
        super(drawingApi);
        adjMatrix = matrix;
        if (adjMatrix.length > 0 && adjMatrix.length != adjMatrix[0].length) {
            throw new IllegalArgumentException("asd");
        }
    }

    public GraphMatrix(DrawingApi drawingApi) {
        super(drawingApi);
        adjMatrix = new boolean[0][0];
    }

    @Override
    public void drawGraph() {
        int n = adjMatrix.length;
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
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adjMatrix[i][j]) {
                    drawingApi.drawLine(nodesCoords[i][0], nodesCoords[i][1],
                            nodesCoords[j][0], nodesCoords[j][1]);
                }
            }
        }
        drawingApi.plot();
    }

}
