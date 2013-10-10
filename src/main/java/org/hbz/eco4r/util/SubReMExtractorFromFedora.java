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

import static org.hbz.eco4r.vocabulary.MiscVocabulary.RDF_XML;
import static org.hbz.eco4r.vocabulary.MiscVocabulary.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.dspace.foresite.Aggregation;
import org.dspace.foresite.OREException;
import org.dspace.foresite.OREFactory;
import org.dspace.foresite.ORESerialiser;
import org.dspace.foresite.ORESerialiserException;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <b>Class Name</b>: SubReMExtractorFromFedora</br>
 * <b>Class Definition</b>:
 * <p>This class is about extracting subgraphs of a Resource-Map.</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */

public class SubReMExtractorFromFedora {

	private ResourceMap rem;
	private Model model;
	private Aggregation newAgg;
	private ResourceMap newRem;
	private List<URI> passed;
	
	
	public SubReMExtractorFromFedora(ResourceMap rem, URI aggURI, URI remURI,  List<URI> passed) {
		this.rem = rem;
		this.passed = passed;
		try {
			newAgg = OREFactory.createAggregation(aggURI);
			newRem = newAgg.createResourceMap(remURI);
			
			model = this.initModel();
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
	}
	
	public void traverse(URI uri) {
		
		if (!this.passed.contains(uri)) {
			List<URI> nextURIs = this.expandURI(uri);
			this.passed.add(uri);
			
			for (URI nextURI : nextURIs) {
				this.traverse(nextURI);
			}
		}
	}
	
	
	
	private List<URI> expandURI(URI uri) {
		List<URI> uris = new ArrayList<URI>();
		
		StmtIterator stmtIter = this.model.listStatements();
		
		try {
			while (stmtIter.hasNext()) {
				Statement stmt = stmtIter.nextStatement();
				Resource subj = stmt.getSubject();
				Property pred = stmt.getPredicate();
				RDFNode obj = stmt.getObject();
				
				if (!subj.isAnon()) {
					URI subjURI = new URI(subj.getURI());
					
					URI predURI = new URI(pred.getURI());
					if (subj.getURI().compareTo(uri.toString()) == 0) {
						if (!obj.isAnon()) {
							if (obj.isResource()) {
								URI objURI = new URI(obj.asResource().getURI());
								uris.add(objURI);
								org.dspace.foresite.Triple tr = OREFactory.createTriple(subjURI, predURI, objURI);
								this.newAgg.addTriple(tr);
							}
							if (obj.isLiteral()) {
								String objStr = obj.asLiteral().getString();
								org.dspace.foresite.Triple tr = OREFactory.createTriple(subjURI, predURI, objStr);
								this.newAgg.addTriple(tr);
							}
						}
					}
				}
			}
		} 
		catch (URISyntaxException e) {
			e.printStackTrace();
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
		
		return uris;
	}

	private Model initModel() {
		Model model = null;
		try {
			ORESerialiser serialiser = ORESerialiserFactory
					.getInstance(RDF_XML);
			ResourceMapDocument remDoc = serialiser.serialise(this.rem);
			String remString = remDoc.toString();

			InputStream is = new ByteArrayInputStream(remString.getBytes(UTF_8));
			model = ModelFactory.createDefaultModel();
			model.read(is, null);
		} 
		catch (ORESerialiserException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return model;
	}

	public ResourceMap getRem() {
		return rem;
	}

	public void setRem(ResourceMap rem) {
		this.rem = rem;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Aggregation getNewAgg() {
		return newAgg;
	}

	public void setNewAgg(Aggregation newAgg) {
		this.newAgg = newAgg;
	}

	public ResourceMap getNewRem() {
		return newRem;
	}

	public void setNewRem(ResourceMap newRem) {
		this.newRem = newRem;
	}

	public List<URI> getPassed() {
		return passed;
	}

	public void setPassed(List<URI> passed) {
		this.passed = passed;
	}
}
