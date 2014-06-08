package service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Log;
import models.SystemApplication;
import play.Play;
import service.LogRepository;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;

public class MongoDAO implements LogRepository {

	private static final String MONGODB_INTERNAL_COLLECTION = "system.indexes";
	private static final String FIELD_VERBOSITY = "verbosity";
	private static final String FIELD_CONTENT = "content";
	private static final String FIELD_TIMESTAMP = "timestamp";
	private DBCollection logsCollection;
	private DB db;
	private String mongoHost;
	private Integer mongoPort;
	private Long mongoCappedCollectionSize;
	private String mongodatabase;
	private Boolean debug;

	public MongoDAO() {
		try {
			debug = Play.application().configuration().getBoolean("plongo.debug");
			mongoHost = Play.application().configuration().getString("mongodb.server");
			mongoPort = Play.application().configuration().getInt("mongodb.port");
			mongodatabase = Play.application().configuration().getString("mongodb.database");
			mongoCappedCollectionSize = Play.application().configuration().getLong("mongodb.collectionSize");

			MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost,mongoPort));
			db = mongoClient.getDB(mongodatabase);

		} catch (MongoTimeoutException mte) {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long storeLog(String collectionName,List<Log> logs) {

		openOrCreateCollections(collectionName);
		long count = 1;
		for (Log log : logs) {
			if (debug) {
				System.out.println(count + " -  added: " + addLog(log));
			}
			count++;
		}
		return count;
	}

	public List<SystemApplication> getCollections() {

		List<SystemApplication> list = new ArrayList<SystemApplication>();
		for (String dbName : db.getCollectionNames()) {
			SystemApplication sysapp = new SystemApplication();
			sysapp.name = dbName;
			if (!dbName.equals(mongodatabase) && !dbName.equals(MONGODB_INTERNAL_COLLECTION)) {
				list.add(sysapp);
			}
		}

		return list;
	}

	public boolean addLog(Log log) {

		BasicDBObject logCollection = new BasicDBObject();

		logCollection.append("_id",log.id).append(FIELD_TIMESTAMP,log.timestamp).append(FIELD_CONTENT,log.content);

		if (log.verbosity != null && !log.verbosity.equals("")) {
			logCollection.append(FIELD_VERBOSITY,log.verbosity);
		}

		try {
			logsCollection.insert(logCollection);
			return true;
		} catch (MongoException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Log> getLogList(SystemApplication sysApp) {

		List<Log> list = new ArrayList<Log>();

		logsCollection = db.getCollection(sysApp.name);
		DBCursor cursor = logsCollection.find().limit(500);
		try {
			while (cursor.hasNext()) {

				DBObject resultElement = cursor.next();
				Map<?,?> resultElementMap = resultElement.toMap();
				Log log = new Log();
				log.timestamp = (String) resultElementMap.get(FIELD_TIMESTAMP);
				log.content = (String) resultElementMap.get(FIELD_CONTENT);
				log.verbosity = (String) resultElementMap.get(FIELD_VERBOSITY);

				list.add(log);

			}
		} finally {
			cursor.close();
		}

		return list;
	}

	private void openOrCreateCollections(String system) {
		if (db.collectionExists(system)) {
			if (debug) {
				System.out.println("opening collection " + system);
			}
			logsCollection = db.getCollection(system);
		} else {
			if (debug) {
				System.out.println("creating collection " + system);
			}
			DBObject options = BasicDBObjectBuilder.start().add("capped",true).add("size",mongoCappedCollectionSize).get();
			logsCollection = db.createCollection(system,options);
		}

	}
}
