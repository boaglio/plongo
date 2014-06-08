package controllers;

import java.io.File;
import java.util.List;

import models.Log;
import models.SystemApplication;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import service.impl.MongoDAO;
import util.DateUtil;
import util.FileUtil;

public class Application extends Controller {

	public static Result index() {
		MongoDAO dao = new MongoDAO();
		return ok(views.html.index.render(dao.getCollections()));
	}

	public static Result details(String collection) {
		MongoDAO dao = new MongoDAO();
		SystemApplication systemApplication = new SystemApplication();
		systemApplication.name = collection;
		return ok(views.html.details.render(systemApplication,dao.getLogList(systemApplication)));
	}

	public static Result upload() {

		MultipartFormData body = request().body().asMultipartFormData();
		FilePart logFile = body.getFile("logfile");
		if (logFile != null) {

			String sysAppId = Form.form().bindFromRequest().get("appId");
			String defaultTempFileExtension = Play.application().configuration().getString("defaultTempFileExtension");
			String fileName = "plongo-" + sysAppId + "-" + DateUtil.getTimestampStr() + defaultTempFileExtension;
			String contentType = logFile.getContentType();
			File file = logFile.getFile();

			System.out.println("file name=" + fileName);
			System.out.println("file size=" + file.length() / 1024 + " kb");

			List<Log> logs = FileUtil.convertFileToLogList(file);
			System.out.println("log size=" + logs.size());

			String collectionName = sysAppId;
			if (sysAppId.equals("-1")) {
				collectionName = logFile.getFilename();
			}
			System.out.println("collectionName=" + collectionName);
			MongoDAO dao = new MongoDAO();
			dao.storeLog(collectionName,logs);

			String tempDir = Play.application().configuration().getString("tempDir");
			file.renameTo(new File(tempDir,fileName));

			return ok(views.html.upload.render("File  \"" + fileName + "\" type [" + contentType + "] loaded !"));

		} else {
			flash("error","Upload error.");
			return redirect(routes.Application.index());
		}
	}

}
