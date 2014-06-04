package service.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Log;
import models.SystemApplication;
import service.LogRepository;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public class MongoDAO implements LogRepository {

	private DBCollection logsCollection;
	private DB db;

	public MongoDAO() {
		try {
			MongoClient mongoClient = new MongoClient(new ServerAddress("localhost",27017));
			db = mongoClient.getDB("plongo");

			if (db.collectionExists("plongo")) {
				logsCollection = db.getCollection("plongo");
			} else {
				DBObject options = BasicDBObjectBuilder.start().add("capped",true).add("size",2000000000l).get();
				logsCollection = db.createCollection("plongo",options);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long storeLog(List<Log> logs) {
		long count = 1;
		for (Log log : logs) {
			System.out.println(count + " -  added: " + addLog(log));
			count++;
		}
		return count;
	}

	public List<SystemApplication> getCollections() {

		List<SystemApplication> list = new ArrayList<SystemApplication>();
		for (String dbName : db.getCollectionNames()) {
			SystemApplication sysapp = new SystemApplication();
			sysapp.name = dbName;
			if (!dbName.equals("plongo") && !dbName.equals("system.indexes")) {
				list.add(sysapp);
			}
		}

		return list;
	}

	public boolean addLog(Log log) {

		BasicDBObject logCollection = new BasicDBObject();

		logCollection.append("_id",log.id).append("timestamp",log.timestamp).append("content",log.content);

		if (log.verbosity != null && !log.verbosity.equals("")) {
			logCollection.append("verbosity",log.verbosity);
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
				log.timestamp = (String) resultElementMap.get("timestamp");
				log.content = (String) resultElementMap.get("content");
				log.verbosity = (String) resultElementMap.get("verbosity");

				list.add(log);

			}
		} finally {
			cursor.close();
		}

		return list;
	}

}
