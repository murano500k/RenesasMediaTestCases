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
	public void testMp3A000001(){
		testMedia(mp3A000001);
	}
	@Test
	public void testFlacA000484(){
		testMedia(flacA000484);
	}
	@Test
	public void testWavA000354(){
		testMedia(wavA000354);
	}
	@Test
	public void testAacA000157(){
		testMedia(aacA000157);
	}
	@Test
	public void test3gpA000123(){
		testMedia(sample3gpA000123);
	}
	@Test
	public void testOggA000480(){
		testMedia(oggA000480);
	}
	@Test
	public void testMkaA000485(){
		testMedia(mkaA000485);
	}
	@Test
	public void testMp4A000454(){
		testMedia(mp4A000454);
	}
	@Test
	public void testAmrA000443(){
		testMedia(amrA000443);
	}




}
