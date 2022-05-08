package org.matsim.analysis;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.NetworkUtils;

public class RunEventHandler {
    public static void main(String[] args) {
//        import network for the beelineDistanceEventHandler, travelDistanceEventHandler
        Network network = NetworkUtils.readNetwork("D:\\softwares\\MATSim\\matsim-berlin\\scenarios\\berlin-v5.5-1pct\\output\\berlin-v5.5.3-1pct.output_network.xml.gz");
        var beelineDistanceEventHandler = new BeelineDistanceEventHandler(network);
        var travelDistanceEventHandler = new TravelDistanceEventHandler(network);  // The EventHandler has BUGs.
        var travelDistanceEventHandler2 = new TravelDistanceEventHandler2(network);
        var eventsManager= EventsUtils.createEventsManager();

//        eventsManager.addHandler(beelineDistanceEventHandler);
        eventsManager.addHandler(travelDistanceEventHandler);
//        eventsManager.addHandler(travelDistanceEventHandler2);
        EventsUtils.readEvents(eventsManager, "D:\\softwares\\MATSim\\matsim-berlin\\scenarios\\berlin-v5.5-1pct\\output\\berlin-v5.5.3-1pct.output_events.xml.gz");

//        get the beeline distance.
//        System.out.println(Arrays.toString(beelineDistanceEventHandler.getBeelineDistanceBins()));
//        System.out.println(travelDistanceEventHandler.getPerson2TravelDistance());
    }
}
