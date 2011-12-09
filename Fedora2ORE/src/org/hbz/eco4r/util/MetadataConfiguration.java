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

import java.util.ArrayList;
import java.util.List;

import org.hbz.eco4r.config.Configuration;
import org.hbz.eco4r.config.Property;
import org.hbz.eco4r.connection.FedoraConnector;

import static org.hbz.eco4r.vocabulary.ConfigVocabulary.*;

/**
 * <b>Class Name</b>: MetadataConfiguration</br>
 * <b>Class Definition</b>:
 * <p>Represents a metadata configuration which is a set of configuration triples.</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */
public class MetadataConfiguration {

	private List<MetadataConfigTriple> metadataConfig;
	private FedoraConnector connector;
	
	
	public MetadataConfiguration(FedoraConnector connector) {
		this.connector = connector;
		this.setMetadataConfig(new ArrayList<MetadataConfigTriple>());
		this.loadMetadataConfiguration(this.connector.getConfiguration());
	}
	
	private void loadMetadataConfiguration(Configuration configuration) {
		
		List<Property> properties =  configuration.getProperties();
		List<Property> metadataProperties = this.getMetadataProperties(properties);
		
		List<String> dsKeys = this.getDSKeys(metadataProperties);
		
		for (String dsKey : dsKeys){
			MetadataConfigTriple triple = this.getMetadataConfigTriple(dsKey, metadataProperties);
			if (!this.metadataConfig.contains(triple)){
				this.metadataConfig.add(triple);
			}
		}
	}

	public MetadataConfigTriple getMetadataConfigTriple(String dsKey,
			List<Property> metadataProperties) {
		MetadataConfigTriple triple = null;
		
		String datastreamKey = dsKey;
		String datastreamNamespace = this.getDSNamespace(datastreamKey, metadataProperties);
		List<String> datastreamKeys = this.getDatastreamProperties(datastreamKey, metadataProperties);
		
		triple = new MetadataConfigTriple(datastreamKey, datastreamNamespace, datastreamKeys);
		
		return triple;
	}

	private List<String> getDatastreamProperties(String datastreamKey,
			List<Property> metadataProperties) {
		List<String> dsProperties = new ArrayList<String>();
		
		for (Property property : metadataProperties){
			String propertyKey = property.getKey();
			if (propertyKey.startsWith(METADATA_KEYS + "."  + datastreamKey)){
				dsProperties = property.getValues();
			}
		}
		
		return dsProperties;
	}

	private String getDSNamespace(String datastreamKey,
			List<Property> metadataProperties) {
		String dsNamespace = "";
		
		for (Property property : metadataProperties){
			String propertyKey = property.getKey();
			if (propertyKey.startsWith(METADATA_NAMESPACE + "." + datastreamKey)){
				dsNamespace = property.getValues().get(0);
				break;
			}
		}
		
		return dsNamespace;
	}

	private List<String> getDSKeys(List<Property> metadataProperties) {
		List<String> dsKeys = new ArrayList<String>();
		
		for (Property property : metadataProperties){
			String propKey = property.getKey();
			String dsKey = this.getLast(propKey, "\\.");
			if (!dsKeys.contains(dsKey))
				dsKeys.add(dsKey);
		}
		
		return dsKeys;
	}

	private List<Property> getMetadataProperties(List<Property> properties) {
		List<Property> metadataProperties = new ArrayList<Property>();
		
		for (Property property : properties){
			String propKey = property.getKey();
			if (propKey.startsWith(METADATA + ".")){
				if (!metadataProperties.contains(property))
					metadataProperties.add(property);
			}
		}
		
		return metadataProperties;
	}
	
	private String getLast(String baseString, String separator){
		String[] parts = baseString.split(separator);
		String last = parts[parts.length - 1];
		return last;
	}

	public List<MetadataConfigTriple> getMetadataConfig() {
		return metadataConfig;
	}
	public void setMetadataConfig(List<MetadataConfigTriple> metadataConfig) {
		this.metadataConfig = metadataConfig;
	}
	public FedoraConnector getConnector() {
		return connector;
	}
	public void setConnector(FedoraConnector connector) {
		this.connector = connector;
	}
}
