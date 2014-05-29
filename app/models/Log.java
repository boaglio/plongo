package models;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class Log {

	@Id
	@ObjectId
	public String id;

	public SystemApplication app;

	public String timestamp;

	public String verbosity;

	public String content;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (timestamp == null ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		Log other = (Log) obj;
		if (id == null) {
			if (other.id != null) { return false; }
		} else if (!id.equals(other.id)) { return false; }
		if (timestamp == null) {
			if (other.timestamp != null) { return false; }
		} else if (!timestamp.equals(other.timestamp)) { return false; }
		return true;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", app=" + app + ", timestamp=" + timestamp + ", verbosity=" + verbosity + ", content=" + content + "]";
	}

	// public static List<Log> all() {
	// return Log.coll.find().toArray();
	// }
	//
	// private static JacksonDBCollection<Log,String> coll = MongoDB.getCollection("plongo",Log.class,String.class);

}
