package org.hbz.eco4r.postprocessing;

import static org.hbz.eco4r.vocabulary.MiscVocabulary.RDF_XML;
import static org.hbz.eco4r.vocabulary.MiscVocabulary.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.log4j.Logger;
import org.dspace.foresite.OREException;
import org.dspace.foresite.OREFactory;
import org.dspace.foresite.ORESerialiser;
import org.dspace.foresite.ORESerialiserException;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;
import org.dspace.foresite.Triple;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class TestProcessingNode1 implements ProcessingNode {

	private static Logger logger = Logger.getLogger(TestProcessingNode1.class);
	
	private ResourceMap rem;
	private Model model;
	
	
	@Override
	public ResourceMap run(ResourceMap rem) {
		logger.info(" ------------------------------------------------ ");
		logger.info("Performing Processing Node: " + TestProcessingNode1.class);
		
		this.rem = rem;
		
		try {
			logger.info("Resource-Map URI:   " + this.rem.getURI());
			
			Model model = this.initModel(this.rem);
			
			StmtIterator iter = model.listStatements();
			
			int i = 0;
			
			while (iter.hasNext()) {
				iter.nextStatement();
				i++;
			}
			
			logger.info("Number of Statements: " + i);
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
		
		logger.info("Processing Node: " + TestProcessingNode1.class + " finished!");
		logger.info(" ------------------------------------------------ ");
		
		this.reInitModel();
		
		return rem;
	}
	
	private Model initModel(ResourceMap rem) {
		Model model = null;
		try {
			ORESerialiser serialiser = ORESerialiserFactory
					.getInstance(RDF_XML);
			ResourceMapDocument remDoc = serialiser.serialise(rem);
			String remString = remDoc.toString();

			InputStream is = new ByteArrayInputStream(remString.getBytes(UTF_8));
			this.model = ModelFactory.createDefaultModel();
			model = this.model.read(is, null);
			
		} catch (ORESerialiserException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return model;
	}
	
	public void removeTriple(URI res1, URI res2, URI res3) {
		try {
			Triple tr = OREFactory.createTriple(res1, res2, res3);
			this.rem.getAggregation().removeTriple(tr);
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
	}
	
	public void removeTriple(URI res1, URI res2, String res3) {
		try {
			Triple tr = OREFactory.createTriple(res1, res2, res3);
			this.rem.getAggregation().removeTriple(tr);
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
	}
	
	public void addTriple(URI res1, URI res2, URI res3) {
		try {
			Triple tr = OREFactory.createTriple(res1, res2, res3);
			this.rem.getAggregation().addTriple(tr);
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void addTriple(URI res1, URI res2, String str) {
		try {
			Triple tr = OREFactory.createTriple(res1, res2, str);
			this.rem.getAggregation().addTriple(tr);
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
	}
	
	
	public void reInitModel() {
		this.model.close();
		this.initModel(this.rem);
	}
}
