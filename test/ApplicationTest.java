import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

import java.util.ArrayList;
import java.util.List;

import models.SystemApplication;

import org.junit.Test;

import play.mvc.Content;

/**
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 */
public class ApplicationTest {

	@Test
	public void simpleCheck() {
		int a = 1 + 1;
		assertThat(a).isEqualTo(2);
	}

	@Test
	public void renderTemplate() {

		List<SystemApplication> apps = new ArrayList<SystemApplication>();
		SystemApplication sysApp = new SystemApplication();
		sysApp.id = "1";
		sysApp.name = "Test";
		apps.add(sysApp);

		Content html = views.html.index.render(apps);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("Test");
	}

}
