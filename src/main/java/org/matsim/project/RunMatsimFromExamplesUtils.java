package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.net.URL;

class RunMatsimFromExamplesUtils{

	public static void main( String[] args ){

		URL context = org.matsim.examples.ExamplesUtils.getTestScenarioURL( "equil-mixedTraffic" );
		URL url = IOUtils.extendUrl( context, "config-with-mode-vehicles.xml" );

		Config config = ConfigUtils.loadConfig( url );
		config.controler().setOverwriteFileSetting( OverwriteFileSetting.deleteDirectoryIfExists );
		config.controler().setLastIteration(0);
		config.controler().setOutputDirectory("equil-mixedTraffic");

		// ---

		Scenario scenario = ScenarioUtils.loadScenario( config );

		// ---

		Controler controler = new Controler( scenario );

		// ---

		controler.run();

	}

}
