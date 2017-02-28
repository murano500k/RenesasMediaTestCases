package android.renesas.cts;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by artem on 12/2/16.
 */
@RunWith(AndroidJUnit4.class)
public class AudioTest extends BaseMediaTest {
	@Before
	public void prepare(){
		isVideo=false;
	}

	@Test
	public void testMp3_1(){
		testMedia(mp3_1);
	}
	@Test
	public void testFlac1(){
		testMedia(flac1);
	}
	@Test
	public void testWav1(){
		testMedia(wav1);
	}
	@Test
	public void testAac1(){
		testMedia(aac1);
	}
	@Test
	public void test3gp1(){
		testMedia(sample3gp1);
	}
	@Test
	public void testOgg1(){
		testMedia(ogg1);
	}
	@Test
	public void testMka1(){
		testMedia(mka1);
	}
	@Test
	public void testMp4_1(){
		testMedia(mp4_1);
	}
	@Test
	public void testAMR1(){
		testMedia(amr1);
	}




}
