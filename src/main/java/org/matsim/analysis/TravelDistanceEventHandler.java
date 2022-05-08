package org.matsim.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.PersonEntersVehicleEvent;
import org.matsim.api.core.v01.events.PersonLeavesVehicleEvent;
import org.matsim.api.core.v01.events.TransitDriverStartsEvent;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.events.handler.PersonEntersVehicleEventHandler;
import org.matsim.api.core.v01.events.handler.PersonLeavesVehicleEventHandler;
import org.matsim.api.core.v01.events.handler.TransitDriverStartsEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.network.NetworkUtils;
import org.matsim.vehicles.Vehicle;

import java.util.*;

/* MATSim seminar 4 homework. BUG
 * This handler collects travel distances for each trip a person has conducted during the simulation.
 * It sums up the lengths of all the links a person has passed during a trip.
 * */

public class TravelDistanceEventHandler implements LinkLeaveEventHandler, PersonEntersVehicleEventHandler, TransitDriverStartsEventHandler, PersonLeavesVehicleEventHandler {

    //    map vehicle id to person id
    private final Map<Id<Vehicle>, Id<Person>> vehicle2person = new HashMap<>();
    //    map person id to travel distances in trips he has conducted
    private final Map<Id<Person>, List<Double>> person2TravelDistances = new HashMap<>();   // we don't know how many trips for each person
    //    collect all transit drivers
    private final Set<Id<Person>> transitDriverId = new HashSet<>();
    //    we need network to calculate the length of links.
    private Network network = NetworkUtils.createNetwork();

    public TravelDistanceEventHandler(Network network) {
        this.network = network;
    }

    //        Tick: Coding event handlers according to In the order of events will help a lot.

    /*
    * Listen for transitDriverStartsEvent because transit drivers also generate "PersonEntersVehicleEvent" and all other evnets.
    * Since we are not interested in the travelled distances of transit vehicles we store the person-ids of
    * all transit drivers and ignore all events related to those drivers.
    *
    * If we consider transit vehicles, one vehicle may have multiple persons. This code will be false.
    *
    * @param transitDriverStartsEvent
    * */
    @Override
    public void handleEvent(TransitDriverStartsEvent transitDriverStartsEvent) {
        transitDriverId.add(transitDriverStartsEvent.getDriverId());
    }

    @Override
    public void handleEvent(PersonEntersVehicleEvent personEntersVehicleEvent) {
        if (!transitDriverId.contains(personEntersVehicleEvent.getPersonId())){
            vehicle2person.put(personEntersVehicleEvent.getVehicleId(), personEntersVehicleEvent.getPersonId());
            person2TravelDistances.computeIfAbsent(personEntersVehicleEvent.getPersonId(), personId -> new ArrayList<>()).add(0.0);
        }

    }

    @Override
    public void handleEvent(LinkLeaveEvent linkLeaveEvent) {
//        only cars, not transit vehicles.
        if(vehicle2person.containsKey(linkLeaveEvent.getVehicleId())){
            var vehicleId = linkLeaveEvent.getLinkId();
            var linkId = linkLeaveEvent.getLinkId();
            var personId = vehicle2person.get(vehicleId);
            var distances = person2TravelDistances.get(personId);
//            get the current trip distance!
//            202205223 我不知道为啥，这个地方会报错，空指针异常，但是时间有限，后续有时间再研究吧。
            var distance = distances.get(distances.size() - 1);
//            set the current trip distance. 提取 map 的值，对值进行更改，也就是对 map 进行了更改，因为全是 引用。
            distances.set(distances.size() - 1, distance + network.getLinks().get(linkId).getLength() );
        }
    }

    @Override
    public void handleEvent(PersonLeavesVehicleEvent personLeavesVehicleEvent) {
        if (!transitDriverId.contains(personLeavesVehicleEvent.getPersonId())){
//            the person is not in the vehicle anymore, so remove the record as well. // Note
            vehicle2person.remove(personLeavesVehicleEvent.getVehicleId());
        }
    }
}
