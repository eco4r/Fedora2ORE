package org.hbz.eco4r.main;

/**
 * <b>Package Name: org.hbz.eco4r.main</b>
 * <b>Package Description: </b>
 * <p>This package is about the generation of OAI-ORE ResourceMaps. 
 * It contains higher level classes that are responsible for the invocation of generation methods.</p>
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dspace.foresite.Aggregation;
import org.dspace.foresite.OREException;
import org.dspace.foresite.OREFactory;
import org.dspace.foresite.ORESerialiser;
import org.dspace.foresite.ORESerialiserException;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;
import org.hbz.eco4r.config.Configuration;
import org.hbz.eco4r.connection.FedoraConnector;
import org.hbz.eco4r.postprocessing.ProcessingNode;
import org.hbz.eco4r.postprocessing.ProcessingQueue;
import org.hbz.eco4r.util.FedoraUtils;
import org.hbz.eco4r.util.MetadataUtils;
import org.hbz.eco4r.util.PropertyUtils;
import org.hbz.eco4r.util.RelationshipUtils;
import org.junit.Assert;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * <b>Class Name</b>: ResourceMapGenerator</br>
 * <b>Class Definition</b>:
 * <p>This is a higher class for generating OAI-ORE ResourceMaps.</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */

public class ResourceMapGenerator {

	private static Logger logger = Logger.getLogger(ResourceMapGenerator.class);

	private Aggregation agg;
	private ResourceMap rem;
	private String propertiesPath;
	private FedoraConnector fedoraConnector;
	private FedoraUtils fedoraUtils;
	private PropertyUtils propertyUtils;
	private Map<String, List<String>> propertiesMap;
	private MetadataUtils metadataUtils;
	private Model model;
	private List<ProcessingNode> processingNodes;

	private RelationshipUtils relationshipUtils;

	public ResourceMapGenerator(String propertiesPath) {
		this.setPropertiesPath(propertiesPath);
		this.setFedoraConnector(new FedoraConnector(this.propertiesPath));
		this.initProcessingNodes(this.fedoraConnector.getConfiguration());
		this.setFedoraUtils(new FedoraUtils(this.fedoraConnector));
		this.setPropertyUtils(new PropertyUtils(this.fedoraConnector));
		this.setPropertiesMap(this.propertyUtils.getPropertiesMap());
		this.setMetadataUtils(new MetadataUtils(this.fedoraConnector, this.fedoraUtils, this.propertyUtils));
		this.setMetadataUtils_TEST(new MetadataUtils(this.fedoraConnector, this.fedoraUtils, this.propertyUtils));
		this.setRelationshipUtils(new RelationshipUtils(this.fedoraConnector, this.fedoraUtils, 
				this.propertyUtils, this.metadataUtils));
	}

