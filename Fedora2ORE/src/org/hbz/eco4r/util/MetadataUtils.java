package org.hbz.eco4r.util;

import static org.hbz.eco4r.vocabulary.DublinCoreElements.*;
import static org.hbz.eco4r.vocabulary.FedoraVocabulary.FEDORA_DEF_DATASTREAM;
import static org.hbz.eco4r.vocabulary.FedoraVocabulary.FEDORA_REL_DISSEMINATES;
import static org.hbz.eco4r.vocabulary.MiscVocabulary.RDF_XML;
import static org.hbz.eco4r.vocabulary.MiscVocabulary.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.dspace.foresite.Agent;
import org.dspace.foresite.Aggregation;
import org.dspace.foresite.OREException;
import org.dspace.foresite.OREFactory;
import org.dspace.foresite.ORESerialiser;
import org.dspace.foresite.ORESerialiserException;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.Predicate;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;
import org.dspace.foresite.Triple;
import org.hbz.eco4r.connection.FedoraConnector;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fedora.server.access.FedoraAPIA;
import fedora.server.management.FedoraAPIM;
import fedora.server.types.gen.Datastream;
import fedora.server.types.gen.DatastreamDef;
import fedora.server.types.gen.MIMETypedStream;
import fedora.server.types.gen.ObjectFields;

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

/**
 * <b>Class Name</b>: MetadataUtils</br>
 * <b>Class Definition</b>:
 * <p>Util class for handling metadata.</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */
public class MetadataUtils {

private static Logger logger = Logger.getLogger(MetadataUtils.class);
	
	private FedoraConnector fedoraConnector;
	private Map<String, List<String>> propertiesMap;
	private FedoraUtils fedoraUtils;
	private PropertyUtils propertyUtils;
	private MetadataConfiguration metadataConfig;
	private Model model;
	private ResourceMap rem;
	

	public MetadataUtils(FedoraConnector fedoraConnector, 
			FedoraUtils fedoraUtils, PropertyUtils propertyUtils) {
		this.setFedoraConnector(fedoraConnector);
		this.setPropertiesMap(propertyUtils.getPropertiesMap());
		this.setFedoraUtils(fedoraUtils);
		this.setPropertyUtils(propertyUtils);
		this.setMetadataConfig(new MetadataConfiguration(this.fedoraConnector));
	}
	
