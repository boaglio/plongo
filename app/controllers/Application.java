package controllers;

import java.io.File;
import java.util.List;

import models.Log;
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

	public static Result upload() {

		MultipartFormData body = request().body().asMultipartFormData();
		FilePart logFile = body.getFile("logfile");
		if (logFile != null) {

			String sysAppId = Form.form().bindFromRequest().get("appId");
			String extensaoPadraoDeArquivoTemporario = Play.application().configuration().getString("extensaoPadraoDeArquivoTemporario");
			String fileName = "plongo-" + sysAppId + "-" + DateUtil.getTimestampStr() + extensaoPadraoDeArquivoTemporario;
			String contentType = logFile.getContentType();
			File file = logFile.getFile();

			System.out.println("file name=" + fileName);
			System.out.println("file size=" + file.length() / 1024 + " kb");

			List<Log> logs = FileUtil.convertFileToLogList(file);
			System.out.println("log size=" + logs.size());

			MongoDAO dao = new MongoDAO();
			dao.storeLog(logs);

			String diretorioTemporario = Play.application().configuration().getString("diretorioTemporario");
			file.renameTo(new File(diretorioTemporario,fileName));

			return ok(views.html.upload.render("Arquivo  \"" + fileName + "\" do tipo [" + contentType + "] foi carregado com sucesso !"));

		} else {
			flash("error","Erro ao fazer upload");
			return redirect(routes.Application.index());
		}
	}

}
