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

import static org.hbz.eco4r.vocabulary.FedoraVocabulary.ALL_OBJECT_FIELDS;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.axis.types.NonNegativeInteger;
import org.apache.log4j.Logger;
import org.hbz.eco4r.connection.FedoraConnector;

import fedora.client.utility.AutoFinder;
import fedora.server.access.FedoraAPIA;
import fedora.server.types.gen.ComparisonOperator;
import fedora.server.types.gen.Condition;
import fedora.server.types.gen.FieldSearchQuery;
import fedora.server.types.gen.FieldSearchResult;
import fedora.server.types.gen.ListSession;
import fedora.server.types.gen.ObjectFields;


/**
 * <b>Class Name</b>: FedoraUtils</br>
 * <b>Class Definition</b>:
 * <p>Contains methods that raps specific Fedora-API methods as 'findObjects'.</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */
public class FedoraUtils {
	
	private static Logger logger = Logger.getLogger(FedoraUtils.class);

	private FedoraAPIA fedoraAPIA;
	private FedoraConnector fedoraConnector;
	
	public FedoraUtils(FedoraConnector fedoraConnector) {
		this.setFedoraConnector(fedoraConnector);
		try {
			this.setFedoraAPIA(this.fedoraConnector.getFedoraClient().getAPIA());
		} 
		catch (ServiceException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns The {@link ObjectFields} object for a Fedora Digital Object.
	 * The {@link ObjectFields} object contains specified fields of each 
	 * object matching a given criteria 
	 * 
	 * @param objectPID The pid of the object you want to find
	 * @param fedoraAPIA The Fedora Access API object
	 * @param objFields a String array which specifies the object fields to get
	 * in the objectFields object 
	 * 
	 * @return the specified fields of each object matching a given criteria.
	 */
	public ObjectFields getObjectFields(String objectPID){
		ObjectFields objectFields = null;
		
		FieldSearchQuery fieldSearchQuery = new FieldSearchQuery();
		fieldSearchQuery.setConditions(new Condition[]{new Condition("pid", ComparisonOperator.eq, objectPID)});
		
		try {
			FieldSearchResult fieldSearchResult = this.fedoraAPIA.findObjects(ALL_OBJECT_FIELDS, new NonNegativeInteger("1000"), fieldSearchQuery);
			if (fieldSearchResult != null){
				while (fieldSearchResult != null && objectFields == null){
					ObjectFields[] ofs = fieldSearchResult.getResultList();
					
					if (ofs != null){
						if (ofs.length != 0){
							objectFields = ofs[0];
						}
						else {
							ListSession listSession = fieldSearchResult.getListSession();
							if (listSession != null){
								String token = listSession.getToken();
								if (token != null){
									fieldSearchResult = AutoFinder.resumeFindObjects(this.fedoraAPIA, token);
								}
							}
							else
								break;
						}
					}
				}
			}
			else
				logger.error("The field search result object is NULL", new NullPointerException("The field search result object is NULL"));
		} 
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return objectFields;
	}
	
	
	public List<ObjectFields> getAllObjectFields(){
		List<ObjectFields> objFieldss = new ArrayList<ObjectFields>();
		
		FieldSearchQuery fieldSearchQuery = new FieldSearchQuery();
		fieldSearchQuery.setConditions(new Condition[]{new Condition("pid", ComparisonOperator.has, "*")});
		
		try {
			FieldSearchResult fieldSearchResult = this.fedoraAPIA.findObjects(ALL_OBJECT_FIELDS, new NonNegativeInteger("1000"), fieldSearchQuery);
			if (fieldSearchResult != null) {
				while (fieldSearchResult != null) {
					System.out.println(objFieldss.size());
					ObjectFields[] ofs = fieldSearchResult.getResultList();
					if (ofs != null) {
						if (ofs.length != 0) {
							for (ObjectFields of : ofs) {
								if (!objFieldss.contains(of))
									objFieldss.add(of);
							}
						}
					}
					
					ListSession listSession = fieldSearchResult.getListSession();
					if (listSession != null){
						String token = listSession.getToken();
						if (token != null){
							fieldSearchResult = AutoFinder.resumeFindObjects(this.fedoraAPIA, token);
						}
					}
					else
						break;
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return objFieldss;
	}
	
	
	/**
	 * Getter and Setter Methods
	 */

	public FedoraAPIA getFedoraAPIA() {
		return fedoraAPIA;
	}

	public void setFedoraAPIA(FedoraAPIA fedoraAPIA) {
		this.fedoraAPIA = fedoraAPIA;
	}

	public FedoraConnector getFedoraConnector() {
		return fedoraConnector;
	}

	public void setFedoraConnector(FedoraConnector fedoraConnector) {
		this.fedoraConnector = fedoraConnector;
	}
	
	
}