	public ResourceMap generateResourceMap(String pid){
		
		logger.info("Processing object with pid: " + pid);
		
		logger.info("--------  Initialization  --------");
		this.agg = this.instantiateAggregation(pid);
		ResourceMap rem = this.instantiateResourceMap(pid);
		
		
		logger.info("-------- Setting Up the Relationships ---------");
		this.relationshipUtils.setUpReMRelationships(rem);
		
		logger.info("-------- Setting Up the Metadata ---------");
		try {
			this.metadataUtils.propagateMetadata(rem, this.metadataUtils.getMetadataConfig());
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
		
		this.setRem(rem);
		this.setAgg(agg);
		
		logger.info("-------- Post Processing ---------");
		this.setModel(this.initModel());
		if (this.processingNodes != null && this.processingNodes.size() > 0) {
			
			ProcessingQueue pQueue = new ProcessingQueue(rem);
			
			if (this.processingNodes != null && this.processingNodes.size() > 0) {
				for (ProcessingNode node : this.processingNodes) {
					pQueue.addProcessingNode(node);
				}
			}
			
			ResourceMap newReM = pQueue.doProcess();
			
			return newReM;
		}
		else {
			return rem;
		}
	}
	
	public ResourceMap instantiateResourceMap(String pid){
		ResourceMap rem = null;
		
		try {
//			The following 2 code lines are under consideration 
//			List<String> baseURIs = this.propertiesMap.get("rem.metadata.baseURI");
//			String uri = baseURIs.get(0).trim() + pid;
			
			String uri = this.agg.getURI().toString() + "/rem.rdf";
			
//			uri = this.replacePort(uri, "8081");
			
			rem = this.agg.createResourceMap(new URI(uri));
			this.metadataUtils.setResourceMapMetadata(rem);
		} 
		catch (OREException e) {
			e.printStackTrace();
		} 
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull("The resource map object is null." + rem);
		return rem;
	}

	public Aggregation instantiateAggregation(String pid){
		Aggregation agg = null;
		try {
			URI aggURI = new URI(this.fedoraConnector.getBaseURL() + "/objects/" + pid);
			agg = OREFactory.createAggregation(aggURI);
		} catch (OREException e) {
			e.printStackTrace();
		} 
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull("The aggregation object is null." + agg);
		return agg;
	}
	
//	private boolean isArticleAgg(Aggregation aggregation) {
//		boolean isArticle = false;
//		boolean dsTermsTypeIsArticle = this.checkDCTermsTypeIsArticle();
//
//		if (dsTermsTypeIsArticle == true) {
//			isArticle = true;
//		} else {
//			boolean isHasModelArticle = this.checkHasModelIsArticle();
//			if (isHasModelArticle == true) {
//				isArticle = true;
//			}
//		}
//
//		return isArticle;
//	}
	
//	private boolean checkDCTermsTypeIsArticle() {
//		boolean dsTermsIsArticle = false;
//
//		try {
//			Resource aggRes = this.model.getResource(this.rem.getAggregation()
//					.getURI().toString());
//			Property prop = this.model.getProperty(DCMI_TERMS_TYPE);
//			NodeIterator iter = this.model.listObjectsOfProperty(aggRes, prop);
//			while (iter.hasNext()) {
//				RDFNode node = iter.nextNode();
//				String nodeStr = node.toString();
//				if (nodeStr.contains("article")) {
//					dsTermsIsArticle = true;
//					break;
//				}
//			}
//		} catch (OREException e) {
//			e.printStackTrace();
//		}
//
//		return dsTermsIsArticle;
//	}
	
//	private boolean checkHasModelIsArticle() {
//		boolean hasModelIsArticle = false;
//		try {
//			Resource aggRes = this.model.getResource(this.rem.getAggregation()
//					.getURI().toString());
//			Property prop = this.model
//					.getProperty("info:fedora/fedora-system:def/model#hasModel");
//
//			NodeIterator iter = this.model.listObjectsOfProperty(aggRes, prop);
//
//			while (iter.hasNext()) {
//				RDFNode node = iter.nextNode();
//				String nodeStr = node.toString();
//				if (nodeStr.contains("DiPP:article")) {
//					hasModelIsArticle = true;
//					break;
//				}
//			}
//		} catch (OREException e) {
//			e.printStackTrace();
//		}
//
//		return hasModelIsArticle;
//	}
	
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
	
	private void initProcessingNodes(Configuration configuration) {
		this.processingNodes = new ArrayList<ProcessingNode>();
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		List<org.hbz.eco4r.config.Property> props = configuration.getProperties();
		for (org.hbz.eco4r.config.Property prop : props) {
			String key = prop.getKey();
			if (key.startsWith("postProcessing")) {
				int order = Integer.valueOf(key.split("\\.")[2]);
				if (!map.containsKey(order)) {
					map.put(order, prop.getValues().get(0).trim());
				}
			}
		}
		
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			String classStr = entry.getValue();
			
			@SuppressWarnings("rawtypes")
			Class dateAcceptedClass;
			try {
				dateAcceptedClass = Class.forName(classStr);
				@SuppressWarnings("rawtypes")
				Constructor constructor = dateAcceptedClass.getConstructors()[0];
				Object obj = constructor.newInstance();
				ProcessingNode node = (ProcessingNode) obj;
				
				if (!this.processingNodes.contains(node)) {
					this.processingNodes.add(node);
				}
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			} 
			catch (InstantiationException e) {
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public Aggregation getAgg() {
		return agg;
	}

	public void setAgg(Aggregation agg) {
		this.agg = agg;
	}
	
	public String getPropertiesPath() {
		return propertiesPath;
	}
	
	public void setPropertiesPath(String propertiesPath) {
		this.propertiesPath = propertiesPath;
	}

	public FedoraConnector getFedoraConnector() {
		return fedoraConnector;
	}

	public void setFedoraConnector(FedoraConnector fedoraConnector) {
		this.fedoraConnector = fedoraConnector;
	}

	public FedoraUtils getFedoraUtils() {
		return fedoraUtils;
	}

	public void setFedoraUtils(FedoraUtils fedoraUtils) {
		this.fedoraUtils = fedoraUtils;
	}
	
	public Map<String, List<String>> getPropertiesMap() {
		return propertiesMap;
	}

	public void setPropertiesMap(Map<String, List<String>> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}
	
	public PropertyUtils getPropertyUtils() {
		return propertyUtils;
	}

	public void setPropertyUtils(PropertyUtils propertyUtils) {
		this.propertyUtils = propertyUtils;
	}
	
	public MetadataUtils getMetadataUtils_TEST() {
		return metadataUtils;
	}

	public void setMetadataUtils_TEST(MetadataUtils metadataUtils_TEST) {
		this.metadataUtils = metadataUtils_TEST;
	}
	
	public RelationshipUtils getRelationshipUtils() {
		return relationshipUtils;
	}

	public void setRelationshipUtils(RelationshipUtils relationshipUtils) {
		this.relationshipUtils = relationshipUtils;
	}
	
	public MetadataUtils getMetadataUtils() {
		return metadataUtils;
	}

	public void setMetadataUtils(MetadataUtils metadataUtils) {
		this.metadataUtils = metadataUtils;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ResourceMap getRem() {
		return rem;
	}

	public void setRem(ResourceMap rem) {
		this.rem = rem;
	}

	public List<ProcessingNode> getProcessingNodes() {
		return processingNodes;
	}

	public void setProcessingNodes(List<ProcessingNode> processingNodes) {
		this.processingNodes = processingNodes;
	}
}
