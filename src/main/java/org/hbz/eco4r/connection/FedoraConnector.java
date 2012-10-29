package org.hbz.eco4r.connection;

/**
 * <b>Package Name: org.hbz.eco4r.connection</b>
 * <b>Package Description: </b>
 * <p>This package is about connection issues to the Fedora system. A Connection Object is established  
 * either through FedoraClient or HTTPCLient </p>
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

import static org.hbz.eco4r.vocabulary.FedoraVocabulary.FEDORA_HOST;
import static org.hbz.eco4r.vocabulary.FedoraVocabulary.FEDORA_PASSWORD;
import static org.hbz.eco4r.vocabulary.FedoraVocabulary.FEDORA_PORT;
import static org.hbz.eco4r.vocabulary.FedoraVocabulary.FEDORA_PROTOCOL;
import static org.hbz.eco4r.vocabulary.FedoraVocabulary.FEDORA_SERVICE_SUFFIX;
import static org.hbz.eco4r.vocabulary.FedoraVocabulary.FEDORA_USER;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.fcrepo.client.FedoraClient;
import org.hbz.eco4r.config.Configuration;
import org.hbz.eco4r.config.Property;
import org.junit.Assert;

/**
 * <b>Class Name</b>: FedoraConnector</br> <b>Class Definition</b>:
 * <p>
 * A FedoraConnector object, establishes a Connection with Fedora and returns a
 * FedoraClient object that is responsible for the communication through the
 * Fedora APIs
 * </p>
 * 
 * @author Anouar Boulal, boulal@hbz-nrw.de
 * 
 */
public class FedoraConnector implements Connector
{

	private static Logger logger = Logger.getLogger(Connector.class);

	private String protocol;
	private String user;
	private String passwd;
	private String host;
	private String port;
	private String suffix;
	private String baseURL;

	private Configuration configuration;

	private FedoraClient fedoraClient;

	private List<Object> connectionObjects;
	private Connection connection;

	public FedoraConnector(String configFile)
	{
		this.configuration = new Configuration(configFile);
		this.configure(this.configuration);
		this.connect();
	}

	public FedoraConnector(Configuration configuration)
	{
		this.configuration = configuration;
		this.configure(this.configuration);
		this.connect();
	}

	@Override
	public void configure(Configuration configuration)
	{
		this.configuration = configuration;
		List<Property> properties = configuration.getProperties();

		for (Property property : properties)
		{

			if (property.getKey().equals(FEDORA_PROTOCOL))
				this.protocol = property.getValues().get(0);

			if (property.getKey().equals(FEDORA_USER))
				this.user = property.getValues().get(0);

			if (property.getKey().equals(FEDORA_PASSWORD))
				this.passwd = property.getValues().get(0);

			if (property.getKey().equals(FEDORA_HOST))
				this.host = property.getValues().get(0);

			if (property.getKey().equals(FEDORA_PORT))
				this.port = property.getValues().get(0);

			if (property.getKey().equals(FEDORA_SERVICE_SUFFIX))
				this.suffix = property.getValues().get(0);

		}

		this.baseURL = this.getBaseURL(this.protocol, this.host, this.port,
				this.suffix);

		Assert.assertNotNull("The user is null or is empty", this.protocol);
		Assert.assertNotNull("The user is null or is empty", this.user);
		Assert.assertNotNull("The password is null or is empty", this.passwd);
		Assert.assertNotNull("The host is null or is empty", this.host);
		Assert.assertNotNull("The port is null or is empty", this.port);
		Assert.assertNotNull("The suffix is null or is empty", this.suffix);
		Assert.assertNotNull("The base URL is null or is empty", this.baseURL);
	}

	@Override
	public Connection connect()
	{
		try
		{
			this.fedoraClient = new FedoraClient(this.baseURL, this.user,
					this.passwd);
			this.connectionObjects = new ArrayList<Object>();
			this.connectionObjects.add(fedoraClient);
			this.connection = new Connection(connectionObjects);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		return this.connection;
	}

	private String getBaseURL(String protocol, String host, String port,
			String fedoraServicePrefix)
	{
		String baseURL = protocol + "://" + host + ":" + port + "/"
				+ fedoraServicePrefix;
		return baseURL;
	}

	/**
	 * Java Beans getter and setter methods
	 */

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getPasswd()
	{
		return passwd;
	}

	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getSuffix()
	{
		return suffix;
	}

	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}

	public String getBaseURL()
	{
		return baseURL;
	}

	public void setBaseURL(String baseURL)
	{
		this.baseURL = baseURL;
	}

	public FedoraClient getFedoraClient()
	{
		return fedoraClient;
	}

	public void setFedoraClient(FedoraClient fedoraClient)
	{
		this.fedoraClient = fedoraClient;
	}

	public List<Object> getConnectionObjects()
	{
		return connectionObjects;
	}

	public void setConnectionObjects(List<Object> connectionObjects)
	{
		this.connectionObjects = connectionObjects;
	}

	public Connection getConnection()
	{
		return connection;
	}

	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

	public Configuration getConfiguration()
	{
		return configuration;
	}

	public void setConfiguration(Configuration configuration)
	{
		this.configuration = configuration;
	}

	@Override
	public String toString()
	{
		String str = "";

		logger.info("Connection Properties:");
		logger.info("User: " + this.user);
		logger.info("passwd: " + this.passwd);
		logger.info("host: " + this.host);
		logger.info("port: " + this.port);
		logger.info("suffix: " + this.suffix);
		logger.info("baseURL: " + this.baseURL);

		if (this.getConnectionObjects().size() != 0)
		{
			logger.info("The following connector objects was loaded:");
			for (Object object : this.getConnectionObjects())
				logger.info(object.getClass());
		}

		return str;
	}
}
