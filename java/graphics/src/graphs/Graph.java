package graphs;

import drawingapis.DrawingApi;

public abstract class Graph {

    /**
     * Bridge to drawing api
     */
    protected DrawingApi drawingApi;

    public Graph(DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
    }

    public abstract void drawGraph();
}