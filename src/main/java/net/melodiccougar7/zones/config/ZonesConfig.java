package net.melodiccougar7.zones.config;

import com.corrinedev.jsconf.api.Config;
import com.corrinedev.jsconf.api.ConfigComment;
import com.corrinedev.jsconf.api.ConfigValue;
import com.google.common.reflect.TypeToken;
import net.melodiccougar7.zones.Zones;
import net.melodiccougar7.zones.util.Point;
import net.melodiccougar7.zones.util.VertexList;
import net.melodiccougar7.zones.util.ZoneData;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Zones.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ZonesConfig {
    public static final Config ZONE_CONFIG = new Config("zones_config");
    public static final ConfigComment ZONE_ORDERING;
    public static final ConfigValue<List<ZoneData>> ZONES_LIST;


    static {
        final VertexList defaultVList =new VertexList();
        defaultVList.addVertex(new Point(0,0));
        defaultVList.addVertex(new Point(0,10));
        defaultVList.addVertex(new Point(10,10));
        defaultVList.addVertex(new Point(10,0));

        ZONE_ORDERING = new ConfigComment("Zones are added in lists of points. [(0,0),(10,0),(10,10),(0,10)] defines a square", ZONE_CONFIG);
        // Deprecated attempt at making a config system that's just lists of lists in case this is easier to parse
        //ZONES_LIST = new ConfigValue(new LinkedHashMap(Map.of("default_zone", List.of(List.of(0,0), List.of(10,0), List.of(10,10), List.of(0,10)), "zones", ZONE_CONFIG, (new TypeToken<List<List<Integer>>>(){}).getType())))
        ZONES_LIST = new ConfigValue(List.of(
                new ZoneData(1, "industrial", "default_translation_key", defaultVList)
                ),
                "zones", ZONE_CONFIG, (new TypeToken<List<ZoneData>>() {
            }).getType()
        );
    }

    public static void register() {
        ZONE_CONFIG.register();
    }
}
