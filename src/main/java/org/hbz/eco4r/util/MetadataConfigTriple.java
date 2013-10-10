package org.hbz.eco4r.util;

/**
 * <b>Package Name: org.hbz.eco4r.util</b>
 * <b>Package Description: </b>
 * <p>This package contains Util classes</p>
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

/**
 * <b>Class Name</b>: MetadataConfigTriple</br>
 * <b>Class Definition</b>:
 * <p>Representation metadata configuration triples</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */

public class MetadataConfigTriple {

	private String datastreamID;
	private String datastreamNamespace;
	private List<String> metadataKeys;
	
	public MetadataConfigTriple(String datastreamID, String datastreamNamespace,
			List<String> metadataKeys) {
		this.datastreamID = datastreamID;
		this.datastreamNamespace = datastreamNamespace;
		this.metadataKeys = metadataKeys;
	}
	
	@Override
	public String toString() {
		String str = "";
		
		str += "DS_ID: " + datastreamID;
		str += "   DS_NAMESPACE:  " + datastreamNamespace;
		str += "   METADATA_KEYS:  " + metadataKeys;
		
		return str;
	}
	
	public String getDatastreamID() {
		return datastreamID;
	}
	public void setDatastreamID(String datastreamID) {
		this.datastreamID = datastreamID;
	}
	public String getDatastreamNamespace() {
		return datastreamNamespace;
	}
	public void setDatastreamNamespace(String datastreamNamespace) {
		this.datastreamNamespace = datastreamNamespace;
	}
	public List<String> getMetadataKeys() {
		return metadataKeys;
	}
	public void setMetadataKeys(List<String> metadataKeys) {
		this.metadataKeys = metadataKeys;
	}	
}
