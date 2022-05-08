package org.matsim.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.network.Link;

/* MATSim seminar 3 or 4
* */
public class LinkVolumeEventHandler implements LinkLeaveEventHandler {

    private Id<Link> targetLinkId = Id.createLinkId("7235731770209f");
    private final int[] hourlyVolumes = new int[30];   // array
//    Key: how to get the data from main coding?
//    make targetLinkId and hourlyVolumes public, so that we can visit them and change them.
//    Or, you must import getter method and setter method.

//    Key: How to change the data from main coding? For example, we want change the inputs.
//    make targetLinkId not final, and import setter method.

//    private final List<Integer> hourlyVolumes = new ArrayList<>();  // list

    public void setTargetLinkId(Id<Link> linkId){
        this.targetLinkId = linkId;
    }
    public int[] getHourlyVolumes() {
        return hourlyVolumes;
    }

    public Id<Link> getTargetLinkId() {
        return targetLinkId;
    }

    @Override
    public void handleEvent(LinkLeaveEvent event) {
        if (event.getLinkId().equals(targetLinkId)){
            var departureTime = event.getTime();
            int bin = (int) (departureTime / 3600);
            hourlyVolumes[bin] += 1;
        }
    }
}
