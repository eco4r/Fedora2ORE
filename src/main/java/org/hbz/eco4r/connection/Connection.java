package org.hbz.eco4r.connection;

/**
 * <b>Package Name: org.hbz.eco4r.connection</b>
 * <b>Package Description: </b>
 * <p>This package is about connection issues to the Fedora system. A Connection Object is established  
 * either through FedoraClient or HTTPCLient </p>
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

import java.util.List;

import org.apache.log4j.Logger;

/**
 * <b>Class Name</b>: Connection</br>
 * <b>Class Definition</b>:
 * <p>Holds connectors (either FedoraClient or HTTPClient) that are responsible
 * for the communication with Fedora. </p> 
 * 
 * @author Anouar Boulal
 *
 */
public class Connection {

	private static Logger logger = Logger.getLogger(Connection.class);
	
	private List<Object> connectionObjects;
	
	public Connection(List<Object> connectionObjects) {
		this.connectionObjects = connectionObjects;
	}

	public List<Object> getConnectionObjects() {
		return connectionObjects;
	}

	public void setConnectionObjects(List<Object> connectionObjects) {
		this.connectionObjects = connectionObjects;
	}
	
	@Override
	public String toString() {
		String string = "";
		if (this.connectionObjects == null)
			logger.error("The Connector class has not been initialized.");
		else {
			for (Object object : this.connectionObjects){
				string += object.getClass() + "\n";
			}
		}
		return string;
	}
}
