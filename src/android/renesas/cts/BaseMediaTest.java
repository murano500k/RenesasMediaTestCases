package android.renesas.cts;

import android.app.Instrumentation;
import android.mediastress.renesas.BaseTest;
import android.mediastress.renesas.ImageTestCts;
import android.mediastress.renesas.MediaStressTestCts;
import android.mediastress.renesas.MediaTestCts;
import android.os.ParcelFileDescriptor;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME;

/**
 * Created by artem on 12/2/16.
 */

public class BaseMediaTest extends TestCase {

        String webmAV000962 = "AV_000962_big_buck_bunny_vga_vp8_25fps_vorbis.webm";
        String h263AV001248 =
                "AV_001248_lunarsurface_H263_CIF_2mbps_30fps_NB_AMR.mp4";
        String h264AV000869 =
                "AV_000869_H264_High_Profile_level_31_VGA_30fps_4Mbps_AAC_48kHz_128kbps_stereo.mp4";
        String h264AV001097 =
                "AV_001097_1080p_crowdrun_HP_cabac_2B_wBpred_adct_30fps_20mbps_1000fr_AAC_HE_48kHz_64kbps_stereo.mp4";
        String h264AV001187 =
                "AV_001187_Toy_Story3Official_Trailer_in_720p_h264_MP_L2_1280x720_24fps_1Mbps_eAACplus_64kbps_44100Hz.mp4";
        String h265AV001429 =
                "AV_001429_life_in_orbit_720p_main_2.mp4";
        String h265AV001430 =
                "AV_001430_iss_tour_720p_main_3.mp4";
        String h265AV001431 =
                "AV_001431_arthur_christmas_720p_psnr_main_22.mp4";
        String mpeg2AV000327 =
                "AV_000327_MPEG2_Main_Profile_Main_Level_D1_PAL_25fps_9.8Mbps_AAC_48khz_128kbps_Stereo.mp4";
        String mpeg2AV000328 =
                "AV_000328_MPEG2_Simple_Profile_Main_Level_QVGA_24fps_384kbps_WBAMR_23.85kbps.mp4";
        String mpeg2AV000472 =
                "AV_000472_MPEG2_QVGA_30fps_2Mbps_Main_Low_AMR_NB_12_2kbps.mp4";
        String mpeg2AV000474 =
                "AV_000474_MPEG2_QVGA_2Mbps_30fps_AAC_128kbps_48KHz.mp4";
        String mpeg2AV001235 =
                "AV_001235_nasa_sts131_landing_mpeg2_mp_ml_25fps_bf2_aac_hev2.mp4";
        String mpeg2AV001238 =
                "AV_001238_nasa_greenflight_mpeg2_mp_ll_30fps_bf2_aac_hev2.mp4";
        String mpeg2AV001239 =
                "AV_001239_launch_mpeg2_sp_ml_24fps_bf0_nbamr.mp4";

        String mpeg4AV000022 =
                "AV_000022_SF145.mp4";

        String mp3A000001 =
                "A_000001_02_Tetanus.mp3";
        String wavA000354 =
		            "A_000354_PCM_16bit_48Khz_1536kbps_stereo.wav";

		    String flacA000484 =
				    "A_000484_SpoonRevenge.flac";
        String sample3gpA000123 =
		        "A_000123_15dot85kbps_ex30.3gp";
        String aacA000157 =
		        "A_000157_128kbps_werk32.aac";
        String amrA000443 =
		        "A_000443_NB_AMR_8000Hz_10.2kbps.AMR";
        String mp4A000454 =
		        "A_000454_eAACplus_48000Hz_32Kbps_Stereo_track1.mp4";
        String oggA000480 =
		        "A_000480_The_Abyss-4T.ogg";
        String mkaA000485 =
		        "A_000485_ehren-paper_lights.mka";








	String jpgI000287 =
			"I_000287_benchmark_lightbox_5336x3000_16MP.jpg";

	String jpgI000309 =
			"I_000309_roskilde_station.jpg";

	String jpgI000310 =
			"I_000310_kettbach_dulmen.jpg";

	String jpgI000311 =
			"I_000311_konnu_suursoo.jpg";

	String jpgI000001 =
			"I_000001_Birdcatcher_with_jockey_up.jpg";

	String webpI000308 =
			"I_000308_green_mountain.webp";

	String gifI000021 =
			"I_000021_GPN_001040.gif";

	String bmpI000312 =
			"I_000312_moon_landscape.bmp";

	String pngI000301=
			"I_000301_1922_world_map_34995x2374.png";

	String pngI000313 =
			"I_000313_niagara_falls.png";



	boolean isVideo;

	public String ASSERT_MESSAGE_PREFIX;

	private static final String TAG = "RandomTest";

	Instrumentation inst;
	BaseTest mediaTest;


	public void setAssertTestPrefix(String test_type) {
		ASSERT_MESSAGE_PREFIX = test_type + " ";
	}

	public void goHome() {
		assertTrue(ASSERT_MESSAGE_PREFIX + "test finished: "
				, inst.getUiAutomation().performGlobalAction(GLOBAL_ACTION_HOME));
	}

	public void checkPermPM() {
		String currentPackage = inst.getTargetContext().getPackageName();
		ParcelFileDescriptor fileDescriptor = inst.getUiAutomation()
				.executeShellCommand("pm grant "+currentPackage+" android.permission.READ_EXTERNAL_STORAGE");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(fileDescriptor.getFileDescriptor())));
		String line, res = "";
		try {
			while ((line = reader.readLine()) != null) {
				res += line;
			}
		} catch (IOException e) {
			fail(ASSERT_MESSAGE_PREFIX + e.getMessage());
		}
		Log.d(TAG, "checkPermPM: " + res);
	}
	public void testMedia(String fileName){
		inst = InstrumentationRegistry.getInstrumentation();
		checkPermPM();

		setAssertTestPrefix(isVideo ? "TEST_VIDEO" : "TEST_AUDIO");
		mediaTest=new MediaTestCts(inst, isVideo,fileName);
		assertNotNull(ASSERT_MESSAGE_PREFIX, mediaTest);
		mediaTest.run();
	}
	public void testMediaRandomAction(String fileName){
		inst = InstrumentationRegistry.getInstrumentation();
		checkPermPM();

		setAssertTestPrefix(isVideo ? "TEST_VIDEO" : "TEST_AUDIO");
		mediaTest=new MediaStressTestCts(inst, isVideo,fileName);
		assertNotNull(ASSERT_MESSAGE_PREFIX, mediaTest);
		mediaTest.run();
	}

	public void testMediaRandom(String fileName){
		inst = InstrumentationRegistry.getInstrumentation();
		checkPermPM();

		setAssertTestPrefix(isVideo ? "TEST_VIDEO" : "TEST_AUDIO");
		mediaTest=new MediaStressTestCts(inst, isVideo,fileName);
		assertNotNull(ASSERT_MESSAGE_PREFIX, mediaTest);
		mediaTest.run();
	}
	public void testImage(String fileName){
		inst = InstrumentationRegistry.getInstrumentation();
		checkPermPM();
		setAssertTestPrefix("TEST_IMAGE");
		mediaTest=new ImageTestCts(inst,fileName);
		assertNotNull(ASSERT_MESSAGE_PREFIX, mediaTest);
		mediaTest.run();
	}
}
