package org.matsim.analysis;

import org.matsim.core.events.EventsUtils;

import java.util.Arrays;

public class SimpleAnalysis {
    public static void main(String[] args) {
        var simplePersonEventHandler = new SimplePersonEventHandler();
        var linkVolumeEventHandler = new LinkVolumeEventHandler();
//        you can make some changes for linkVolumeEventHandler.
//        linkVolumeEventHandler.setTargetLinkId();
        var manager = EventsUtils.createEventsManager();

        manager.addHandler(simplePersonEventHandler);
        manager.addHandler(linkVolumeEventHandler);
        EventsUtils.readEvents(manager, "D:\\softwares\\MATSim\\matsim-serengeti-park-hodenhagen\\scenarios\\serengeti-park-v1.0\\output\\output-serengeti-park-v1.0-run1\\serengeti-park-v1.0-run1.output_events.xml.gz");

        System.out.println("Target link Id:" + linkVolumeEventHandler.getTargetLinkId());
        System.out.println("Hourly volumes:");
        System.out.println(Arrays.toString(linkVolumeEventHandler.getHourlyVolumes()));
    }
}
