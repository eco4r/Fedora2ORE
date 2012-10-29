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
 * <b>Class Name</b>: MimeTypeVocabulary</br>
 * <b>Class Definition</b>:
 * <p>MIMEtypes</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */

public class MimeTypeVocabulary {

	// DIPP_MYMETYPES
	public static final String MIMETYPE_TEXT_RTF   = "text/rtf";
	public static final String MIMETYPE_TEXT_XML   = "text/xml";
	public static final String MIMETYPE_TEXT_HTML  = "text/html";
	public static final String MIMETYPE_TEXT_PLAIN = "text/plain";
	
	public static final String MIMETYPE_APPLICATION_RDF_XML      = "application/rdf+xml";
	public static final String MIMETYPE_APPLICATION_X_PDF 	     = "application/x-pdf";
	public static final String MIMETYPE_APPLICATION_PDF 	     = "application/pdf";
	public static final String MIMETYPE_APPLICATION_OCTETSTREAM  = "application/octetstream";
	public static final String MIMETYPE_APPLICATION_MSWORDS		 = "application/msword";
	public static final String MIMETYPE_APPLICATION_POSTSCRIPT 	 = "application/postscript";
	public static final String MIMETYPE_APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String MIMETYPE_APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
	public static final String MIMETYPE_APPLICATION_X_UNKNOWN_APPLICATION_OCTET_STREAM = "application/x-unknown-application-octet-stream";
	public static final String MIMETYPE_APPLICATION_RTF          = "application/rtf";
	public static final String MIMETYPE_APPLICATION_ZIP          = "application/zip";
	public static final String MIMETYPE_APPLICATION_FORCE_DOWNLOAD = "application/force-download";
	public static final String MIMETYPE_APPLICATION_X_SHOCKWAVE_FLASH = "application/x-shockwave-flash";
	public static final String MIMETYPE_APPLICATION_VND_OASIS_OPENDOCUMENT_TEX = "application/vnd.oasis.opendocument.text";
	
	public static final String MIMETYPE_IMAGE_PNG = "image/png";
	public static final String MIMETYPE_IMAGE_JPEG = "image/jpeg";
	public static final String MIMETYPE_IMAGE_GIF = "image/gif";
	public static final String MIMETYPE_IMAGE_SVG_XML = "image/svg xml";
	public static final String MIMETYPE_IMAGE_PJPEG = "image/pjpeg";
	public static final String MIMETYPE_IMAGE_TIFF = "image/tiff";
	
	public static final String MIMETYPE_AUDIO_MPEG = "audio/mpeg";
	public static final String MIMETYPE_X_MSVIDEO = "video/x-msvideo";
	public static final String MIMETYPE_UNKNOWN = "unknown";
	
	public static final String[] TEXT_MIMETYPES = new String[]{MIMETYPE_TEXT_RTF, MIMETYPE_TEXT_XML, 
		MIMETYPE_TEXT_HTML, MIMETYPE_TEXT_PLAIN};
	
	public static final String[] APPLICATION_MIMETYPES = new String[]{MIMETYPE_APPLICATION_RDF_XML, 
		MIMETYPE_APPLICATION_X_PDF, MIMETYPE_APPLICATION_PDF, MIMETYPE_APPLICATION_OCTETSTREAM,
		MIMETYPE_APPLICATION_MSWORDS, MIMETYPE_APPLICATION_POSTSCRIPT, MIMETYPE_APPLICATION_OCTET_STREAM,
		MIMETYPE_APPLICATION_VND_MS_EXCEL, MIMETYPE_APPLICATION_X_UNKNOWN_APPLICATION_OCTET_STREAM, 
		MIMETYPE_APPLICATION_RTF, MIMETYPE_APPLICATION_ZIP, MIMETYPE_APPLICATION_FORCE_DOWNLOAD,
		MIMETYPE_APPLICATION_X_SHOCKWAVE_FLASH, MIMETYPE_APPLICATION_VND_OASIS_OPENDOCUMENT_TEX};
	
	public static final String[] IMAGE_MIMETYPES = new String[]{MIMETYPE_IMAGE_PNG, MIMETYPE_IMAGE_JPEG, 
		MIMETYPE_IMAGE_GIF, MIMETYPE_IMAGE_SVG_XML, MIMETYPE_IMAGE_PJPEG, MIMETYPE_IMAGE_TIFF};
	
	public static final String[] AUDIO_MIMETYPES = new String[]{MIMETYPE_AUDIO_MPEG};
	public static final String[] VIDEO_MIMETYPES = new String[]{MIMETYPE_X_MSVIDEO, MIMETYPE_APPLICATION_X_SHOCKWAVE_FLASH};
	public static final String[] UNKNOWN_MIMETYPES = new String[]{MIMETYPE_UNKNOWN};
	public static final String[] DATABASE_MIMETYPES = new String[]{MIMETYPE_APPLICATION_VND_MS_EXCEL};
	public static final String[] COMPRESSED_MIMETYPES = new String[]{MIMETYPE_APPLICATION_ZIP};
	
	
	public static final String FEDORA_MIMETYPE_NAMESPACE_URI = "info:fedora/fedora-system:def/foxml#datastream/MIMEType";
	
	
}
