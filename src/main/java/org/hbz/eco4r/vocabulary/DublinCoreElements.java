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
 * <b>Class Name</b>: DublinCoreElements</br>
 * <b>Class Definition</b>:
 * <p>Dublin Core Metadata Element Set, Version 1.1</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */


public class DublinCoreElements {

	public static final String DC_PREFIX = "DC";
	public static final String DC_11_ELEMENTS_NS = "http://purl.org/dc/elements/1.1/";
	
	public static final String  DC_11_CONTRIBUTOR = DC_11_ELEMENTS_NS + "contributor";
	public static final String  DC_11_COVERAGE = DC_11_ELEMENTS_NS + "coverage";
	public static final String  DC_11_CREATOR = DC_11_ELEMENTS_NS + "creator";
	public static final String  DC_11_DATE = DC_11_ELEMENTS_NS + "date";
	public static final String  DC_11_DESCRIPTION = DC_11_ELEMENTS_NS + "description";
	public static final String  DC_11_FORMAT = DC_11_ELEMENTS_NS + "format";
	public static final String  DC_11_IDENTIFIER = DC_11_ELEMENTS_NS + "identifier";
	public static final String  DC_11_LANGUAGE = DC_11_ELEMENTS_NS + "language";
	public static final String  DC_11_PUBLISHER = DC_11_ELEMENTS_NS + "publisher";
	public static final String  DC_11_RELATION = DC_11_ELEMENTS_NS + "relation";
	public static final String  DC_11_RIGHTS = DC_11_ELEMENTS_NS + "rights";
	public static final String  DC_11_SOURCE = DC_11_ELEMENTS_NS + "source";
	public static final String  DC_11_SUBJECT = DC_11_ELEMENTS_NS + "subject";
	public static final String  DC_11_TITLE = DC_11_ELEMENTS_NS + "title";
	public static final String  DC_11_TYPE = DC_11_ELEMENTS_NS + "type";
	
	public static final String[] DC_11_ELEMENTS = new String[]{
		DC_11_CONTRIBUTOR, DC_11_COVERAGE, DC_11_CREATOR,
		DC_11_DATE, DC_11_DESCRIPTION, DC_11_FORMAT, 
		DC_11_IDENTIFIER, DC_11_LANGUAGE, DC_11_PUBLISHER,
		DC_11_RELATION, DC_11_RIGHTS, DC_11_SOURCE, 
		DC_11_SUBJECT, DC_11_TITLE, DC_11_TYPE
	};
	
}
