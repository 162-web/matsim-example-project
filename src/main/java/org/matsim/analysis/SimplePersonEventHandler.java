package org.matsim.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.api.core.v01.population.Person;

import java.util.HashMap;
import java.util.Map;

/* MATSim seminar 3 or 4 */
public class SimplePersonEventHandler implements PersonDepartureEventHandler, PersonArrivalEventHandler {

//    Key : We should construct global vars, because each handleEvent can't return anything, but we want to integration all handleEvents class.
    private final Map<Id<Person>, Double> person2DepartureTime = new HashMap<>();

//    Key : The sequence of codes doesn't matter, but you must be aware that events are handled one by one in time.
//    So, PersonDepartureEvent is before PersonArrivalEvent when handling.
    @Override
    public void handleEvent(PersonArrivalEvent personArrivalEvent) {
        var personArrivalTime = personArrivalEvent.getTime();
        var personID = personArrivalEvent.getPersonId();
        var travelTime = personArrivalTime - person2DepartureTime.get(personID);
        System.out.println(personID + "travel time " + travelTime);
//        System.out.println(personArrivalEvent.getTime() + ":" + personArrivalEvent.getPersonId() + ";" + personArrivalEvent.getLegMode());
    }

    @Override
    public void handleEvent(PersonDepartureEvent personDepartureEvent) {
        var personDepartureTime = personDepartureEvent.getTime();
        var personID = personDepartureEvent.getPersonId();
        person2DepartureTime.put(personID, personDepartureTime);
//        System.out.println(personDepartureEvent.getTime() + ":" + personDepartureEvent.getPersonId() + ";" + personDepartureEvent.getLegMode());
    }
}
