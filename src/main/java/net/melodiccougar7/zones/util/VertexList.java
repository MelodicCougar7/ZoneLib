package net.melodiccougar7.zones.util;

import java.util.ArrayList;
import java.util.List;

public class VertexList {
    public ArrayList<Point> vertexList;

    public VertexList(ArrayList<Point> point) {
        this.vertexList = new ArrayList<Point>();
            vertexList.addAll(point);
    }
    public VertexList() {
        this.vertexList = new ArrayList<Point>();
    }
    public void addVertex(Point point) {
        this.vertexList.add(point);
    }

    public int size() {
        return vertexList.size();
    }

    public Point get(int index) {
        return vertexList.get(index);
    }
}
