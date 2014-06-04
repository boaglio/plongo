import java.io.File;
import java.util.List;

import models.Log;

import org.junit.Assert;
import org.junit.Test;

import util.FileUtil;

public class FileUtilTest {

	@Test
	public void testConvertFileToLog() {
		File f = new File("/tmp/test.log");
		List<Log> list = FileUtil.convertFileToLogList(f);

		Assert.assertTrue(list != null);
		Assert.assertTrue(list.size() > 0);

		for (Log line : list) {
			System.out.println(line);
		}
		System.out.println(list.size());

	}
}
