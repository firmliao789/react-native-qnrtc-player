package com.reactlibrary.qnrtcplayer;

import android.graphics.Color;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.qiniu.droid.rtplayer.QNConfiguration;
import com.qiniu.droid.rtplayer.QNError;
import com.qiniu.droid.rtplayer.QNLogLevel;
import com.qiniu.droid.rtplayer.QNRTPlayer;
import com.qiniu.droid.rtplayer.QNRTPlayerFactory;
import com.qiniu.droid.rtplayer.QNRTPlayerSetting;
import com.qiniu.droid.rtplayer.QNRTPlayerUrl;
import com.qiniu.droid.rtplayer.QNRenderMode;
import com.qiniu.droid.rtplayer.render.QNSurfaceView;
import com.reactlibrary.qnrtcplayer.constants.QNRTCPlayerEventConstants;

import org.webrtc.RendererCommon;

import java.util.Map;

public class QnrtPlayerViewManager extends SimpleViewManager<QNSurfaceView> implements LifecycleEventListener {
  public static final String REACT_CLASS = "QNRTPlayer";
  ReactApplicationContext mCallerContext;
  QNSurfaceView qnSurfaceView;
  QNRTPlayer mRTPlayer;
  QNRTPlayerUrl mRTUrl;
  RCTEventEmitter mEventEmitter;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  public QnrtPlayerViewManager(ReactApplicationContext reactContext) {
    mCallerContext = reactContext;
  }

  @Override
  protected QNSurfaceView createViewInstance(ThemedReactContext themedReactContext) {
    themedReactContext.addLifecycleEventListener(this);
    mEventEmitter = themedReactContext.getJSModule(RCTEventEmitter.class);
    initPlayer(themedReactContext);
    return qnSurfaceView;
  }

  @Override
  public void onHostResume() {
//    if (!mRTUrl.getURL().isEmpty() && !mRTPlayer.isPlaying()) {
//      mRTPlayer.playUrl(mRTUrl);
//    }
  }

  @Override
  public void onHostPause() {
//    if (mRTPlayer.isPlaying()) {
//      mRTPlayer.stopPlay();
//    }
  }

  @Override
  public void onHostDestroy() {
    mRTPlayer.releasePlayer();
  }

  private void initPlayer(ThemedReactContext themedReactContext) {
    qnSurfaceView = new QNSurfaceView(themedReactContext);
    qnSurfaceView.setKeepScreenOn(true);
    mRTPlayer = QNRTPlayerFactory.createQNRTPlayer(mCallerContext);
    mRTPlayer.initPlayer(new QNRTPlayerSetting());
    mRTPlayer.setSurfaceRenderWindow(qnSurfaceView);
    mRTPlayer.setEventListener(new QNRTPlayer.EventListener() {
      @Override
      public void onPlayerStateChanged(int i) {
        Log.d(REACT_CLASS, "onPlayerStateChanged:" + i);
        WritableMap map = Arguments.createMap();
        map.putInt("playerState", i);
        mEventEmitter.receiveEvent(qnSurfaceView.getId(), QNRTCPlayerEventConstants.ON_PLAYER_STATE_CHANGED, map);
      }

      @Override
      public void onPlayerInfo(int i, Object o) {
        Log.d(REACT_CLASS, "onPlayerInfo:" + i);
        WritableMap map = Arguments.createMap();
        map.putInt("type", i);
        mEventEmitter.receiveEvent(qnSurfaceView.getId(), QNRTCPlayerEventConstants.ON_PLAYER_INFO, Arguments.createMap());
      }

      @Override
      public void onPlayerError(QNError qnError) {
        Log.d(REACT_CLASS, "onPlayerError:" + qnError.mDescription);
        WritableMap map = Arguments.createMap();
        map.putString("message", qnError.mDescription);
        map.putInt("code", qnError.mCode);
        mEventEmitter.receiveEvent(qnSurfaceView.getId(), QNRTCPlayerEventConstants.ON_PLAYER_ERROR, Arguments.createMap());
      }
    });
    mRTUrl = new QNRTPlayerUrl();
  }

  @Override
  @Nullable
  public Map getExportedCustomDirectEventTypeConstants() {
    MapBuilder.Builder<String, Map<String, String>> builder = MapBuilder.builder();
    for (String event : QNRTCPlayerEventConstants.EVENT_LIST) {
      builder.put(event, MapBuilder.of("registrationName", event));
    }
    return builder.build();
  }

  @ReactProp(name = "source")
  public void setSrc(QNSurfaceView qnSurfaceView, @Nullable ReadableMap source) {
    mRTUrl.setURL(source.getString("uri"));
    mRTUrl.setHttpPost(source.getBoolean("mHttpPost"));
    mRTPlayer.playUrl(mRTUrl);
  }

  @ReactProp(name = "configuration")
  public void setQNConfiguration(QNSurfaceView qnSurfaceView, @Nullable ReadableMap configuration) {
    QNConfiguration qnConfiguration = new QNConfiguration();
    if (configuration.hasKey("CONF_PLAY_STAT"))
      qnConfiguration.setConfigure("CONF_PLAY_STAT", configuration.getInt("playConfStat"));
    if (configuration.hasKey("CONF_DEBUG_FILE"))
      qnConfiguration.setConfigure("CONF_DEBUG_FILE", configuration.getBoolean("confDebugFile"));
    mRTPlayer.configurePlayer(qnConfiguration);
  }

  @ReactProp(name = "setting")
  public void setQNRTPlayerSetting(QNSurfaceView qnSurfaceView, @Nullable ReadableMap setting) {
    QNRTPlayerSetting qnrtPlayerSetting = new QNRTPlayerSetting();
    if (setting.hasKey("mLogLevel")) {
      switch (setting.getInt("mLogLevel")) {
        case 0:
          qnrtPlayerSetting.setLogLevel(QNLogLevel.VERBOSE);
          break;
        case 1:
          qnrtPlayerSetting.setLogLevel(QNLogLevel.INFO);
          break;
        case 2:
          qnrtPlayerSetting.setLogLevel(QNLogLevel.WARNING);
          break;
        case 3:
          qnrtPlayerSetting.setLogLevel(QNLogLevel.ERROR);
          break;
        case 4:
          qnrtPlayerSetting.setLogLevel(QNLogLevel.NONE);
          break;
      }
    }
    mRTPlayer.initPlayer(qnrtPlayerSetting);
  }

}
