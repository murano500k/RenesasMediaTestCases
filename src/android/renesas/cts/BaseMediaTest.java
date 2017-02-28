package android.renesas.cts;

import android.app.Instrumentation;
import android.mediastress.renesas.BaseTest;
import android.mediastress.renesas.ImageTestCts;
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

        String webm1 = "AV_000962_big_buck_bunny_vga_vp8_25fps_vorbis.webm";
        String h263_1 =
                "AV_001248_lunarsurface_H263_CIF_2mbps_30fps_NB_AMR.mp4";
        String h264_1 =
                "AV_000869_H264_High_Profile_level_31_VGA_30fps_4Mbps_AAC_48kHz_128kbps_stereo.mp4";
        String h264_2 =
                "AV_001097_1080p_crowdrun_HP_cabac_2B_wBpred_adct_30fps_20mbps_1000fr_AAC_HE_48kHz_64kbps_stereo.mp4";
        String h264_3 =
                "AV_001187_Toy_Story3Official_Trailer_in_720p_h264_MP_L2_1280x720_24fps_1Mbps_eAACplus_64kbps_44100Hz.mp4";
        String h265_1 =
                "AV_001429_life_in_orbit_720p_main_2.mp4";
        String h265_2 =
                "AV_001430_iss_tour_720p_main_3.mp4";
        String h265_3 =
                "AV_001431_arthur_christmas_720p_psnr_main_22.mp4";
        String mpeg2_1 =
                "AV_000327_MPEG2_Main_Profile_Main_Level_D1_PAL_25fps_9.8Mbps_AAC_48khz_128kbps_Stereo.mp4";
        String mpeg2_2 =
                "AV_000328_MPEG2_Simple_Profile_Main_Level_QVGA_24fps_384kbps_WBAMR_23.85kbps.mp4";
        String mpeg2_3 =
                "AV_000472_MPEG2_QVGA_30fps_2Mbps_Main_Low_AMR_NB_12_2kbps.mp4";
        String mpeg2_4 =
                "AV_000474_MPEG2_QVGA_2Mbps_30fps_AAC_128kbps_48KHz.mp4";
        String mpeg2_5 =
                "AV_001235_nasa_sts131_landing_mpeg2_mp_ml_25fps_bf2_aac_hev2.mp4";
        String mpeg2_6 =
                "AV_001238_nasa_greenflight_mpeg2_mp_ll_30fps_bf2_aac_hev2.mp4";
        String mpeg2_7 =
                "AV_001239_launch_mpeg2_sp_ml_24fps_bf0_nbamr.mp4";

        String mpeg4_1 =
                "AV_000022_SF145.mp4";

        String mp3_1 =
                "A_000001_02_Tetanus.mp3";
        String  wav1=
		        "A_000354_PCM_16bit_48Khz_1536kbps_stereo.wav";

		String flac1 =
				"A_000484_SpoonRevenge.flac";
        String sample3gp1 =
		        "A_000123_15dot85kbps_ex30.3gp";
        String aac1 =
		        "A_000157_128kbps_werk32.aac";
        String amr1 =
		        "A_000443_NB_AMR_8000Hz_10.2kbps.AMR";
        String mp4_1 =
		        "A_000454_eAACplus_48000Hz_32Kbps_Stereo_track1.mp4";
        String ogg1 =
		        "A_000480_The_Abyss-4T.ogg";
        String mka1 =
		        "A_000485_ehren-paper_lights.mka";








	String jpg1=
			"I_000287_benchmark_lightbox_5336x3000_16MP.jpg";

	String jpg2=
			"I_000309_roskilde_station.jpg";

	String jpg3=
			"I_000310_kettbach_dulmen.jpg";

	String jpg4=
			"I_000311_konnu_suursoo.jpg";

	String jpg5=
			"I_000001_Birdcatcher_with_jockey_up.jpg";

	String webp1=
			"I_000308_green_mountain.webp";

	String gif1=
			"I_000021_GPN_001040.gif";

	String bmp1=
			"I_000312_moon_landscape.bmp";

	String bmp2=
			"I_000304_Cevennes.bmp";

	String png1=
			"I_000301_1922_world_map_34995x2374.png";

	String png2=
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
	public void testImage(String fileName){
		inst = InstrumentationRegistry.getInstrumentation();
		checkPermPM();
		setAssertTestPrefix("TEST_IMAGE");
		mediaTest=new ImageTestCts(inst,fileName);
		assertNotNull(ASSERT_MESSAGE_PREFIX, mediaTest);
		mediaTest.run();
	}
}
