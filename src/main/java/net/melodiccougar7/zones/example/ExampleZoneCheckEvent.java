package net.melodiccougar7.zones.example;

import com.mojang.logging.LogUtils;
import net.melodiccougar7.zones.config.ZonesConfig;
import net.melodiccougar7.zones.util.Point;
import net.melodiccougar7.zones.util.VertexList;
import net.melodiccougar7.zones.util.ZoneData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.List;

import static net.melodiccougar7.zones.api.ZoneCheck.getMSZ;
import static net.melodiccougar7.zones.api.ZoneCheck.isInZone;

//@Mod.EventBusSubscriber(modid = "zones", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExampleZoneCheckEvent {

    public static final Logger LOGGER = LogUtils.getLogger();

    //@SubscribeEvent
    public static void notifyPlayerWhenInZone(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (event.player.level().isClientSide) {
            return;
        }

        // every second
        if (event.player.tickCount % 20 == 0) {
            int x = event.player.getBlockX();
            int y = event.player.getBlockZ();

            // get the config
            List<ZoneData> zoneDataList = ZonesConfig.ZONES_LIST.get();

            boolean yourLogicDependingOnPointBeingInZone = false;
            ZoneData mostSignificantZone;

            // iterate over all zones, find the most important one
            for (ZoneData zone : zoneDataList) {
                VertexList vList = zone.vList;
                yourLogicDependingOnPointBeingInZone = isInZone(new Point(x, y), vList);
//                // Checking for MSZ within this loop here prevents looping over the data needlessly
//                // but requires that you initialize mostSignificantZone, which the method below
//                // handles for you
//
//                if (isInZone(new Point(x, y), zone.vList) && zone.priority > mostSignificantZone.priority) {
//                    mostSignificantZone = zone;
//                }
            }

            // alternative method of getting the most significant zone from the player
            mostSignificantZone = getMSZ(event.player);
        }
    }
}