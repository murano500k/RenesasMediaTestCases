package android.mediastress.renesas;

import android.app.Instrumentation;
import android.media.MediaPlayer;
import android.os.Looper;
import android.os.SystemClock;
import android.renesas.cts.MainActivity;
import android.util.Log;

import java.util.Random;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by artem on 11/17/16.
 */
public class MediaStressTestCts extends BaseTest {


	private static String TAG = "MediaTestCts";
	private static MediaPlayer mMediaPlayer;
	private static MainActivity activity;
	private MediaPlayer.OnPreparedListener mOnPreparedListener;
	private static int WAIT_FOR_COMMAND_TO_COMPLETE = 60000;  //1 min max.
	private static boolean mInitialized = false;
	private static boolean mPrepareReset = false;
	private static Looper mLooper = null;
	private static final Object mLock = new Object();
	private static final Object mPrepareDone = new Object();
	private static final Object mVideoSizeChanged = new Object();
	private static final Object mOnCompletion = new Object();
	private static boolean mOnPrepareSuccess = false;
	private static final long PAUSE_WAIT_TIME = 3000;
	private static final long WAIT_TIME = 2000;
	private static final int SEEK_TIME = 10000;
	public static boolean mOnCompleteSuccess = false;
	public static boolean mPlaybackError = false;
	public static int mMediaInfoUnknownCount = 0;
	public static int mMediaInfoVideoTrackLaggingCount = 0;
	public static int mMediaInfoBadInterleavingCount = 0;
	public static int mMediaInfoNotSeekableCount = 0;
	public static int mMediaInfoMetdataUpdateCount = 0;
	private static int seekTo,duration;
	private static int seekToIteration;
	private static boolean finished;


	private static final int NUMBER_OF_RECORDER_RANDOM_ACTIONS = 100000;
	private static final int NUMBER_OF_PLAYER_RANDOM_ACTIONS   = 100000;
	private volatile boolean mMediaServerDied;
	private volatile int mAction;
	private volatile int mParam;


	public MediaStressTestCts(Instrumentation inst, boolean isVideo, String testFileName) {
		TAG = "MediaTest";
		initTestWithFile(inst,isVideo ? BASE_PATH_VIDEO : BASE_PATH_AUDIO, testFileName);
	}

	public MediaStressTestCts(Instrumentation inst, String msgPrefix, boolean isVideo) {
		TAG = "MediaTest";

		initRandom(inst, msgPrefix, isVideo ? BASE_PATH_VIDEO : BASE_PATH_AUDIO);
	}


	@Override
	public void run() {
		testMedia();
	}

