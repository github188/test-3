package nari.model.symbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nari.MongoClient.interfaces.MongoAdaptor;
import nari.model.TableName;
import nari.model.bean.KVSymbolDef;
import nari.model.bean.SymbolDef;
import nari.model.device.AbstractDeviceModel;

import org.bson.BsonDocument;
import org.bson.BsonString;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class MongoSymbolSelector extends AbstractDeviceModel implements SymbolSelector {

	private MongoAdaptor mongoAdapter;
	
	public MongoSymbolSelector(MongoAdaptor mongoAdapter) {
		this.mongoAdapter = mongoAdapter;
	}
	
	@Override
	public Iterator<SymbolDef> search(String modelId) throws Exception {
		MongoCollection<BsonDocument> mongoColl = null;
		MongoCursor<BsonDocument> cursor = null;
		try {
			mongoColl = mongoAdapter.getMongoCollection(TableName.CONF_MODELSYMBOL);
			cursor = mongoAdapter.find(mongoColl, new BsonDocument().append("modelid", new BsonString(modelId)),null).iterator();
		} catch (Exception e) {
			throw new Exception("exception when query from mongodb.", e);
		}
		
		List<SymbolDef> defs = new ArrayList<SymbolDef>();
		
		BsonDocument doc = null;
		while(cursor.hasNext()){
			doc = cursor.next();
			defs.add(new KVSymbolDef(doc.getString("modelid").getValue(),doc.getString("symbolid").getValue(),doc.getString("symbolvlue").getValue()));
		}
		cursor.close();
		return defs.iterator();
	}

}
