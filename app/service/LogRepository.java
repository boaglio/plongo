package service;

import java.util.List;

import models.Log;

public interface LogRepository {

	long storeLog(String collectionName,List<Log> logs);

}
