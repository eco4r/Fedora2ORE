package org.hbz.eco4r.vocabulary;

/**
 * <b>Package Name: org.hbz.eco4r.vocabulary</b>
 * <b>Package Description: </b>
 * <p>This package classes for metadata vocabularies</p>
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

/**
 * <b>Class Name</b>: ConfigVocabulary</br>
 * <b>Class Definition</b>:
 * <p>Mirror class for the properties in the configuration file.</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */

public class ConfigVocabulary {

	public static final String FEDORA_PROTOCOL = "fedora.protocol";
	public static final String FEDORA_USER = "fedora.user";
	public static final String FEDORA_PASSWD= "fedora.password";
	public static final String FEDORA_HOST = "fedora.host";
	public static final String FEDORA_PORT= "fedora.port";
	public static final String FEDORA_SERVICE_SUFFIX= "fedora.serviceSuffix";
	
	public static final String REM_METADATA_BASEURI = "rem.metadata.baseURI";
	public static final String REM_METADATA_CREATOR_URI = "rem.metadata.creator.URI";
	public static final String REM_METADATA_CREATOR_NAME = "rem.metadata.creator.name";
	public static final String REM_METADATA_CREATOR_MBOX = "rem.metadata.creator.mbox";
	public static final String REM_METADATA_RIGHTS = "rem.metadata.rights";
	
	public static final String SEARCH_DEPTH = "searchDepth";
	
	public static final String EXCLUDE_RES_BY_PID = "excludeResourcesByPID";
	public static final String EXCLUDE_RELS = "excludeRelationships";
	public static final String AGGREGATES_RELS = "aggregates.relationships";
	public static final String EXCLUDE_DATASTREAMS_BY_ID = "excludeDatastreamsByID";
	public static final String EXCLUDE_DATASTREAMS_BY_URI = "excludeDatastremsByURI";
	public static final String METADATA_NAMESPACE = "metadata.namespace";
	public static final String METADATA_KEYS = "metadata.keys";
	public static final String METADATA = "metadata";
}
