package org.hbz.eco4r.postprocessing;

/**
 * <b>Package Name: org.hbz.eco4r.processing</b>
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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dspace.foresite.ResourceMap;

/**
 * <b>Class Name</b>: ProcessingQueue</br>
 * <b>Class Definition</b>:
 * <p>Represents a post processing queue which consists of 1 or more processing nodes.</p>
 *
 * @author Anouar Boulal, boulal@hbz-nrw.de
 *
 */

public class ProcessingQueue {

	private static Logger logger = Logger.getLogger(ProcessingQueue.class);
	
	private List<ProcessingNode> processingQueue;
	private ResourceMap inReM;
	private ResourceMap currentReM;
	private ResourceMap outReM;
	
	public ProcessingQueue(ResourceMap inReM) {
		logger.info("Setting up the processing queue ...");
		this.processingQueue = new ArrayList<ProcessingNode>();
		this.inReM = inReM;
		logger.info("Actual number of processing nodes: " + this.processingQueue.size());
	}
	
	public ProcessingQueue(ResourceMap inReM, List<ProcessingNode> pQueue) {
		logger.info("Setting up the processing queue ...");
		this.processingQueue = pQueue;
		this.inReM = inReM;
		logger.info("Actual number of processing nodes: " + this.processingQueue.size());
	}
	
	public ResourceMap doProcess(){
		logger.info("Starting post processing queue ...");
		if (this.processingQueue.isEmpty() || this.processingQueue == null){
			logger.info("No operation to perform. Processing queue is empty or null.");
			logger.info("processing queue will be stopped.");
		}
		else {
			for (int i = 0; i < this.processingQueue.size(); i++){
				if (i == 0) {
					this.currentReM = this.processingQueue.get(i).run(inReM);
				}
				else {
					ResourceMap tmpReM = this.processingQueue.get(i).run(this.currentReM);
					this.currentReM = tmpReM;
				}
			}
		}
		
		logger.info("Post processing finished.");
		
		return outReM = this.currentReM;
	}
	
	public void addProcessingNode (ProcessingNode processingNode){
		logger.info("Adding processing node to processing queue: " + processingNode.getClass());
		this.processingQueue.add(processingNode);
		logger.info("Actual number of processing nodes: " + this.processingQueue.size());
	}
	
	public void addProcessingNode (int index, ProcessingNode processingNode){
		logger.info("Adding processing node to processing queue: " + processingNode.getClass());
		this.processingQueue.add(index, processingNode);
		logger.info("Actual number of processing nodes: " + this.processingQueue.size());
	}
	
	public void removeProcessingNode (ProcessingNode processingNode) {
		logger.info("Removing processing node to processing queue: " + processingNode.getClass());
		this.processingQueue.remove(processingNode);
		logger.info("Actual number of processing nodes: " + this.processingQueue.size());
	}
	
	public void removeProcessingNode (int index){
		logger.info("Removing processing node to processing queue: " + this.processingQueue.get(index).getClass());
		this.processingQueue.remove(index);
		logger.info("Actual number of processing nodes: " + this.processingQueue.size());
	}
	
	/**
	 * Getter and Setter methods
	 */
	
	public List<ProcessingNode> getProcessingQueue() {
		return processingQueue;
	}
	public void setProcessingQueue(List<ProcessingNode> processingQueue) {
		this.processingQueue = processingQueue;
	}
	public ResourceMap getInReM() {
		return inReM;
	}
	public void setInReM(ResourceMap inReM) {
		this.inReM = inReM;
	}
	public ResourceMap getCurrentReM() {
		return currentReM;
	}
	public void setCurrentReM(ResourceMap currentReM) {
		this.currentReM = currentReM;
	}
	public ResourceMap getOutReM() {
		return outReM;
	}
	public void setOutReM(ResourceMap outReM) {
		this.outReM = outReM;
	}
}
