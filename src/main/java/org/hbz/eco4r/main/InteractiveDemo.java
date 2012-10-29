package org.hbz.eco4r.main;

/**
 * <b>Package Name: org.hbz.eco4r.main</b>
 * <b>Package Description: </b>
 * <p>Main package for launching the Application</p>
 *
 * -----------------------------------------------------------------------------
 * 
 * This file is part of the eco4r-Project funded by the German Research Foundation - DFG. 
 * It is created by Library Service Center North Rhine Westfalia (Cologne) and the University of Bielefeld.

 * <b>License and Copyright:</b> </br>
 * <p>The contents of this file are subject to the
 * D-FSL License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at <a href="http://www.dipp.nrw.de/dfsl/">http://www.dipp.nrw.de/dfsl/.</a></p>
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.</p>
 *
 * <p>Portions created for the Fedora Repository System are Copyright &copy; 2002-2005
 * by The Rector and Visitors of the University of Virginia and Cornell
 * University. All rights reserved."</p>
 *
 * -----------------------------------------------------------------------------
 *
 * <b>Creator(s): @author Anouar Boulal, boulal@hbz-nrw.de</b>
 *
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.dspace.foresite.ORESerialiser;
import org.dspace.foresite.ORESerialiserException;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;

/**
 * <b>Class Name</b>: Demo</br>
 * <b>Class Definition</b>:
 * <p>InteractiveDemo class for launching the applications.</p> 
 * 
 * @author Anouar Boulal
 *
 */

public class InteractiveDemo {

	private static Logger logger = Logger.getLogger(InteractiveDemo.class);

	public static void main(String[] args) throws ORESerialiserException, IOException {
		
		try {
			
			InputStreamReader isr = new InputStreamReader(System.in);
		    BufferedReader br = new BufferedReader(isr);
		    System.out.print("Please enter the pid: ");
		    String pid_in = br.readLine().trim();
		    System.out.println("Please enter the file path where the Resource-Map should be stored: ");
		    String file_in = br.readLine().trim();
		    System.out.println("Please select one of the following RDF-Formats: ");
		    System.out.println("ATOM-1.0,  RDF/XML,  TURTLE,  N-TRIPLE,  N3,  RDF/XML-ABBREV");
		    String RDFFormat_in = br.readLine().trim();
		    
		    String path = "configuration";
			ResourceMapGenerator generator = new ResourceMapGenerator(path);
			ResourceMap rem = generator.generateResourceMap(pid_in);
			
			ORESerialiser serialiser = ORESerialiserFactory.getInstance(RDFFormat_in);
			ResourceMapDocument remDoc = serialiser.serialise(rem);
			String remString = remDoc.toString();
			
			logger.info("Createing file: " + file_in);
			
			File file = new File(file_in);
			file.createNewFile();
			
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(remString);
			
			out.close();
			fstream.close();
			
			logger.info("Resource Map was generated, trying to create file in: " + file_in);
		}
		catch (Exception e){}
	}
}
