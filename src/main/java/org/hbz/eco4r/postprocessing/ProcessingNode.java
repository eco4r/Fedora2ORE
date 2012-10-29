package org.hbz.eco4r.postprocessing;

import org.dspace.foresite.ResourceMap;

/**
 * <b>Package Name: org.hbz.eco4r.postprocessing</b>
 * <b>Package Description: </b>
 * <p>This package is about the post processing phase in the Resource Map generation process.
 * The post processing is modeled as a processing queue which includes one or more processing nodes.
 * The processing queue executes all modification tasks defined by the nodes in the order in which 
 * they are injested in the queue.</p>
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
 * <b>Class Name</b>: ProcessingNode</br>
 * <b>Class Definition</b>:
 * <p>Represents an interface of a processing node. Each processing node 
 * contains one run method which performs different modification operations on the Resource Map.
 * The input and output of such methods is always a Resource Map object.</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */

public interface ProcessingNode {

	/**
	 * Runs one processing task.
	 * Each processing node contains such a method which defines 
	 * one or more operations on the resource map.
	 * 
	 * @return the resource map after processing operations
	 */
	public abstract ResourceMap run(ResourceMap rem);
	
	
//	/**
//	 * Closes a Resource-Map Model and reinitializes it.
//	 * After performing modifications on a Model, it should be closed 
//	 * to enable the modifications to take effect.
//	 */
//	public abstract void reInitModel();
}
