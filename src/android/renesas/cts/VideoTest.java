package android.renesas.cts;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by artem on 12/2/16.
 */
@RunWith(AndroidJUnit4.class)
public class VideoTest extends BaseMediaTest {
	@Before
	public void prepare(){
		isVideo=true;
	}

	@Test
	public void testH263_1(){
		testMedia(h263_1);
	}

	@Test
	public void testH264_1(){
		testMedia(h264_1);
	}

	@Test
	public void testH264_2(){
		testMedia(h264_2);
	}

	@Test
	public void testH264_3(){
		testMedia(h264_3);
	}

	@Test
	public void testH265_1(){
		testMedia(h265_1);
	}

	@Test
	public void testH265_2(){
		testMedia(h265_2);
	}

	@Test
	public void testH265_3(){
		testMedia(h265_3);
	}

	@Test
	public void testMPEG4_1(){
		testMedia(mpeg4_1);
	}
/*

	@Test
	public void testWEBM1(){

		testMedia(webm1);
	}

	@Test
	public void testMPEG2_1(){
		testMedia(mpeg2_1);
	}

	@Test
	public void testMPEG2_2(){
		testMedia(mpeg2_2);
	}

	@Test
	public void testMPEG2_3(){
		testMedia(mpeg2_3);
	}

	@Test
	public void testMPEG2_4(){
		testMedia(mpeg2_4);
	}

	@Test
	public void testMPEG2_5(){
		testMedia(mpeg2_5);
	}

	@Test
	public void testMPEG2_6(){
		testMedia(mpeg2_6);
	}

	@Test
	public void testMPEG2_7(){
		testMedia(mpeg2_7);
	}
*/





}
