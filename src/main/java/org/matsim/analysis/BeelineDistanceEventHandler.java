package org.matsim.analysis;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* MATSim seminar 4 homework
* */
public class BeelineDistanceEventHandler implements PersonDepartureEventHandler, PersonArrivalEventHandler {

    private Network network = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getNetwork();
    private final Map<Id<Person>, Coord> person2departureCoord = new HashMap<>();
//    the actual data container which stores a list of distances fo each person.
//    each trip has its own value in the list of trips which is associated with the if of a person
    private final Map<Id<Person>, List<Double>> person2beelineDistances = new HashMap<>();    // NOTE. We don't know how many trips for each person, so List is used.
    private final int[] beelineDistanceBins = new int[5]; // distance bins 0-1000, 1000-5000, 5000-10000, 10000-20000, > 20000m

//    public void setNetwork(String networkPath) {
//        this.network = NetworkUtils.readNetwork(networkPath);
//    }
//    We can also use the constructor function to pass the network from outside.
    public BeelineDistanceEventHandler(Network network) {
        this.network = network;
    }

    public Map<Id<Person>, List<Double>> getPerson2beelineDistance() {
        return person2beelineDistances;
    }

    public int[] getBeelineDistanceBins() {
        return beelineDistanceBins;
    }

    @Override
    public void handleEvent(PersonArrivalEvent personArrivalEvent) {
        var arrivalPersonId = personArrivalEvent.getPersonId();
        var arrivalLinkId = personArrivalEvent.getLinkId();

//        get the coordinate of the arrival
//        also remove the entry because our person is not conducting a trip anymore.
        var departureCoord = person2departureCoord.remove(arrivalPersonId);   // Ticks
        var arrivalCoord = network.getLinks().get(arrivalLinkId).getCoord();

        var beelineDistance = CoordUtils.calcEuclideanDistance(departureCoord, arrivalCoord);  // Ticks

//        get the list of trips for the person's id and add the distance to that list
//        the "compute if absent" will put a new list into our map in case non is present yet.
//        the "compute if absent" will return the list, so we can "add" new element.
        person2beelineDistances.computeIfAbsent(arrivalPersonId, personId -> new ArrayList<>()).add(beelineDistance);  // NOTE

//        count beeline distances to bins
        if (beelineDistance <= 1000) {
            beelineDistanceBins[0] += 1;
        } else if (beelineDistance > 1000 && beelineDistance <= 5000) {
            beelineDistanceBins[1] += 1;
        } else if (beelineDistance > 5000 && beelineDistance <= 10000) {
            beelineDistanceBins[2] += 1;
        } else if (beelineDistance > 10000 && beelineDistance <= 20000) {
            beelineDistanceBins[3] += 1;
        } else {
            beelineDistanceBins[4] += 1;
        }
    }

    @Override
    public void handleEvent(PersonDepartureEvent personDepartureEvent) {
        var departurePersonId = personDepartureEvent.getPersonId();
        var departureLinkId = personDepartureEvent.getLinkId();
        Coord departureCoord = network.getLinks().get(departureLinkId).getCoord();
        person2departureCoord.put(departurePersonId, departureCoord);

    }
}