	public void testMedia() {
		try{
			testPlayerRandomAction(fileName);
		}catch (Exception e){
			Log.e(TAG, "testPlayerRandomAction got exception");
		}
		inst.waitForIdleSync();
	}
	public void testPlayerRandomAction(String fileName) throws Exception {


		activity = launchMainActivity();
		mOnCompleteSuccess = false;
		mMediaInfoUnknownCount = 0;
		mMediaInfoVideoTrackLaggingCount = 0;
		mMediaInfoBadInterleavingCount = 0;
		mMediaInfoNotSeekableCount = 0;
		mMediaInfoMetdataUpdateCount = 0;
		mPlaybackError = false;
		initializeMessageLooper();
		synchronized (mLock) {
			try {
				mLock.wait(WAIT_FOR_COMMAND_TO_COMPLETE);
			} catch (Exception e) {
				fail(ASSERT_MESSAGE_PREFIX+"looper was interrupted.");
			}
		}
		Watchdog watchdog = new Watchdog(5000);
		try {
			mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					if (mMediaPlayer == mp &&
							what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
						Log.e(TAG, "mediaserver process died");
						mMediaServerDied = true;
					}
					return true;
				}
			});
			mMediaPlayer.setDataSource(fileName);
			mMediaPlayer.setDisplay(activity.getSurfaceView().getHolder());
			mMediaPlayer.prepare();
			Log.w(TAG, fileName +" getDuration: "+mMediaPlayer.getDuration()+" ms");
			mMediaPlayer.start();
			long seed = System.currentTimeMillis();
			Log.v(TAG, "seed = " + seed);
			Random r = new Random(seed);
			watchdog.start();
			for (int i = 0; i < NUMBER_OF_PLAYER_RANDOM_ACTIONS; i++){
				watchdog.ping();
				assertTrue(!mMediaServerDied);
				mAction = (int)(r.nextInt() % 12);
				mParam = (int)(r.nextInt() % 1000000);
				try {
					switch (mAction) {
						case 0:
							mMediaPlayer.getCurrentPosition();
							break;
						case 1:
							mMediaPlayer.getDuration();
							break;
						case 2:
							mMediaPlayer.getVideoHeight();
							break;
						case 3:
							mMediaPlayer.getVideoWidth();
							break;
						case 4:
							mMediaPlayer.isPlaying();
							break;
						case 5:
							mMediaPlayer.pause();
							break;
						case 6:
							// Don't add mMediaPlayer.prepare() call here for two reasons:
							// 1. calling prepare() is a bad idea since it is a blocking call, and
							// 2. when prepare() is in progress, mediaserver died message will not be sent to apps
							mMediaPlayer.prepareAsync();
							break;
						case 7:
							mMediaPlayer.seekTo((int)(mParam));
							break;
						case 8:
							mMediaPlayer.setLooping(mParam % 2 == 0);
							break;
						case 9:
							mMediaPlayer.setVolume((mParam % 1000) / 500.0f,
									(mParam / 1000) / 500.0f);
							break;
						case 10:
							mMediaPlayer.start();
							break;
						case 11:
							Thread.sleep(mParam % 20);
							break;
					}
				} catch (Exception e) {
					if(mMediaServerDied)Log.e(TAG, "testPlayerRandomAction: ",e );
				}
			}
			mMediaPlayer.stop();
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			watchdog.end();
			watchdog.join();
		}
	}
	class Watchdog extends Thread {
		private final long mTimeoutMs;
		private boolean mWatchdogStop;
		private boolean mWatchdogPinged;
		public Watchdog(long timeoutMs) {
			mTimeoutMs = timeoutMs;
			mWatchdogStop = false;
			mWatchdogPinged = false;
		}
		public synchronized void run() {
			while (true) {
				// avoid early termination by "spurious" waitup.
				final long startTimeMs = System.currentTimeMillis();
				long remainingWaitTimeMs = mTimeoutMs;
				do {
					try {
						wait(remainingWaitTimeMs);
					} catch (InterruptedException ex) {
						// ignore.
					}
					remainingWaitTimeMs = mTimeoutMs - (System.currentTimeMillis() - startTimeMs);
				} while (remainingWaitTimeMs > 0);
				if (mWatchdogStop) {
					break;
				}
				if (!mWatchdogPinged) {
					fail("Action " + mAction + " Param " + mParam
							+ " waited over " + (mTimeoutMs - remainingWaitTimeMs) + " ms");
					return;
				}
				mWatchdogPinged = false;
			}
		}
		public synchronized void ping() {
			mWatchdogPinged = true;
			this.notify();
		}
		public synchronized void end() {
			mWatchdogStop = true;
			this.notify();
		}
	}

	public void playMediaSample(String fileName) {
		activity = launchMainActivity();
		mOnCompleteSuccess = false;
		mMediaInfoUnknownCount = 0;
		mMediaInfoVideoTrackLaggingCount = 0;
		mMediaInfoBadInterleavingCount = 0;
		mMediaInfoNotSeekableCount = 0;
		mMediaInfoMetdataUpdateCount = 0;
		mPlaybackError = false;
		initializeMessageLooper();
		synchronized (mLock) {
			try {
				mLock.wait(WAIT_FOR_COMMAND_TO_COMPLETE);
			} catch (Exception e) {
				fail(ASSERT_MESSAGE_PREFIX+"looper was interrupted.");
			}
		}
		try {
			mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mOnErrorListener);
			mMediaPlayer.setOnInfoListener(mInfoListener);
			Log.w(TAG, "playMediaSample: sample file name " + fileName);
			setTitle(activity, ASSERT_MESSAGE_PREFIX);
			//mMediaPlayer.setDataSource("/sdcard/test/rvideo/AV_000869_H264_High_Profile_level_31_VGA_30fps_4Mbps_AAC_48kHz_128kbps_stereo.mp4");
			//mMediaPlayer.setDataSource("/sdcard/test/rvideo/AV_000022_SF145.mp4");
			//mMediaPlayer.setDataSource("/sdcard/test/rvideo/AV_001187_Toy_Story3Official_Trailer_in_720p_h264_MP_L2_1280x720_24fps_1Mbps_eAACplus_64kbps_44100Hz.mp4");


			mMediaPlayer.setDataSource(fileName);
			mMediaPlayer.setDisplay(activity.getSurfaceView().getHolder());
			mMediaPlayer.prepare();
			duration = mMediaPlayer.getDuration();
			Log.w(TAG, "duration of media " + duration);
			seekToIteration = 0;
			mMediaPlayer.start();
			SystemClock.sleep(2000);
			seekToRandom(duration);
			finished=false;
			inst.waitForIdle(new Runnable() {
				@Override
				public void run() {
					while(!finished){
						SystemClock.sleep(1000);
						assertNotNull(ASSERT_MESSAGE_PREFIX,mMediaPlayer);
					}
					waitToComplete(duration);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			fail(ASSERT_MESSAGE_PREFIX + e.getMessage());
		}
	}

		public static void waitToComplete(int duration){
			int waittime = 0;
			mMediaPlayer.start();
			SystemClock.sleep(3000);
			int pos =mMediaPlayer.getCurrentPosition();
			waittime = duration - pos;
			Log.w(TAG, "duration of media " + duration);
			Log.w(TAG, "pos+waittime of media " + (pos+ waittime));
			Log.w(TAG, "mMediaPlayer.getCurrentPosition() " + pos);
			Log.w(TAG, "waittime of media " + waittime);
			//printCpuInfo();


			synchronized (mOnCompletion) {
				try {
					mOnCompletion.wait(waittime + 3000);
				} catch (Exception e) {
					Log.w(TAG, "playMediaSamples are interrupted");
					fail("playMediaSamples are interrupted");
				}
			}
			if (!mOnCompleteSuccess && !mPlaybackError) {
				Log.e(TAG, "wait timed-out without onCompletion notification ");
				Log.e(TAG, "mOnCompleteSuccess "+mOnCompleteSuccess);
				Log.e(TAG, "mPlaybackError "+mPlaybackError);
			}
			terminateMessageLooper();

			assertTrue(mOnCompleteSuccess);
			//if(activity!=null)activity.finish();
		}

	public static void seekToRandom(int duration) {
		int random = new Random().nextInt((duration /100))-(duration /200);
		int random2 = new Random().nextInt(9)+1;
		seekTo =(duration * random2 / 10) +random;
		if(duration-seekTo<6000) {
			Log.d(TAG, "seekToRandom: randomPosition before end" );
			seekToRandom(duration);
			return;
		}
		Log.w(TAG, "seekToRandom: "+seekTo/1000+" seconds");

		mMediaPlayer.pause();
		mMediaPlayer.seekTo(seekTo);

	}
	public static void checkPositionValid(int duration, int seekTo){
		activity.showToast(seekToIteration+". seekToRandom: "+seekTo/1000+" seconds");

		SystemClock.sleep(2000);
		assertTrue("seekTo not Playing",mMediaPlayer.isPlaying());
		int pos=mMediaPlayer.getCurrentPosition();

		int delta = pos-(seekTo+2000);
		Log.v("DELTA seek", "seekTo(" +seekTo+") current position ("+pos+")");
		Log.w("DELTA seek", "delta = " +delta);
		//assertTrue("SEEKTO",delta<2000 && delta > -2000);
	}



	static MediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
		@Override
		public void onSeekComplete(MediaPlayer mediaPlayer) {
			seekToIteration++;
			mMediaPlayer.start();

			Log.w(TAG, "onSeekComplete: "+seekToIteration);
			if(seekToIteration<5){
				checkPositionValid(duration, seekTo);
				seekToRandom(duration);
			}else if(seekToIteration==5) {
				if(duration/10<5000) seekTo=duration * 700 / 1000;
				else seekTo=duration * 900 / 1000;
				mMediaPlayer.pause();
				mMediaPlayer.seekTo(seekTo);
			}else {
				SystemClock.sleep(2000);
				finished=true;
				//waitToComplete(duration);
			}



		}
	};
	static MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			synchronized (mPrepareDone) {
				if (mPrepareReset) {
					Log.v(TAG, "call Reset");
					mMediaPlayer.reset();
				}
				Log.v(TAG, "notify the prepare callback");
				mPrepareDone.notify();
				mOnPrepareSuccess = true;
			}
		}
	};
	static MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			//SystemClock.sleep(1000);
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/

			int pos=mp.getCurrentPosition();
			int delta = pos-duration;
			Log.w("DELTA OnComplet", "duration(" +duration+") current position ("+pos+")");
			Log.w("DELTA OnComplet", "delta = " +delta);
			synchronized (mOnCompletion) {
				Log.w(TAG, "notify the completion callback");
				mOnCompleteSuccess = true;
				mOnCompletion.notify();
			}
		}
	};
	static MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			Log.e(TAG, "playback error");
			mPlaybackError = true;
			mp.reset();
			synchronized (mOnCompletion) {
				Log.w(TAG, "notify the completion callback");
				mOnCompletion.notify();
				mOnCompleteSuccess = false;
			}
			return true;
		}
	};
	static MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			switch (what) {
				case MediaPlayer.MEDIA_INFO_UNKNOWN:
					mMediaInfoUnknownCount++;
					break;
				case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
					mMediaInfoVideoTrackLaggingCount++;
					break;
				case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
					mMediaInfoBadInterleavingCount++;
					break;
				case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
					mMediaInfoNotSeekableCount++;
					break;
				case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
					mMediaInfoMetdataUpdateCount++;
					break;
			}
			Log.e(TAG, "onInfo: "+what );
			return true;
		}
	};

	/*
     * Initializes the message looper so that the mediaPlayer object can
     * receive the callback messages.
     */
	private static void initializeMessageLooper() {
		Log.v(TAG, "start looper");
		new Thread() {
			@Override
			public void run() {
				// Set up a looper to be used by camera.
				Looper.prepare();
				Log.v(TAG, "start loopRun");
				// Save the looper so that we can terminate this thread
				// after we are done with it.
				mLooper = Looper.myLooper();
				mMediaPlayer = new MediaPlayer();
				synchronized (mLock) {
					mInitialized = true;
					mLock.notify();
				}
				Looper.loop();  // Blocks forever until Looper.quit() is called.
				Log.v(TAG, "initializeMessageLooper: quit.");
			}
		}.start();
	}

	/*
	 * Terminates the message looper thread.
	 */
	private static void terminateMessageLooper() {
		finished=true;

		mLooper.quit();
		mMediaPlayer.release();
	}
}