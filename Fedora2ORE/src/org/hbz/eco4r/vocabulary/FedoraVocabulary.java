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
 * <b>Class Name</b>: FedoraVocabulary</br>
 * <b>Class Definition</b>:
 * <p>The Fedora 3.3 Relationship Vokabulary</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */


public class FedoraVocabulary {
	
	/**
	 * Connection parameters
	 */
	public static final String FEDORA_PROTOCOL = "fedora.protocol";
	public static final String FEDORA_USER = "fedora.user";
	public static final String FEDORA_PASSWORD = "fedora.password";
	public static final String FEDORA_HOST = "fedora.host";
	public static final String FEDORA_PORT = "fedora.port";
	public static final String FEDORA_SERVICE_SUFFIX = "fedora.serviceSuffix";
	
	
	/**
	 * Fedora resource index query parameters
	 */
	public static final String RI_SERVICE_SUFFIX = "ri.serviceSuffix";
	public static final String RISEARCH_TYPE = "ri.type";
	public static final String RISEARCH_FLUSH = "ri.flush";
	public static final String RISEARCH_LANG = "ri.lang";
	public static final String RISEARCH_FORMAT = "ri.format";
	public static final String RISEARCH_LIMIT = "ri.limit";
	public static final String RISEARCH_DISTINCT = "ri.distinct";
	public static final String RISEARCH_STREAM = "ri.stream";
	public static final String RISEARCH_QUERY = "ri.query";
	public static final String RISEARCH_TEMPLATE = "ri.template";
	
	
	/**
	 * The possible values for result field (see API_A) 
	 * are the following:
	 */
	
	// key fields
	public static final String PID = "pid"; 
	public static final String LABEL = "label";
	public static final String STATE = "state";
	public static final String OWNER_ID = "ownerId";
	public static final String C_DATE = "cDate";
	public static final String M_DATE = "mDate";
	public static final String DC_M_DATE = "dcmDate";
	// Dublin core fields
	public static final String TITLE = "title";
	public static final String CREATOR = "creator";
	public static final String SUBJECT = "subject";
	public static final String DESCRIPTION = "description";
	public static final String PUBLISHER = "publisher";
	public static final String CONTRIBUTOR = "contributor";
	public static final String DATE = "date";
	public static final String FORMAT = "format";
	public static final String IDENTIFIER = "identifier";
	public static final String SOURCE = "source";
	public static final String LANGUAGE = "language";
	public static final String RELATION = "relation";
	public static final String COVERAGE = "coverage";
	public static final String RIGHTS = "rights";
	// All result fields array
	public static final String[] ALL_OBJECT_FIELDS = new String[] {
		PID, LABEL, STATE, OWNER_ID, C_DATE, M_DATE, DC_M_DATE, 
		TITLE, CREATOR, SUBJECT, DESCRIPTION,PUBLISHER,  CONTRIBUTOR, 
		DATE, FORMAT, IDENTIFIER, SOURCE, LANGUAGE, RELATION, COVERAGE,
		RIGHTS}; 
	
	
	/**
	 * Resource map parameters 
	 */
	public static final String ECO4R_NAMESPACE = "http://www.eco4r.org/";
	public static final String ECO4R_TERMS_NAMESPACE = ECO4R_NAMESPACE + "terms/";
	public static final String ECO4R_AGGREGATION_NAMESPACE = ECO4R_NAMESPACE + "Aggregation/";
	public static final String ECO4R_RESOURCEMAP_NAMESPACE = ECO4R_NAMESPACE + "ResourceMap/";
	public static final String ECO4R_AGGREGATED_RESOURCE_NAMESPACE = ECO4R_NAMESPACE + "AggregatedResource/";
	public static final String ECO4R_PROXY_NAMESPACE = ECO4R_NAMESPACE + "Proxy/";
	
	
	/**
	 * DiPP Object to Object Relationships
	 */
	//Default RELS-EXT namespace
	public static final String DEFAULT_RELS_EXT_NAMESPACE = "info:fedora/fedora-system:def/relations-external#";
	public static final String DIPP_HAS_CONSTITUENT = DEFAULT_RELS_EXT_NAMESPACE + "hasConstituent";
	public static final String DIPP_HAS_COLLECTION_MEMBER = DEFAULT_RELS_EXT_NAMESPACE + "hasCollectionMember";
	public static final String DIPP_HAS_SUBSET = DEFAULT_RELS_EXT_NAMESPACE + "hasSubset";
	public static final String DIPP_HAS_MEMBER = DEFAULT_RELS_EXT_NAMESPACE + "hasMember";
	public static final String FEDORA_REL_DISSEMINATES = "info:fedora/fedora-system:def/view#disseminates";
	public static final String FEDORA_REL_HAS_CONSTITUENT = "info:fedora/fedora-system:def/relations-external#hasConstituent";
	public static final String FEDORA_REL_HAS_MODEL = "info:fedora/fedora-system:def/model#hasModel";
	//Default RELS-EXT namespace Array
	public static final String[] DIPP_DEFAULT_OBJECT_2_OBJECT_RELS = new String[] {
		DEFAULT_RELS_EXT_NAMESPACE, DIPP_HAS_CONSTITUENT, DIPP_HAS_COLLECTION_MEMBER, 
		DIPP_HAS_SUBSET, DIPP_HAS_MEMBER
	};
	
