package android.renesas.cts;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by artem on 12/2/16.
 */
@RunWith(AndroidJUnit4.class)
public class VideoRandomActionTest extends BaseMediaTest {
	@Before
	public void prepare(){
		isVideo=true;
	}

	@Test
	public void testH263AV001248(){
		testMediaRandomAction(h263AV001248);
	}

	@Test
	public void testH264AV000869(){
		testMediaRandomAction(h264AV000869);
	}

	@Test
	public void testH264AV001097(){
		testMediaRandomAction(h264AV001097);
	}

	@Test
	public void testH264AV001187(){
		testMediaRandomAction(h264AV001187);
	}

	@Test
	public void testH265AV001429(){
		testMediaRandomAction(h265AV001429);
	}

	@Test
	public void testH265AV001430(){
		testMediaRandomAction(h265AV001430);
	}

	@Test
	public void testH265AV001431(){
		testMediaRandomAction(h265AV001431);
	}

	@Test
	public void testMPEG4AV000022(){
		testMediaRandomAction(mpeg4AV000022);
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
