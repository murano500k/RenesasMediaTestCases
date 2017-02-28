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
	public void testJpeg1(){
		testImage(jpg1);
	}
	@Test
	public void testJpeg2(){
		testImage(jpg2);
	}
	@Test
	public void testJpeg3(){
		testImage(jpg3);
	}
	@Test
	public void testJpeg4(){
		testImage(jpg4);
	}
	@Test
	public void testJpeg5(){
		testImage(jpg5);
	}
	@Test
	public void testPng1(){
		testImage(png1);
	}
	@Test
	public void testPng2(){
		testImage(png2);
	}
	@Test
	public void testWebp1(){
		testImage(webp1);
	}
	@Test
	public void testGif1(){
		testImage(gif1);
	}
	@Test
	public void testBmp1(){
		testImage(bmp1);
	}
	@Test
	public void testBmp2(){
		testImage(bmp2);
	}
}
