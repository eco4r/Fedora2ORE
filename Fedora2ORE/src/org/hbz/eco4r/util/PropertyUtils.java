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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbz.eco4r.config.Property;
import org.hbz.eco4r.connection.FedoraConnector;
import org.junit.Assert;


/**
 * <b>Class Name</b>: PropertyUtils</br>
 * <b>Class Definition</b>:
 * <p>Util class for handling Properties</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */
public class PropertyUtils {
	
	private FedoraConnector fedoraConnector;
	private List<Property> propertyList;
	private Map<String, List<String>> propertiesMap;
	
	public PropertyUtils(FedoraConnector fedoraConnector) {
		this.setFedoraConnector(fedoraConnector);
		this.setPropertyList(fedoraConnector.getConfiguration().getProperties());
		this.setPropertiesMap(this.propertyList2Map());
	}


	public Map<String, List<String>> propertyList2Map(){
		Assert.assertNotNull("Property list object is null." + this.propertyList);
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		if (!this.propertyList.isEmpty()){
			for (Property property : this.propertyList){
				String key = property.getKey();
				List<String> value = property.getValues();
				if (!map.containsKey(key)){
					map.put(key, value);
				}
			}
		}
		
		return map;
	}
	
	public Map<String, List<String>> getDs2mKeysMap(){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (Map.Entry<String, List<String>> entry : this.propertiesMap.entrySet()){
			String key = entry.getKey();
			if (key.startsWith("metadata.datastream.")){
				List<String> values = entry.getValue();
				String dsId = key.replace("metadata.datastream.", "");
				if (!map.containsKey(dsId)){
					String dsNamespace = this.getDatastreamNamespace(dsId);
					List<String> nameSpacedKeys = this.propageteNamespace(dsNamespace, values);
					map.put(dsId, nameSpacedKeys);
				}
				
			}
		}
		
		return map;
	}

	private List<String> propageteNamespace(String dsNamespace,
			List<String> values) {
		List<String> propagatedKeys = new ArrayList<String>();
		
		for (String value : values){
			propagatedKeys.add(dsNamespace + value.trim());
		}
		
		return propagatedKeys;
	}

	private String getDatastreamNamespace(String dsId) {
		String fullName = "metadata.namespace." + dsId;
		String namespace = this.propertiesMap.get(fullName).get(0).trim();
		
		return namespace;
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
	

	public List<Property> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<Property> propertyList) {
		this.propertyList = propertyList;
	}
}
