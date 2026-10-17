package net.melodiccougar7.zones.api;

import com.mojang.logging.LogUtils;
import net.melodiccougar7.zones.config.ZonesConfig;
import net.melodiccougar7.zones.util.Point;
import net.melodiccougar7.zones.util.VertexList;
import net.melodiccougar7.zones.util.ZoneData;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.util.List;

public class ZoneCheck {
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * This method will give you everything you need to know about the most significant zone
     *
     * @param player ServerPlayer for safety but client player also works if the configs are synced iirc
     * @return Most significant zone and its ZoneData
     */
    public static ZoneData getMSZ(Player player) {
        int x = player.getBlockX();
        int y = player.getBlockZ();

        // get the config
        List<ZoneData> zoneDataList = ZonesConfig.ZONES_LIST.get();
        ZoneData mostSignificantZone;
        if (!zoneDataList.isEmpty()) {
            mostSignificantZone = zoneDataList.get(0); //be sure that the config always has one value
        } else {
            LOGGER.warn("Zone config is empty! Please add values to utilize it");
            return new ZoneData(0, "error", "error", new VertexList());
        }

        for (ZoneData zone : zoneDataList) {
            if (isInZone(new Point(x, y), zone.vList) && zone.priority > mostSignificantZone.priority) {
                mostSignificantZone = zone;
            }
        }

        return mostSignificantZone;
    }

    /**
     * Standard check of whether a point is inside a polygon, no AABB "optimization"
     *
     * @param testPoint Point to test
     * @param vList     Polygon definition from
     * @return is a point within the given polygon
     */
    public static boolean isInZone(Point testPoint, VertexList vList) {
        int nvert = vList.size();
        // (Thank you stack overflow
        // https://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon
        // this method adapted from above)

        // ... then check if the point is actually in the defined zone/polygon
        return polygonCheck(nvert, testPoint, vList);
    }

    /**
     * Alternative check of whether a point is inside a polygon, with AABB "optimization".
     * The AABB check has been known to slow down this algorithm. Use with caution and for
     * huge areas only or in situations in which there are many zones in your config.
     *
     * @param testPoint Point to test
     * @param vList     Polygon definition from
     * @return is a point within the given polygon
     */
    public static boolean isInZoneWithAABB(Point testPoint, VertexList vList) {
        // First make an AABB to be sure we're at least near the zone

        int nvert = vList.size();

        // Iterate over the polygon and get the extreme points ...
        int xMax = vList.get(0).x;
        int yMax = vList.get(0).y;
        int xMin = xMax;
        int yMin = yMax;

        for (int i = 1; i < nvert; i++) {
            // Check for max values
            int x = vList.get(i).x;
            int y = vList.get(i).y;

            if (x > xMax) {
                xMax = x;
            }
            if (y > yMax) {
                yMax = y;
            }
            if (x < xMin) {
                xMin = x;
            }
            if (y < yMin) {
                yMin = y;
            }
        }

        //ensure that this isn't the problem with the zone algorithm
        // WARNING: may make this check slower if the zone is tiny / has very few sides. We'll have to do some testing
        if (!((testPoint.x <= xMax && testPoint.x >= xMin) && (testPoint.y <= yMax && testPoint.y >= yMin))) {
            // If the point is not within these max values, return immediately
            return false;
        }

        return polygonCheck(nvert, testPoint, vList);
    }

    private static boolean polygonCheck(int nvert, Point testPoint, VertexList vList) {
        // (Thank you stack overflow
        // https://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon
        // this method adapted from above)

        //Check if the point is actually in the defined zone/polygon
        int i;
        int j;
        boolean c = false;
        for (i = 0, j = nvert - 1; i < nvert; j = i++) {
            if (((vList.get(i).y > testPoint.y) != (vList.get(j).y > testPoint.y)) &&
                    (testPoint.x < (vList.get(j).x - vList.get(i).x) * (testPoint.y - vList.get(i).y) /
                            (vList.get(j).y - vList.get(i).y) + vList.get(i).x)) {
                c = !c;
            }
        }
        return c;
    }
}
