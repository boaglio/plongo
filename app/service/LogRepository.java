package service;

import java.util.List;

import models.Log;

public interface LogRepository {

	long storeLog(List<Log> logs);
}
