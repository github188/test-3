package nari.Dao.bundle.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;

public class TransactionAdaptor implements Transaction{

	private DbAdaptor db = null;
	
	private boolean started = false;
	
	private final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	
	private Map<String,Savepoint> savepointMap = new HashMap<String,Savepoint>();
	
	public TransactionAdaptor(DbAdaptor db){
		this.db = db;
	}

	public void start() throws SQLException{
		if(started){
			return;
		}
		started = true;
		Connection conn = db.getConnection();
		conn.setAutoCommit(false);
		threadLocal.set(conn);
	}

	public void stop() throws SQLException{
		if(!started){
			throw new SQLException("start trasaction first.");
		}
		if(threadLocal.get()!=null){
			threadLocal.get().setAutoCommit(true);
			threadLocal.get().close();
			threadLocal.set(null);
		}
		started = false;
	}
	
	public void addRollbackPoint(String pointName) throws SQLException{
		if(!started){
			throw new SQLException("transaction is closed or not start yet");
		}
		Savepoint point = threadLocal.get().setSavepoint(pointName);
		if(savepointMap.containsKey(pointName)){
			throw new SQLException("savepoint is already exist");
		}
		savepointMap.put(pointName, point);
	}
	
	public void rollback(String pointName) throws SQLException{
		if(!started){
			throw new SQLException("transaction is closed or not start yet");
		}
		Savepoint point = savepointMap.get(pointName);
		if(point==null){
			throw new SQLException("savepoint is not exist");
		}
		threadLocal.get().rollback(point);
		stop();
	}
	
	public boolean isStarted(){
		return started;
	}
	
	public void rollback() throws SQLException{
		threadLocal.get().rollback();
		stop();
	}
	
	@Override
	public void commit() throws SQLException {
		if(threadLocal.get()!=null){
			threadLocal.get().commit();
			threadLocal.set(null);
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		return threadLocal.get();
	}
}