	public ResourceMap setResourceMapMetadata(ResourceMap rem) {

		try {
			Assert.assertTrue(
					"Properties map does not contain the key: rem.metadata.creator.URI",
					propertiesMap.containsKey("rem.metadata.creator.URI"));
			Assert.assertTrue(
					"Properties map does not contain the key: rem.metadata.creator.name",
					propertiesMap.containsKey("rem.metadata.creator.name"));
			Assert.assertTrue(
					"Properties map does not contain the key: rem.metadata.creator.mbox",
					propertiesMap.containsKey("rem.metadata.creator.mbox"));

			List<String> uris = this.propertiesMap
					.get("rem.metadata.creator.URI");
			Agent agent = OREFactory.createAgent(new URI(uris.get(0)));

			if (this.propertiesMap.containsKey("rem.metadata.creator.name")) {
				List<String> values = this.propertiesMap
						.get("rem.metadata.creator.name");
				if (values != null && values.size() > 0) {
					agent.setNames(values);
				}
			}
			if (this.propertiesMap.containsKey("rem.metadata.rights")) {
				List<String> values = this.propertiesMap.get("rem.metadata.rights");
				if (values != null && values.size() > 0) {
					for (String value : values) {
						Triple tr = OREFactory.createTriple(rem.getURI(), new URI(DC_11_RIGHTS), value);
						rem.addTriple(tr);
					}
				}
			}
			if (this.propertiesMap.containsKey("rem.metadata.creator.mbox")) {
				List<String> values = this.propertiesMap
						.get("rem.metadata.creator.mbox");
				if (values != null && values.size() > 0) {
					for (String value : values) {
						Triple triple = OREFactory.createTriple(agent,
								new Predicate(new URI(
										"http://xmlns.com/foaf/0.1/mbox")),
								value.trim());
						agent.addTriple(triple);
					}
				}
			}
			rem.addCreator(agent);

		} catch (OREException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return rem;
	}
	
	public Aggregation propagateMetadata(ResourceMap rem, 
				MetadataConfiguration metadataConfig) throws OREException{
		logger.info("Performing metadata propagation ...");
		this.setRem(rem);
		this.initModel();
		this.setResourceMapMetadata(rem);
		
		List<URI> passed = new ArrayList<URI>();
		
		StmtIterator stmtIter = this.model.listStatements();
		
		while (stmtIter.hasNext()) {
			Statement stmt = stmtIter.nextStatement();
			Resource subj = stmt.getSubject();
			RDFNode obj = stmt.getObject();
		
			try {
				if (!subj.isAnon()) {
					URI subjURI = new URI(subj.getURI());
					if (!passed.contains(subjURI)) {
						this.setMetadataToResource(new URI(subj.getURI()), metadataConfig);
						passed.add(subjURI);
					}
				}	
				if (!obj.isAnon()) {
					if (obj.isResource()) {
						URI objURI = new URI(obj.asResource().getURI());
						if (!passed.contains(objURI)) {
							this.setMetadataToResource(objURI, metadataConfig);
							passed.add(objURI);
						}
					}
				}
			} 
			catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		return this.rem.getAggregation();
	}
	
	private void setMetadataToResource(URI uri, MetadataConfiguration metadataConfig) {
		try {
			if (this.isDatastreamURI(uri) && this.isSetDSMetatada()) {
				this.setMetadataToDatastream(uri);
			}
			else {
				String pid = this.getLast(uri.toString(), "/");
				if (this.objectExists(pid)) {
					if (this.rem.getAggregation().getURI().equals(uri)) {
						this.setAggregationMetadata(uri, metadataConfig);
					}		
					else {
						if (uri.toString().compareTo(this.rem.getURI().toString()) != 0) {
							this.setMetadata(uri, metadataConfig);
						}
					}
				}
			}
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
	}
	
	private void setMetadata(URI uri, MetadataConfiguration metadataConfig) {
		String pid = this.getLast(uri.toString(), "/");
		if (objectExists(pid)) {
			ObjectFields objectFields = this.fedoraUtils.getObjectFields(pid);
			List<MetadataConfigTriple> config = metadataConfig.getMetadataConfig();
			try {
				for (MetadataConfigTriple triple : config) {
					String mdID = triple.getDatastreamID();
					if (mdID.compareTo("DC") == 0) {
						List<String> mdKeys = triple.getMetadataKeys();
						
						for (String mdKey : mdKeys) {
							if (mdKey.trim().compareTo("contributor") == 0) {
								String[] contributors = objectFields.getContributor();
								if (contributors != null) {
									for (String contributor : contributors) {
										if (contributor != null) {
											if (!contributor.isEmpty())
												this.addTriple(uri, new URI(DC_11_CONTRIBUTOR), contributor);
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("creator") == 0) {
								String[] creators = objectFields.getCreator();
								Resource res = this.model.getResource(uri.toString());
								if (creators != null) {
									for (String creator : creators) {
										if (creator != null) {
											if (!creator.isEmpty()) {
												this.addTriple(new URI(res.getURI()), new URI(DC_11_CREATOR), creator);
											}
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("coverage") == 0) {
								String[] coverages = objectFields.getCoverage();
								if (coverages != null) {
									for (String coverage : coverages) {
										if (coverage != null) {
											if (!coverage.isEmpty())
												this.addTriple(uri, new URI(DC_11_COVERAGE), coverage);
										}
										
									}
								}
							}
							
							if (mdKey.trim().compareTo("date") == 0) {
								String dcmDate = objectFields.getDcmDate();
								if (dcmDate != null && !dcmDate.isEmpty()) {
									this.addTriple(uri, new URI(DC_11_DATE), dcmDate);
								}
							}
							
							if (mdKey.trim().compareTo("description") == 0) {
								String[] descriptions = objectFields.getDescription();
								if (descriptions != null) {
									for (String description : descriptions) {
										if (description != null) {
											if (!description.isEmpty())
												this.addTriple(uri, new URI(DC_11_DESCRIPTION), description);
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("format") == 0) {
								String[] formats = objectFields.getFormat();
								if (formats != null) {
									for (String format : formats) {
										if (format != null) {
											if (!format.isEmpty())
												this.addTriple(uri, new URI(DC_11_FORMAT), format);
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("identifier") == 0) {
								String[] identifiers = objectFields.getIdentifier();
								if (identifiers != null) {
									for (String id : identifiers) {
										if (id != null && !id.isEmpty())
											this.addTriple(uri, new URI(DC_11_IDENTIFIER), id);
									}
								}					
							}
							
							if (mdKey.trim().compareTo("language") == 0) {
								String[] languages = objectFields.getLanguage();
								if (languages != null) {
									for (String language : languages) {
										if (language != null) {
											if (!language.isEmpty())
												this.addTriple(uri, new URI(DC_11_LANGUAGE), language);
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("publisher") == 0) {
								String[] publishers = objectFields.getPublisher();
								if (publishers != null) {
									for (String publisher : publishers) {
										if (publisher != null) {
											if (!publisher.isEmpty())
												this.addTriple(uri, new URI(DC_11_PUBLISHER), publisher);
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("relation") == 0) {
								String[] rels = objectFields.getRelation();
								if (rels != null) {
									for (String rel : rels) {
										if (rel != null) {
											if (!rel.isEmpty())
												this.addTriple(uri, new URI(DC_11_RELATION), rel);
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("source") == 0) {
								String[] sources = objectFields.getSource();
								if (sources != null) {
									for (String source : sources) {
										if (source != null) {
											if (!source.isEmpty())
												this.addTriple(uri, new URI(DC_11_SOURCE), source);
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("subject") == 0) {
								String[] subjects = objectFields.getSubject();
								if (subjects != null) {
									for (String subj : subjects) {
										if (subj != null) {
											if (!subj.isEmpty()) {
												if (subj.contains("ddc:")) { 
													if (subj.split(":").length == 2) {
														URI deweURI = new URI("http://dewey.info/class/" + subj.split(":")[1] + "/");
														this.addTriple(uri, new URI(DC_11_SUBJECT), deweURI);
													}
												}
												else {
													this.addTriple(uri, new URI(DC_11_SUBJECT), subj);
												}
											}
										}
									}
								}
							}
							
							if (mdKey.trim().compareTo("title") == 0) {
								String[] titles = objectFields.getTitle();
								if (titles != null) {
									for (String title : titles) {
										if (title != null) {
											if (!title.isEmpty())
												this.addTriple(uri, new URI(DC_11_TITLE), title);
										}
									}
								}					
							}
							
							if (mdKey.trim().compareTo("type") == 0) {
								String[] types = objectFields.getType();
								if (types != null) {
									for (String type : types) {
										if (type != null) {
											if (!type.isEmpty())
												this.addTriple(uri, new URI(DC_11_TYPE), type);
										}
									}
								}
							}
						}
					}
					else {
						
					}
				}
			}
			catch (URISyntaxException e) {
				e.printStackTrace();
			} 
		}
	}

	private void setAggregationMetadata(URI aggURI, MetadataConfiguration metadataConfig) {
		String pid = this.getLast(aggURI.toString(), "/");
		ObjectFields objectFields = this.fedoraUtils.getObjectFields(pid);
		List<MetadataConfigTriple> config = metadataConfig.getMetadataConfig();
		
		try {
			for (MetadataConfigTriple triple : config) {
				String mdID = triple.getDatastreamID();
				if (mdID.compareTo("DC") == 0) {
					List<String> mdKeys = triple.getMetadataKeys();
					
					for (String mdKey : mdKeys) {
						if (mdKey.trim().compareTo("contributor") == 0) {
							String[] contributors = objectFields.getContributor();
							if (contributors != null) {
								for (String contributor : contributors) {
									if (contributor != null) {
										if (!contributor.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_CONTRIBUTOR), contributor);
									}
								}
							}
						}
						
						if (mdKey.trim().compareTo("coverage") == 0) {
							String[] coverages = objectFields.getCoverage();
							if (coverages != null) {
								for (String coverage : coverages) {
									if (coverage != null) {
										if (!coverage.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_COVERAGE), coverage);
									}
									
								}
							}
						}
						
						if (mdKey.trim().compareTo("creator") == 0) {
							String[] creators = objectFields.getCreator();
							if (creators != null) {
								Agent agent = OREFactory.createAgent();
								List<String> names = Arrays.asList(creators);
								agent.setNames(names);
								this.rem.getAggregation().addCreator(agent);
							}
						}
						
						if (mdKey.trim().compareTo("date") == 0) {
							String dcmDate = objectFields.getDcmDate();
							if (dcmDate != null && !dcmDate.isEmpty()) {
								this.addTriple(aggURI, new URI(DC_11_DATE), dcmDate);
							}
						}
						
						if (mdKey.trim().compareTo("description") == 0) {
							String[] descriptions = objectFields.getDescription();
							if (descriptions != null) {
								for (String description : descriptions) {
									if (description != null) {
										if (!description.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_DESCRIPTION), description);
									}
								}
							}
						}
						
						if (mdKey.trim().compareTo("format") == 0) {
							String[] formats = objectFields.getFormat();
							if (formats != null) {
								for (String format : formats) {
									if (format != null) {
										if (!format.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_FORMAT), format);
									}
								}
							}
						}
						
						if (mdKey.trim().compareTo("identifier") == 0) {
							String[] identifiers = objectFields.getIdentifier();
							if (identifiers != null) {
								for (String id : identifiers) {
									if (id != null && !id.isEmpty())
										this.addTriple(aggURI, new URI(DC_11_IDENTIFIER), id);
								}
							}					
						}
						
						if (mdKey.trim().compareTo("language") == 0) {
							String[] languages = objectFields.getLanguage();
							if (languages != null) {
								for (String language : languages) {
									if (language != null) {
										if (!language.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_LANGUAGE), language);
									}
								}
							}
						}
						
						if (mdKey.trim().compareTo("publisher") == 0) {
							String[] publishers = objectFields.getPublisher();
							if (publishers != null) {
								for (String publisher : publishers) {
									if (publisher != null) {
										if (!publisher.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_PUBLISHER), publisher);
									}
								}
							}
						}
						
						if (mdKey.trim().compareTo("relation") == 0) {
							String[] rels = objectFields.getRelation();
							if (rels != null) {
								for (String rel : rels) {
									if (rel != null) {
										if (!rel.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_RELATION), rel);
									}
								}
							}
						}
						
						if (mdKey.trim().compareTo("rights") == 0) {
							String[] rights = objectFields.getRights();
							
							if (rights != null) {
								for (String right : rights) {
									if (right != null) {
										if (!right.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_RIGHTS), right);
									}
								}
							}
						}
						
						
						if (mdKey.trim().compareTo("source") == 0) {
							String[] sources = objectFields.getSource();
							if (sources != null) {
								for (String source : sources) {
									if (source != null) {
										if (!source.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_SOURCE), source);
									}
								}
							}
						}
						
						if (mdKey.trim().compareTo("subject") == 0) {
							String[] subjects = objectFields.getSubject();
							if (subjects != null) {
								for (String subj : subjects) {
									if (subj != null) {
										if (!subj.isEmpty()) {
											if (subj.contains("ddc:")) { 
												if (subj.split(":").length == 2) {
													URI deweURI = new URI("http://dewey.info/class/" + subj.split(":")[1] + "/");
													this.addTriple(aggURI, new URI(DC_11_SUBJECT), deweURI);
												}
											}
											else {
												this.addTriple(aggURI, new URI(DC_11_SUBJECT), subj);
											}
										}
									}
								}
							}
						}
						
						if (mdKey.trim().compareTo("title") == 0) {
							String[] titles = objectFields.getTitle();
							if (titles != null) {
								for (String title : titles) {
									if (title != null) {
										if (!title.isEmpty())
											this.rem.getAggregation().addTitle(title);
									}
								}
							}					
						}
						
						if (mdKey.trim().compareTo("type") == 0) {
							String[] types = objectFields.getType();
							if (types != null) {
								for (String type : types) {
									if (type != null) {
										if (!type.isEmpty())
											this.addTriple(aggURI, new URI(DC_11_TYPE), type);
									}
								}
							}
						}
					}
				}
				else {
					String mdNamespace = triple.getDatastreamNamespace();
					List<String> mdKeys = triple.getMetadataKeys();
					
					
					for (String mKey : mdKeys) {
						String trimedKey = mKey.trim();
						
						
						if (this.existsDatastream(pid, mdID)){
							FedoraAPIA apiA = this.fedoraConnector.getFedoraClient().getAPIA();
							MIMETypedStream mimeTypedStream = apiA.getDatastreamDissemination(pid, mdID, null);
							
							if (mimeTypedStream != null){
								byte[] bytes = mimeTypedStream.getStream();
								DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
								DocumentBuilder builder = factory.newDocumentBuilder();
								Document document = builder.parse(new ByteArrayInputStream(bytes));
								
								List<String> mKeyValues = this.getMKeyValuesFromDocument(document, this.getLast(trimedKey, "/"));
								
								if (mKeyValues != null && mKeyValues.size() > 0){
									for (String mKeyValue : mKeyValues){
										if (mKeyValue != null) {
											if (!mKeyValue.isEmpty())
												this.rem.getAggregation().createTriple(new Predicate(new URI(mdNamespace + trimedKey)), mKeyValue);
										}
										
									}
								}
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
		catch (ServiceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 
		catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> getMKeyValuesFromDocument(Document document, String path) {
		List<String> values = new ArrayList<String>();
		Assert.assertNotNull(document);
		if (document.hasChildNodes()){
			NodeList nodeList = document.getChildNodes();
			this.getValues(nodeList, path, values);
		}
		else {
			String nodeName = document.getNodeName();
			String nodeValue = document.getTextContent();
			if (nodeName != null && !nodeName.isEmpty() && 
					nodeValue != null && !nodeValue.isEmpty()) {
				if (nodeName.endsWith(path)){
					values.add(nodeValue.trim());
				}
			}
		}
		
		return values;
	}
	
	private void getValues(NodeList nodeList, String path, List<String> values) {
		
		for (int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			String nodeName = node.getNodeName();
			String nodeValue = node.getTextContent();
			if (node.hasChildNodes()){
				if (nodeName.endsWith(path)){
					values.add(nodeValue.trim());
				}
				this.getValues(node.getChildNodes(), path, values);
			}
			else {
				if (nodeName.endsWith(path))
					values.add(nodeValue.trim());
			}
		}
	}
	
//	private boolean existsObject(String objectPID) {
//		boolean exists = false;
//		ObjectFields objectFields = this.fedoraUtils.getObjectFields(objectPID);
//		
//		if (objectFields != null) {
//			exists = true;
//		}
//		
//		return exists;
//	}
	
	private boolean existsDatastream(String objectPID, String datastreamID) {
		boolean exists = false;
		ObjectFields objectFields = this.fedoraUtils.getObjectFields(objectPID);
		DatastreamDef[] datastreamDefs;
		try {
			
			if (objectFields != null){
				if (objectPID.contains(":")){
					datastreamDefs = this.fedoraConnector.getFedoraClient().getAPIA().listDatastreams(objectPID, null);
					for (DatastreamDef datastreamDef : datastreamDefs){
						if (datastreamDef.getID().equals(datastreamID)){
							exists = true;
						}
					}
				}
			}
		} 
		catch (RemoteException e) {
			e.printStackTrace();
		} 
		catch (ServiceException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return exists;
	}

	private void addTriple(URI res1, URI res2, URI res3) {
		try {
			Triple tr = OREFactory.createTriple(res1, res2, res3);
			this.rem.getAggregation().addTriple(tr);
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
		
	}
	
	private void addTriple(URI res1, URI res2, String str) {
		try {
			Triple tr = OREFactory.createTriple(res1, res2, str);
			this.rem.getAggregation().addTriple(tr);
		} 
		catch (OREException e) {
			e.printStackTrace();
		}
	}
	
	private boolean objectExists(String pid) {
		boolean objExists = false;
				
		ObjectFields objFields = this.fedoraUtils.getObjectFields(pid);
		
		if (objFields != null)
			objExists = true;
		
		
		return objExists;
	}
	
	private void setMetadataToDatastream(URI dsURI) {
		
		String pid = this.getPID(dsURI.toString());
		String dsId = this.getLast(dsURI.toString(), "/");
		try {
			FedoraAPIM apiM = this.fedoraConnector.getFedoraClient().getAPIM();
			
			
			Datastream datastream = apiM.getDatastream(pid, dsId, null);
			
			if (datastream != null){
				String controlGroup = datastream.getControlGroup().getValue();
				String id = datastream.getID();
				String versionID = datastream.getVersionID();
				String[] altIDs = datastream.getAltIDs();
				String label = datastream.getLabel();
				boolean versionable = datastream.isVersionable();
				String MIMEType = datastream.getMIMEType();
				String formatURI = datastream.getFormatURI();
				String createDate = datastream.getCreateDate();
				long size = datastream.getSize();
				String state = datastream.getState();
				String location = datastream.getLocation();
				String checksumType = datastream.getChecksumType();
				String checksum = datastream.getChecksum();
				
				String foxmldatastreamURI = FEDORA_DEF_DATASTREAM;
				
				if ( controlGroup != null){
					if (!controlGroup.isEmpty()) {
						Triple controlGroupTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/controlGroup"), controlGroup);
						this.rem.getAggregation().addTriple(controlGroupTriple);
					}
				}
				if (id != null){
					if (!id.isEmpty()){
						Triple idGroupTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/id"), id);
						this.rem.getAggregation().addTriple(idGroupTriple);
					}
				}
				if (versionID != null){
					if (!versionID.isEmpty()){
						Triple versionIDTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/versionID"), versionID);
						this.rem.getAggregation().addTriple(versionIDTriple);
					}
				}
				for (String altID : altIDs){
					if (altID != null){
						if (!altID.isEmpty()){
							Triple altIDTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/altID"), altID);
							this.rem.getAggregation().addTriple(altIDTriple);
						}
					}
				}
				if (label != null){
					if (!label.isEmpty()){
						Triple labelTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/label"), label);
						this.rem.getAggregation().addTriple(labelTriple);
					}
				}
				if (String.valueOf(versionable) != null){
					if (!String.valueOf(versionable).isEmpty()){
						Triple versionableTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/versionable"), versionable);
						this.rem.getAggregation().addTriple(versionableTriple);
					}
				}
				if (MIMEType != null){
					if (!MIMEType.isEmpty()){
						Triple MIMETypeTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/MIMEType"), MIMEType);
						this.rem.getAggregation().addTriple(MIMETypeTriple);
					}
				}
				if (formatURI != null){
					if (!formatURI.isEmpty()) {
						Triple formatURITriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/formatURI"), formatURI);
						this.rem.getAggregation().addTriple(formatURITriple);
					}
				}
				if (createDate != null){
					if (!createDate.isEmpty()){
						Triple createDateTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/createDate"), createDate);
						this.rem.getAggregation().addTriple(createDateTriple);
					}
				}
				if (String.valueOf(size) != null){
					if (!String.valueOf(size).isEmpty()){
						Triple sizeTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/size"), size);
						this.rem.getAggregation().addTriple(sizeTriple);
					}
				}
				if (state != null){
					if (!state.isEmpty()){
						Triple stateTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/state"), state);
						this.rem.getAggregation().addTriple(stateTriple);
					}
				}
				if (location != null){
					if (!location.isEmpty()){
						Triple locationTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/location"), location);
						this.rem.getAggregation().addTriple(locationTriple);
					}
				}
				if (checksumType != null){
					if (!checksumType.isEmpty()){
						Triple checksumTypeTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/checksumType"), checksumType);
						this.rem.getAggregation().addTriple(checksumTypeTriple);
					}
				}
				if (checksum != null){
					if (!checksum.isEmpty()){
						Triple checksumTriple = OREFactory.createTriple(dsURI, new URI(foxmldatastreamURI + "/checksum"), checksum);
						this.rem.getAggregation().addTriple(checksumTriple);
					}
				}
			}
		} 
		catch (ServiceException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (OREException e) {
			e.printStackTrace();
		} 
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isDatastreamURI(URI uri) {
		boolean isDatastreamURI = false;
		
		Property prop = this.model.getProperty(FEDORA_REL_DISSEMINATES);
		NodeIterator iter = this.model.listObjectsOfProperty(prop);
		
		while (iter.hasNext()) {
			RDFNode node = iter.nextNode();
			String nodeStr = node.toString();
			
			if (nodeStr.compareTo(uri.toString()) == 0) {
				isDatastreamURI = true;
				break;
			}
		}
		
		return isDatastreamURI;
	}
	
	private boolean isSetDSMetatada() {
		boolean isRelsFilterON = false;
		
		Assert.assertNotNull("The properties map is null.", this.propertiesMap);
		Assert.assertTrue("The property 'setDatastreamsMetadata' does not exist", 
				this.propertiesMap.containsKey("setDatastreamsMetadata"));
		Assert.assertTrue("The property 'setDatastreamsMetadata' has no values", 
				this.propertiesMap.get("setDatastreamsMetadata") != null || 
				this.propertiesMap.get("setDatastreamsMetadata").size() != 0);
		
		String filterValue = this.propertiesMap.get("setDatastreamsMetadata").get(0).trim();
		
		if (filterValue.equalsIgnoreCase("true"))
			isRelsFilterON = true;
		
		return isRelsFilterON;
	}

	public void initModel() {
		try {
			ORESerialiser serialiser = ORESerialiserFactory
					.getInstance(RDF_XML);
			ResourceMapDocument remDoc = serialiser.serialise(this.rem);
			String remString = remDoc.toString();

			InputStream is = new ByteArrayInputStream(remString.getBytes(UTF_8));
			this.model = ModelFactory.createDefaultModel();
			this.model.read(is, null);
		} catch (ORESerialiserException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private String getLast(String baseString, String separator){
		String[] parts = baseString.split(separator);
		String last = parts[parts.length - 1];
		return last;
	}
	
	private String getPID(String dsURI) {
		String pid = dsURI.split("/")[5];
		
		return pid;
	}
	
	public FedoraConnector getFedoraConnector() {
		return fedoraConnector;
	}

	public void setFedoraConnector(FedoraConnector fedoraConnector) {
		this.fedoraConnector = fedoraConnector;
	}

	public Map<String, List<String>> getPropertiesMap() {
		return propertiesMap;
	}

	public void setPropertiesMap(Map<String, List<String>> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}

	public FedoraUtils getFedoraUtils() {
		return fedoraUtils;
	}

	public void setFedoraUtils(FedoraUtils fedoraUtils) {
		this.fedoraUtils = fedoraUtils;
	}

	public PropertyUtils getPropertyUtils() {
		return propertyUtils;
	}

	public void setPropertyUtils(PropertyUtils propertyUtils) {
		this.propertyUtils = propertyUtils;
	}

	public MetadataConfiguration getMetadataConfig() {
		return metadataConfig;
	}

	public void setMetadataConfig(MetadataConfiguration metadataConfig) {
		this.metadataConfig = metadataConfig;
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
}
