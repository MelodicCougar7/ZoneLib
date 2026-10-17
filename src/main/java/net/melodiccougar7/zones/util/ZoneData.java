package net.melodiccougar7.zones.util;

public class ZoneData {
    public int priority;
    public VertexList vList;
    public String name;
    public String translationKey;
    public ZoneData(int priority, String name, String translationKey, VertexList vList) {
        this.priority = priority;
        this.vList = vList;
        this.name = name;
        this.translationKey = translationKey;
    }
}