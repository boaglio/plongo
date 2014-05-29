package models;

import java.util.List;

import javax.persistence.Id;

import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

public class SystemApplication {

	@Id
	@ObjectId
	public String id;

	public String name;

	public static List<SystemApplication> all() {
		return SystemApplication.coll.find().toArray();
	}

	private static JacksonDBCollection<SystemApplication,String> coll = MongoDB.getCollection("plongo",SystemApplication.class,String.class);

}
