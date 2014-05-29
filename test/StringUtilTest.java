import org.junit.Assert;
import org.junit.Test;

import util.StringUtil;

public class StringUtilTest {

	@Test
	public void testWithDate() {
		Assert.assertTrue(StringUtil.hasDateInfo("20/06/2011 00:19:37"));
		Assert.assertTrue(StringUtil.hasDateInfo("01-04-2011 00:19:37"));
	}

	@Test
	public void testWithoutADate() {
		Assert.assertFalse(StringUtil.hasDateInfo("20062011 0019:37"));
		Assert.assertFalse(StringUtil.hasDateInfo("20-06-2011 0019:37"));
	}

	@Test
	public void testEmptyDate() {
		Assert.assertFalse(StringUtil.hasDateInfo(null));
		Assert.assertFalse(StringUtil.hasDateInfo(""));
	}

	@Test
	public void testBlankDate() {
		Assert.assertFalse(StringUtil.hasDateInfo("                                    "));
	}

}
