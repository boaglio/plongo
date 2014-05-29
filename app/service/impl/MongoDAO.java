package service.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import models.Log;
import models.SystemApplication;
import service.LogRepository;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
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
			System.out.println(count + " -  adicionado: " + addLog(log));
			count++;
		}
		return count;
	}

	public List<SystemApplication> getCollections() {

		List<SystemApplication> list = new ArrayList<SystemApplication>();
		for (String dbName : db.getCollectionNames()) {
			SystemApplication sysapp = new SystemApplication();
			sysapp.name = dbName;
			list.add(sysapp);
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

}