	/**
	 * miscellaneous 
	 */
	public static final String DEFAULT_DIPP_LICENCE_URI = "http://www.dipp.nrw.de/lizenzen/dppl/";
	public static final String FEDORA_SYSTEM_NAMESPACE = "info:fedora/fedora-system";
	public static final String FEDORA_OBJECT_3_0 = FEDORA_SYSTEM_NAMESPACE + ":" + "FedoraObject-3.0";
	public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
	public static final String DIPP_NS = "http://www.dipp.nrw.de/";
	public static final String DEFAULT_NS = "default.namespace";
	public static final String FEDORA_DEF_DATASTREAM = "info:fedora/fedora-system:def/foxml#datastream";
	
	public static final String REM_DEFAULT_BASE_PROPERTY_URI = "rem.baseURI";
	public static final String AGG_DEFAULT_BASE_PROPERTY_URI = "agg.baseURI";
	public static final String AR_DEFAULT_BASE_PROPERTY_URI = "ar.baseURI";
	
	public static final String[] DEFAULT_BASE_URI_PROPERTY_KEYS = new String[]{
		REM_DEFAULT_BASE_PROPERTY_URI, AGG_DEFAULT_BASE_PROPERTY_URI, AR_DEFAULT_BASE_PROPERTY_URI
	};
	
	/**
	 * Metadata Datastreams
	 */
	public static final String DIPP_METADATAID_DIPPEXT = "DiPPExt";
	public static final String DIPP_METADATAID_RELS_EXT= "RELS-EXT";
	public static final String DIPP_METADATAID_QDC = "QDC";
	public static final String DIPP_METADATAID_DC = "DC";
	public static final String DIPP_METADATAID_OAI_EPICUR= "oai_epicur";
	public static final String DIPP_METADATAID_DIPPADM= "DiPPAdm";
	public static final String DIPP_METADATAID_OAI_DC= "oai_dc";
	public static final String DIPP_METADATAID_OAI_DOAI = "oai_doaj";
	
	public static final String[] DIPP_METADATA_IDS = new String[]{
		DIPP_METADATAID_DIPPEXT, DIPP_METADATAID_RELS_EXT, DIPP_METADATAID_QDC,
		DIPP_METADATAID_DC, DIPP_METADATAID_OAI_EPICUR, DIPP_METADATAID_OAI_EPICUR, 
		DIPP_METADATAID_DIPPADM, DIPP_METADATAID_OAI_DC, DIPP_METADATAID_OAI_DOAI};
}
