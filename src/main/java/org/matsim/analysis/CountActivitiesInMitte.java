package org.matsim.analysis;

import org.locationtech.jts.geom.Geometry;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.gis.ShapeFileReader;

import java.util.stream.Collectors;

/* MATSim seminar 5
* Count activities in mitte, a district in berlin.
* Key 1: how to read shapefile,
* Key 2: how to get geometry and attributes from shp?    A shp consists of geometry and attributes. Attributes is a format like map.
* Key 3: how to filter shp for some features?   A shp consists of many features, each of which is a vector.
* Key 2: how to get activities from plans, excluding stage activities?  Stage activities are interactions.
* Key 3: how to transfer MATSim coord with geotools coord ?  MATSim coord has its own format, different from geotools's.
* Key 4: how to transfer a coord to point (Geometry)?
* */
public class CountActivitiesInMitte {
    public static void main(String[] args) {
        var shapeFilePath = "C:\\Users\\Spring\\Desktop\\Bezirke_-_Berlin\\Berlin_Bezirke.shp";
        var planPath = "D:\\softwares\\MATSim\\matsim-berlin\\scenarios\\berlin-v5.5-1pct\\output\\berlin-v5.5.3-1pct.output_plans.xml.gz";
        var coordinateTransformation = TransformationFactory.getCoordinateTransformation("EPSG:31468", "EPSG:3857");

        var features = ShapeFileReader.getAllFeatures(shapeFilePath);

        var geometries = features.stream()
//                filter the shapefile first     // get the attributes from shp
                .filter(simpleFeature -> simpleFeature.getAttribute("Gemeinde_s").equals("001"))
//                get the geometry from the shapefile
                .map(simpleFeature -> (Geometry) simpleFeature.getDefaultGeometry())   // "(Geometry)" is important! And import "jts" rather than others packages.
//                transfer collection to list
                .collect(Collectors.toList());

        var geometryForMitte = geometries.get(0);

        var population = PopulationUtils.readPopulation(planPath);

        var counter = 0;

        for (Person person : population.getPersons().values()) {
            var plan = person.getSelectedPlan();
            var activities = TripStructureUtils.getActivities(plan, TripStructureUtils.StageActivityHandling.ExcludeStageActivities);   // Key: stageActivity!

            for (Activity activity : activities) {
                var coord = coordinateTransformation.transform(activity.getCoord());   // Key: coord transformation
//                var geotoolsCoord = MGC.coord2Coordinate(coord);  // geotools coord (coordinate)  Key: matsim coord is not geotools coordinate;
                var geotoolsPoint = MGC.coord2Point(coord);  // geotools coord (coordinate)  // Key: coord is not geometry.


                if (geometryForMitte.contains(geotoolsPoint)){
                    counter += 1;
                }
            }
        }
        System.out.println("Activities in Mitte:" + counter);
    }
}
