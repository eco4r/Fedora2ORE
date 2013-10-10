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

import static org.hbz.eco4r.vocabulary.ConfigVocabulary.AGGREGATES_RELS;
import static org.hbz.eco4r.vocabulary.ConfigVocabulary.EXCLUDE_DATASTREAMS_BY_ID;
import static org.hbz.eco4r.vocabulary.ConfigVocabulary.EXCLUDE_DATASTREAMS_BY_URI;
import static org.hbz.eco4r.vocabulary.ConfigVocabulary.SEARCH_DEPTH;
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
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.dspace.foresite.OREException;
import org.dspace.foresite.OREFactory;
import org.dspace.foresite.ORESerialiser;
import org.dspace.foresite.ORESerialiserException;
import org.dspace.foresite.ORESerialiserFactory;
import org.dspace.foresite.ResourceMap;
import org.dspace.foresite.ResourceMapDocument;
import org.dspace.foresite.Triple;
import org.fcrepo.server.types.gen.DatastreamDef;
import org.fcrepo.server.types.gen.MIMETypedStream;
import org.fcrepo.server.types.gen.ObjectFields;
import org.hbz.eco4r.connection.FedoraConnector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <b>Class Name</b>: RelationshipsMetadata</br> <b>Class Definition</b>:
 * <p>
 * Util class for handling Relationships
 * </p>
 * 
 * @author Anouar Boulal, boulal@hbz-nrw.de
 * 
 */

public class RelationshipUtils
{

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(RelationshipUtils.class);

	private FedoraConnector fedoraConnector;
	private Map<String, List<String>> propertiesMap;
	private FedoraUtils fedoraUtils;
	private PropertyUtils propertyUtils;
	private ResourceMap rem;

	public RelationshipUtils(FedoraConnector fedoraConnector,
			FedoraUtils fedoraUtils, PropertyUtils propertyUtils,
			MetadataUtils metadataUtils)
	{
		this.setFedoraConnector(fedoraConnector);
		this.setPropertiesMap(propertyUtils.getPropertiesMap());
		this.setFedoraUtils(fedoraUtils);
		this.setPropertyUtils(propertyUtils);
	}

	public void setUpReMRelationships(ResourceMap rem)
	{

		this.setRem(rem);

		try
		{
			this.expand(this.rem.getAggregation().getURI());
			this.addAggregatedResources();
		}
		catch (OREException e)
		{
			e.printStackTrace();
		}
	}

