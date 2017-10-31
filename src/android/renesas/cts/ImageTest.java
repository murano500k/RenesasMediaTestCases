package android.renesas.cts;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by artem on 11/17/16.
 */
@RunWith(AndroidJUnit4.class)
public class ImageTest extends BaseMediaTest{

	@Test
	public void testJpegI000287(){
		testImage(jpgI000287);
	}
	@Test
	public void testJpegI000309(){
		testImage(jpgI000309);
	}
	@Test
	public void testJpegI000310(){
		testImage(jpgI000310);
	}
	@Test
	public void testJpegI000311(){
		testImage(jpgI000311);
	}
	@Test
	public void testJpegI000001(){
		testImage(jpgI000001);
	}
	@Test
	public void testPngI000301(){
		testImage(pngI000301);
	}
	@Test
	public void testPngI000313(){
		testImage(pngI000313);
	}
	@Test
	public void testWebpI000308(){
		testImage(webpI000308);
	}
	@Test
	public void testGifI000021(){
		testImage(gifI000021);
	}
	@Test
	public void testBmpI000312(){
		testImage(bmpI000312);
	}
}
