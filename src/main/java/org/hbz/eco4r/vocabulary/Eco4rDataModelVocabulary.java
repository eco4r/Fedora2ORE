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
 * <b>Class Name</b>: Eco4rDataModelVocabulary</br>
 * <b>Class Definition</b>:
 * <p>Defines the Entities of the eco4r data model</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */
public class Eco4rDataModelVocabulary {

	// -----------------
	// Fabio Vocabular
	// -----------------
	
	public static final String FABIO_JOURNAL_ARTICLE = "http://purl.org/spar/fabio/JournalArticle";
	public static final String FABIO_EXPRESSION = "http://purl.org/spar/fabio/Expression";
	public static final String FABIO_DIGITAL_MANIFESTATION = "http://purl.org/spar/fabio/DigitalManifestation";
	public static final String FABIO_SUPPLEMENT = "http://purl.org/spar/fabio/SupplementaryInformationFile";
	public static final String FABIO_DIGITAL_ITEM = "http://purl.org/spar/fabio/DigitalItem";
	public static final String FABIO_METADATA = "http://purl.org/spar/fabio/Metadata";
	public static final String FABIO_WEB_PAGE = "http://purl.org/spar/fabio/WebPage";
	public static final String FABIO_IMAGE = "http://purl.org/spar/fabio/Image";
	public static final String FABIO_TABLE = "http://purl.org/spar/fabio/Table";
	public static final String COMPRESSED = FABIO_DIGITAL_MANIFESTATION + "/Compressed";
	public static final String FABIO_MOVING_IMAGE = "http://purl.org/spar/fabio/MovingImage";
	public static final String FABIO_BIBLIOGRAPHIC_METADATA = "http://purl.org/spar/fabio/BibliographicMetadata"; 
	
	public static final String FABIO_HAS_MANIFESTATION = "http://purl.org/spar/fabio/hasManifestation";
	public static final String FABIO_IS_MANIFESTATION_OF = "http://purl.org/spar/fabio/isManifestationOf";
	public static final String FABIO_HAS_EXPEMPLAR = "http://purl.org/vocab/frbr/core#exemplar";
	public static final String FABIO_IS_EXEMPLAR_OF = "http://purl.org/vocab/frbr/core#exemplarOf";
	public static final String FABIO_HAS_REPRESENTATION = "http://purl.org/spar/fabio/hasRepresentation";
	public static final String FABIO_IS_REPRESENTATION_OF = "http://purl.org/spar/fabio/isRepresentationOf";
	
	
	/**
	 * OAI-ORE Vocabulary
	 */
	public static final String ORE_AGGREGATION = "http://www.openarchives.org/ore/terms/Aggregation";
	public static final String ORE_AGGREGATED_RESSOURCE = "http://www.openarchives.org/ore/terms/AggregatedResource";
	
	public static final String ORE_AGGREGATES = "http://www.openarchives.org/ore/terms/aggregates";
	public static final String ORE_IS_AGGREGATED_BY = "http://www.openarchives.org/ore/terms/isAggregatedBy";
	
	public static final String ORE_DESCRIBES = "http://www.openarchives.org/ore/terms/describes";
	public static final String ORE_IS_DESCRIBED_BY = "http://www.openarchives.org/ore/terms/isDescribedBy";
	
	
	// -----------------
	// Other Vocabular
	// -----------------
	public static final String RDFS_LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
	public static final String HUMAN_STARTPAGE = "info:eu-repo/semantics/humanStartPage";
	public static final String DIPP_MIMETYPE = "info:fedora/fedora-system:def/foxml#datastream/MIMEType";
	public static final String SCHOLARLY_TEXT = "http://purl.org/eprint/type/ScholarlyText";
	public static final String HAS_PART = "http://purl.org/dc/terms/hasPart";
	public static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	public static final String RDFS_COMMENT = "http://www.w3.org/2000/01/rdf-schema#comment";
}