	private void addAggregatedResources()
	{

		List<URI> aggregatesRels = this.getAggregatesRels();
		Model model = this.initModel();

		try
		{
			StmtIterator iter = model.listStatements(
					model.getResource(this.rem.getAggregation().getURI()
							.toString()), (Property) null, (RDFNode) null);

			while (iter.hasNext())
			{
				Statement stmt = iter.nextStatement();
				Property pred = stmt.getPredicate();
				RDFNode obj = stmt.getObject();

				URI predURI = new URI(pred.getURI());
				if (aggregatesRels.contains(predURI))
				{
					if (!obj.isAnon())
					{
						if (obj.isResource())
						{
							URI objURI = new URI(obj.asResource().getURI());
							this.rem.getAggregation().createAggregatedResource(
									objURI);
						}
					}
				}
			}
		}
		catch (OREException e)
		{
			e.printStackTrace();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	private List<URI> getAggregatesRels()
	{
		List<String> list = this.propertiesMap.get(AGGREGATES_RELS);
		List<String> aggRels = this.trimValues(list);
		List<URI> uris = new ArrayList<URI>();

		try
		{
			for (String aggRel : aggRels)
			{
				URI uri = new URI(aggRel);
				if (!uris.contains(uri))
					uris.add(uri);
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}

		return uris;
	}

	public void expand(URI uri)
	{
		int searchDepth = this.getSearchDepth();
		List<String> excludedResourcesByPID = this.getExcludedResourcesByPID();
		List<URI> excludedRelationships = this.getExcludedRelationships();
		List<String> excludedDatastreamsByID = this
				.getExcludedDatastreamsByID();
		List<URI> excludedDatastreamsByURI = this.getExcludedDatastreamsByURI();

		if (this.objectExists(this.getLast(uri.toString(), "/")))
		{

			List<URI> next = new ArrayList<URI>();
			List<URI> passed = new ArrayList<URI>();
			List<URI> iteration = new ArrayList<URI>();

			iteration.add(uri);

			for (int i = 0; i < searchDepth; i++)
			{

				for (URI iterURI : iteration)
				{
					List<URI> nextURIs = this.expandRelsFor(iterURI,
							excludedResourcesByPID, excludedRelationships,
							excludedDatastreamsByID, excludedDatastreamsByURI,
							i);
					passed.add(iterURI);
					for (URI nextURI : nextURIs)
					{
						if (!next.contains(nextURI)
								&& !passed.contains(nextURI))
						{
							next.add(nextURI);
						}
					}
				}
				iteration.clear();
				iteration.addAll(next);
				next.clear();
			}
		}
	}

	private Model initModel()
	{
		Model model = null;
		try
		{
			ORESerialiser serialiser = ORESerialiserFactory
					.getInstance(RDF_XML);
			ResourceMapDocument remDoc = serialiser.serialise(this.rem);
			String remString = remDoc.toString();

			InputStream is = new ByteArrayInputStream(remString.getBytes(UTF_8));
			model = ModelFactory.createDefaultModel();
			model.read(is, null);
		}
		catch (ORESerialiserException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return model;
	}

	private List<URI> expandRelsFor(URI iterURI,
			List<String> excludedResourcesByPID,
			List<URI> excludedRelationships,
			List<String> ecludedDatastreamsByID,
			List<URI> excludedDatastreamsByURI, int i)
	{

		String pid = this.getLast(iterURI.toString(), "/");

		List<URI> nextURIs = new ArrayList<URI>();

		List<Statement> filteredRelatedURIStmts = this
				.getFilteredRelatedURIStmts(pid, excludedResourcesByPID,
						excludedRelationships, ecludedDatastreamsByID);

		List<Statement> filteredRelatedLiteralStmts = this
				.getFilteredRelatedLiteralStmts(pid, excludedResourcesByPID,
						excludedRelationships, ecludedDatastreamsByID);

		List<Triple> filteredRelatedDatastreamsStmts = this
				.getFilteredRelatedDatastreamsStmts(pid,
						excludedResourcesByPID, excludedRelationships,
						ecludedDatastreamsByID, excludedDatastreamsByURI);

		try
		{
			// Next URI Resources
			for (Statement stmt : filteredRelatedURIStmts)
			{
				URI subjURI = this.getDeferenciableURIForObject(stmt
						.getSubject().getURI());
				URI predURI = new URI(stmt.getPredicate().getURI());
				URI objURI = this.getDeferenciableURIForObject(stmt.getObject()
						.asResource().getURI());

				Triple tr = OREFactory.createTriple(subjURI, predURI, objURI);
				this.rem.getAggregation().addTriple(tr);

				if (!nextURIs.contains(objURI))
					nextURIs.add(objURI);
			}

			// Next Literal Resources
			for (Statement stmt : filteredRelatedLiteralStmts)
			{
				URI subjURI = this.getDeferenciableURIForObject(stmt
						.getSubject().getURI());
				URI predURI = new URI(stmt.getPredicate().getURI());
				String objStr = stmt.getObject().asLiteral().getString();

				Triple tr = OREFactory.createTriple(subjURI, predURI, objStr);
				this.rem.getAggregation().addTriple(tr);
			}

			// Next Datastreams
			for (Triple tr : filteredRelatedDatastreamsStmts)
			{
				this.rem.getAggregation().addTriple(tr);
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (OREException e)
		{
			e.printStackTrace();
		}

		return nextURIs;
	}

	private List<Triple> getFilteredRelatedDatastreamsStmts(String pid,
			List<String> excludedResourcesByPID,
			List<URI> excludedRelationships,
			List<String> excludedDatastreamsByID,
			List<URI> excludedDatastreamsByURI)
	{
		List<Triple> filteredTriples = new ArrayList<Triple>();
		try
		{
			URI subjURI = new URI(this.fedoraConnector.getBaseURL()
					+ "/objects/" + pid);
			URI predURI = new URI(FEDORA_REL_DISSEMINATES);

			List<URI> datastreams = this.getRelatedDatastreams(pid);
			for (URI ds : datastreams)
			{
				if (!excludedDatastreamsByID.contains(this.getLast(
						ds.toString(), "/"))
						&& !excludedDatastreamsByURI.contains(ds)
						&& !excludedRelationships.contains(predURI))
				{
					Triple tr = OREFactory.createTriple(subjURI, predURI, ds);
					if (!filteredTriples.contains(tr))
					{
						filteredTriples.add(tr);
					}
				}
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (OREException e)
		{
			e.printStackTrace();
		}

		return filteredTriples;
	}

	private List<Statement> getFilteredRelatedLiteralStmts(String pid,
			List<String> excludedResourcesByPID,
			List<URI> excludedRelationships, List<String> ecludedDatastreamsByID)
	{

		List<Statement> filteredStmt = new ArrayList<Statement>();

		try
		{
			Model model = this.getRELSEXTModel(pid);
			StmtIterator iter = model.listStatements();
			while (iter.hasNext())
			{
				Statement stmt = iter.nextStatement();
				Resource subj = stmt.getSubject();
				Property pred = stmt.getPredicate();
				RDFNode obj = stmt.getObject();

				if (!obj.isAnon())
				{
					if (obj.isLiteral())
					{
						String subjPID = this.getLast(subj.getURI(), "/");
						URI predURI = new URI(pred.getURI());

						if (!excludedResourcesByPID.contains(subjPID)
								&& !excludedRelationships.contains(predURI))
						{
							if (!filteredStmt.contains(stmt))
								filteredStmt.add(stmt);
						}
					}
				}
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}

		return filteredStmt;
	}

	private List<Statement> getFilteredRelatedURIStmts(String pid,
			List<String> excludedResourcesByPID,
			List<URI> excludedRelationships, List<String> ecludedDatastreamsByID)
	{

		List<Statement> filteredStmt = new ArrayList<Statement>();
		try
		{
			Model model = this.getRELSEXTModel(pid);
			StmtIterator iter = model.listStatements();
			while (iter.hasNext())
			{
				Statement stmt = iter.nextStatement();
				Resource subj = stmt.getSubject();
				Property pred = stmt.getPredicate();
				RDFNode obj = stmt.getObject();

				if (!obj.isAnon())
				{
					if (obj.isResource())
					{
						String subjPID = this.getLast(subj.getURI(), "/");
						URI predURI = new URI(pred.getURI());
						String objPID = this.getLast(obj.asResource().getURI(),
								"/");

						if (!excludedResourcesByPID.contains(subjPID)
								&& !excludedResourcesByPID.contains(objPID)
								&& !excludedRelationships.contains(predURI))
						{
							if (!filteredStmt.contains(stmt))
								filteredStmt.add(stmt);
						}
					}
				}
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}

		return filteredStmt;
	}

	private URI getDeferenciableURIForObject(String uri)
	{
		String pid = this.getLast(uri, "/");

		URI newURI = null;
		try
		{
			newURI = new URI(this.fedoraConnector.getBaseURL() + "/objects/"
					+ pid);
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}

		return newURI;
	}

	public List<URI> getRelatedDatastreams(String pid)
	{
		List<URI> datastreamURIs = new ArrayList<URI>();

		try
		{
			if (pid.contains(":") && this.objectExists(pid))
			{
				DatastreamDef[] datastreamDefs = this.fedoraConnector
						.getFedoraClient().getAPIA().listDatastreams(pid, null);
				if (datastreamDefs != null && datastreamDefs.length != 0)
				{
					for (DatastreamDef datastreamDef : datastreamDefs)
					{
						String dsId = datastreamDef.getID();
						URI dsURI = new URI(this.fedoraConnector.getBaseURL()
								+ "/objects/" + pid + "/datastreams/" + dsId);
						if (!datastreamURIs.contains(dsURI))
						{
							datastreamURIs.add(dsURI);
						}
					}
				}
			}
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}

		return datastreamURIs;
	}

	private List<String> getExcludedResourcesByPID()
	{
		List<String> list = this.propertiesMap.get("excludeResourcesByPID");

		return this.trimValues(list);
	}

	private List<URI> getExcludedRelationships()
	{
		List<String> list = this.propertiesMap.get("excludeRelationships");
		List<String> excludedResources = this.trimValues(list);

		List<URI> uris = new ArrayList<URI>();

		try
		{
			for (String res : excludedResources)
			{
				URI uri = new URI(res.trim());

				if (!uris.contains(uri))
					uris.add(uri);
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}

		return uris;
	}

	private List<String> getExcludedDatastreamsByID()
	{

		List<String> ids = this.propertiesMap.get(EXCLUDE_DATASTREAMS_BY_ID);

		return this.trimValues(ids);
	}

	private List<URI> getExcludedDatastreamsByURI()
	{
		List<URI> uris = new ArrayList<URI>();
		List<String> ids = this.propertiesMap.get(EXCLUDE_DATASTREAMS_BY_URI);
		List<String> newIds = this.trimValues(ids);
		try
		{
			for (String str : newIds)
			{
				URI uri = new URI(str);
				if (!uris.contains(uri))
					uris.add(uri);
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}

		return uris;
	}

	private List<String> trimValues(List<String> values)
	{
		List<String> trimedValues = new ArrayList<String>();

		for (String value : values)
		{
			trimedValues.add(value.trim());
		}

		return trimedValues;
	}

	private int getSearchDepth()
	{
		Integer searchDepth = Integer.valueOf(this.propertiesMap.get(
				SEARCH_DEPTH).get(0));

		if (searchDepth <= 0 || searchDepth == null)
			searchDepth = 1;

		return searchDepth;
	}

	private Model getRELSEXTModel(String pid)
	{
		Model model = ModelFactory.createDefaultModel();

		MIMETypedStream mimeTypedStream;
		try
		{
			if (pid.contains(":") && this.objectExists(pid))
			{
				mimeTypedStream = this.fedoraConnector.getFedoraClient()
						.getAPIA()
						.getDatastreamDissemination(pid, "RELS-EXT", null);
				InputStream is = new ByteArrayInputStream(
						mimeTypedStream.getStream());
				model.read(is, null);
			}
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return model;
	}

	private boolean objectExists(String pid)
	{
		boolean objExists = false;

		ObjectFields objFields = this.fedoraUtils.getObjectFields(pid);

		if (objFields != null)
			objExists = true;

		return objExists;
	}

	public FedoraConnector getFedoraConnector()
	{
		return fedoraConnector;
	}

	public void setFedoraConnector(FedoraConnector fedoraConnector)
	{
		this.fedoraConnector = fedoraConnector;
	}

	public Map<String, List<String>> getPropertiesMap()
	{
		return propertiesMap;
	}

	public void setPropertiesMap(Map<String, List<String>> propertiesMap)
	{
		this.propertiesMap = propertiesMap;
	}

	public FedoraUtils getFedoraUtils()
	{
		return fedoraUtils;
	}

	public void setFedoraUtils(FedoraUtils fedoraUtils)
	{
		this.fedoraUtils = fedoraUtils;
	}

	public PropertyUtils getPropertyUtils()
	{
		return propertyUtils;
	}

	public void setPropertyUtils(PropertyUtils propertyUtils)
	{
		this.propertyUtils = propertyUtils;
	}

	public ResourceMap getRem()
	{
		return rem;
	}

	public void setRem(ResourceMap rem)
	{
		this.rem = rem;
	}

	private String getLast(String baseString, String separator)
	{
		String[] parts = baseString.split(separator);
		String last = parts[parts.length - 1];
		return last;
	}
}
