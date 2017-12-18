package nari.Dao.activator;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import nari.Dao.bundle.bean.DataBase;
import nari.Dao.bundle.bean.Jdbc;
import nari.Dao.bundle.bean.Jndi;
import nari.Dao.bundle.factory.DbManager;
import nari.Dao.bundle.simple.SimpleDao;
import nari.Dao.bundle.transaction.TransactionDbAdapter;
import nari.Dao.interfaces.DbAdaptor;
import nari.Dao.interfaces.impl.DbAdaptorHandler;
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

@ActivatorReg(name="DbActivator")
public class DbActivator implements Activator{

	private BundleContext context = null;
	
	public DbActivator(){
		
	}

	@Override
	public boolean init(BundleConfig config) throws BundleException {
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		this.context = context;
		DataBase db = initDataBase("database","config/db.xml");
		List<Jdbc> jdbcList = db.getJdbcList();
		
		if(jdbcList!=null){
			for(final Jdbc jdbc:jdbcList){
				if(!jdbc.isActive()){
					continue;
				}
				DbManager manager = new DbManager();
				final SimpleDao dao = manager.initJdbcDataBase(jdbc);
				
				final Version version = (jdbc.getVersion()==null || "".equalsIgnoreCase(jdbc.getVersion()))?Version.defaultVersion():Version.version(jdbc.getVersion());
				
				context.registService(DbAdaptor.class, new Provider<DbAdaptor>() {
					
					private final AtomicReference<DbAdaptor> ref = new AtomicReference<DbAdaptor>(DbAdaptor.NONE);
					
					@Override
					public DbAdaptor get() throws BundleException {
						if(dao==null){
							return DbAdaptor.NONE;
						}
						if(ref.get()==DbAdaptor.NONE){
							DbAdaptor db = new DbAdaptorHandler(dao);
							DbAdaptor dbAdaptor = new TransactionDbAdapter(db);
							ref.compareAndSet(DbAdaptor.NONE, dbAdaptor);
						}
						return ref.get();
					}

					@Override
					public AttributeKey getKey() throws BundleException {
						return AttributeKey.key(DbAdaptor.class, version);
					}

					@Override
					public Version version() throws BundleException {
						return version;
					}

					@Override
					public ServiceFilter[] getFilter() throws BundleException {
						return null;
					}
				});
			}
		}
		
		List<Jndi> jndiList = db.getJndiList();
		
		if(jndiList!=null){
			for(final Jndi jndi:jndiList){
				if(!jndi.isActive()){
					continue;
				}
				DbManager manager = new DbManager();
				final SimpleDao dao = manager.initJndiDataBase(jndi);
				
				final Version version = (jndi.getVersion()==null || "".equalsIgnoreCase(jndi.getVersion()))?Version.defaultVersion():Version.version(jndi.getVersion());
				
				context.registService(DbAdaptor.class, new Provider<DbAdaptor>() {
					
					private final AtomicReference<DbAdaptor> ref = new AtomicReference<DbAdaptor>(DbAdaptor.NONE);
					
					@Override
					public DbAdaptor get() throws BundleException {
						if(dao==null){
							return DbAdaptor.NONE;
						}
						if(ref.get()==DbAdaptor.NONE){
							DbAdaptor db = new DbAdaptorHandler(dao);
							DbAdaptor dbAdaptor = new TransactionDbAdapter(db);
							ref.compareAndSet(DbAdaptor.NONE, dbAdaptor);
						}
						return ref.get();
					}

					@Override
					public AttributeKey getKey() throws BundleException {
						return AttributeKey.key(DbAdaptor.class, version);
					}

					@Override
					public Version version() throws BundleException {
						return version;
					}

					@Override
					public ServiceFilter[] getFilter() throws BundleException {
						return null;
					}
				});
			}
		}
		
		
		
		
//		DataBase pms_db = initDataBase("pms_database","config/db_pms.xml");
//		
//		manager = new DbManager();
//		
//		final SimpleDao pms_dao = manager.initDataBase(pms_db);
//		
//		context.registService(DbAdaptor.class, new Provider<DbAdaptor>() {
//			
//			private final AtomicReference<DbAdaptor> ref = new AtomicReference<DbAdaptor>(DbAdaptor.NONE);
//			
//			@Override
//			public DbAdaptor get() throws BundleException {
//				if(dao==null){
//					return DbAdaptor.NONE;
//				}
//				if(ref.get()==DbAdaptor.NONE){
////					DbAdaptor dbAdaptor = new DbAdaptorHandler(pms_dao);
//					DbAdaptor db = new DbAdaptorHandler(pms_dao);
//					DbAdaptor dbAdaptor = new TransactionDbAdapter(db);
//					ref.compareAndSet(DbAdaptor.NONE, dbAdaptor);
//				}
//				return ref.get();
//			}
//
//			@Override
//			public AttributeKey getKey() throws BundleException {
//				return AttributeKey.key(DbAdaptor.class, Version.version("pms_db"));
//			}
//
//			@Override
//			public Version version() throws BundleException {
//				return Version.version("pms_db");
//			}
//
//			@Override
//			public ServiceFilter[] getFilter() throws BundleException {
//				return null;
//			}
//		});
		return true;
	}

	@Override
	public boolean stop(BundleContext context) throws BundleException {
		return true;
	}

	private DataBase initDataBase(String name, String path){
//		InputStream stream = this.getClass().getResourceAsStream("config/db.xml");
//		InputStream stream = getClass().getClassLoader().getResourceAsStream("config/db.xml");
		InputStream stream = context.getResourceAsStream(path);
		
		ConfigSearch searcher = new ConfigSearchService();
		DataBase db = searcher.loadConfigCache(name,stream,"xml",DataBase.class);
		if(db==null){
			return DataBase.NONE;
		}
		return db;
	}
}

