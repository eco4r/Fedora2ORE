package org.hbz.eco4r.main;

import org.apache.log4j.Logger;
import org.dspace.foresite.ORESerialiser;
import org.dspace.foresite.ORESerialiserException;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		try {
			if (args.length != 3) {
				logger.info("USAGE: java -jar Fedora2ORE.jar <path-to-config-file> <pid> <rdf-format>");
				logger.info("You can select one of the following format: ATOM-1.0,  RDF/XML,  " +
						"TURTLE,  N-TRIPLE,  N3,  RDF/XML-ABBREV");
			}
			else {
				String path = args[0];
				String pid = args[1];
				String format = args[2];
				ResourceMapGenerator generator = new ResourceMapGenerator(path);
				ResourceMap rem = generator.generateResourceMap(pid);
				ORESerialiser serialiser = ORESerialiserFactory.getInstance(format);
				ResourceMapDocument remDoc;
				
				if (format.compareTo("ATOM-1.0") == 0) 
					remDoc = serialiser.serialise(rem);
				else 
					remDoc = serialiser.serialiseRaw(rem);
				
				String remString = remDoc.getSerialisation();
				
				logger.info(remString);
			}
		} 
		catch (ORESerialiserException e) {
			e.printStackTrace();
		}
	}
}
