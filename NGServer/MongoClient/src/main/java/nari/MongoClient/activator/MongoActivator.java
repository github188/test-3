package nari.MongoClient.activator;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

import nari.Logger.LoggerManager;
import nari.MongoClient.activator.bean.MongoConfig;
import nari.MongoClient.impl.MongoAdaptorHandler;
import nari.MongoClient.interfaces.MongoAdaptor;
import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;

import com.application.plugin.Activator;
import com.application.plugin.ActivatorReg;
import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.Provider;
import com.application.plugin.Version;
import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

@ActivatorReg(name="MongoActivator")
public class MongoActivator implements Activator{

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	private BundleContext context = null;
	
	private MongoClient mongoClient = null;
	
	public MongoActivator(){
		
	}

	@Override
	public boolean init(BundleConfig config) throws BundleException {
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		this.context = context;
		final MongoConfig mongo = initMongoConfig();
		
		if(mongo==MongoConfig.NONE){
			logger.info("config mongo.xml not found...");
			return true;
		}
		
		context.registService(MongoAdaptor.class, new Provider<MongoAdaptor>() {
			
			private final AtomicReference<MongoAdaptor> ref = new AtomicReference<MongoAdaptor>(MongoAdaptor.NONE);
			
			@Override
			public MongoAdaptor get() throws BundleException {
				if(!mongo.isActive()){
					return MongoAdaptor.NONE;
				}
				if(ref.get()==MongoAdaptor.NONE){
					String sUrl = String.format("mongodb://%s:%s@%s:%d/%s", "root", "P@ssw0rd", 
							mongo.getMongoAddress(), Integer.parseInt(mongo.getMongoPort()), "admin");
					mongoClient = new MongoClient(mongo.getMongoAddress(), Integer.parseInt(mongo.getMongoPort()));
					MongoDatabase dataBase = mongoClient.getDatabase(mongo.getMongoDbName());
					
					MongoAdaptor mongoAdapter = new MongoAdaptorHandler(mongoClient,dataBase);
					ref.compareAndSet(MongoAdaptor.NONE, mongoAdapter);
				}
				return ref.get();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(MongoAdaptor.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return Version.defaultVersion();
			}

			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				return null;
			}
		});
		return true;
	}

	@Override
	public boolean stop(BundleContext context) throws BundleException {
		if(mongoClient!=null){
			mongoClient.close();
		}
		return true;
	}

	private MongoConfig initMongoConfig(){
		InputStream stream = context.getResourceAsStream("config/mongo.xml");
		ConfigSearch searcher = new ConfigSearchService();
		MongoConfig mongo = searcher.loadConfigCache("mongo",stream,"xml",MongoConfig.class);
		if(mongo==null){
			return MongoConfig.NONE;
		}
		return mongo;
	}
}

