package com.sudoplz.reactnativeamplitudeanalytics;

import com.amplitude.api.Amplitude;
import com.amplitude.api.Constants;
import com.amplitude.api.Identify;

import android.app.Activity;
import android.app.Application;

import com.amplitude.api.Revenue;
import com.amplitude.api.TrackingOptions;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;

import org.json.JSONException;
import org.json.JSONObject;

public class RNAmplitudeSDK extends ReactContextBaseJavaModule {

  private Activity mActivity = null;
  private Application mApplication = null;

  public RNAmplitudeSDK(ReactApplicationContext reactContext, Application mApplication) {
    super(reactContext);
    this.mActivity = getCurrentActivity();
    this.mApplication = mApplication;
  }

  @Override
  public String getName() {
    return "RNAmplitudeSDK";
  }

  @ReactMethod
  public void initialize(String apiKey, Boolean trackSessionEvents) {
    Amplitude.getInstance().initialize(getReactApplicationContext(), apiKey)
            .enableForegroundTracking(this.mApplication);

    trackSessionEvents(trackSessionEvents);
  }

  @ReactMethod
  public void trackSessionEvents(Boolean track){
    Amplitude.getInstance().trackSessionEvents(track);
  }

  private boolean shouldDisable(ReadableMap options, String key) {
    return options.hasKey(key) && !options.getBoolean(key);
  }

  @ReactMethod
  public void setTrackingOptions(ReadableMap options) {
    TrackingOptions trackingOptions = new TrackingOptions();

    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_ADID)) {
      trackingOptions = trackingOptions.disableAdid();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_CARRIER)) {
      trackingOptions = trackingOptions.disableCarrier();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_CITY)) {
      trackingOptions = trackingOptions.disableCity();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_COUNTRY)) {
      trackingOptions = trackingOptions.disableCountry();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_DEVICE_BRAND)) {
      trackingOptions = trackingOptions.disableDeviceBrand();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_DEVICE_MANUFACTURER)) {
      trackingOptions = trackingOptions.disableDeviceManufacturer();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_DEVICE_MODEL)) {
      trackingOptions = trackingOptions.disableDeviceModel();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_DMA)) {
      trackingOptions = trackingOptions.disableDma();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_IP_ADDRESS)) {
      trackingOptions = trackingOptions.disableIpAddress();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_LANGUAGE)) {
      trackingOptions = trackingOptions.disableLanguage();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_LAT_LNG)) {
      trackingOptions = trackingOptions.disableLatLng();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_OS_NAME)) {
      trackingOptions = trackingOptions.disableOsName();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_OS_VERSION)) {
      trackingOptions = trackingOptions.disableOsVersion();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_PLATFORM)) {
      trackingOptions = trackingOptions.disablePlatform();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_REGION)) {
      trackingOptions = trackingOptions.disableRegion();
    }
    if(shouldDisable(options, Constants.AMP_TRACKING_OPTION_VERSION_NAME)) {
      trackingOptions = trackingOptions.disableVersionName();
    }

    Amplitude.getInstance().setTrackingOptions(trackingOptions);
  }

  @ReactMethod
  public void enableLocationListening() {
    Amplitude.getInstance().enableLocationListening();
  }

  @ReactMethod
  public void disableLocationListening() {
    Amplitude.getInstance().disableLocationListening();
  }

  @ReactMethod
  public void useAdvertisingIdForDeviceId() {
    Amplitude.getInstance().useAdvertisingIdForDeviceId();
  }

  @ReactMethod
  public void setOffline(Boolean offline) {
    Amplitude.getInstance().setOffline(offline);
  }

  @ReactMethod
  public void setUserId(String id) {
    Amplitude.getInstance().setUserId(id);
  }

  @ReactMethod
  public void getUserId(Promise promise) {
    promise.resolve(Amplitude.getInstance().getUserId());
  }

  @ReactMethod
  public void setUserProperties(ReadableMap properties) {
    try {
      JSONObject jProperties = convertReadableToJsonObject(properties);
      Amplitude.getInstance().setUserProperties(jProperties);
    } catch (JSONException ignored) { }
  }

  @ReactMethod
  public void setOptOut(Boolean optOut) {
    Amplitude.getInstance().setOptOut(optOut);
  }

  @ReactMethod
  public void isOptedOut(Promise promise) {
    promise.resolve(Amplitude.getInstance().isOptedOut());
  }

  @ReactMethod
  public void clearUserProperties() {
    Amplitude.getInstance().clearUserProperties();
  }

  @ReactMethod
  public void regenerateDeviceId() {
    Amplitude.getInstance().regenerateDeviceId();
  }

  @ReactMethod
  public void getDeviceId(Promise promise) {
    promise.resolve(Amplitude.getInstance().getDeviceId());
  }

  @ReactMethod
  public void getSessionId(Promise promise) {
    promise.resolve(Amplitude.getInstance().getSessionId());
  }

  @ReactMethod
  public void logEvent(String eventType, ReadableMap eventPropertiesMap, ReadableMap optionsMap) {
    JSONObject eventProperties = null;
    if(eventPropertiesMap != null) {
      try {
        eventProperties = convertReadableToJsonObject(eventPropertiesMap);
      } catch (JSONException ignored) { }
    }

    JSONObject groups = null;
    long timestamp = System.currentTimeMillis();
    boolean outOfSession = false;
    if(optionsMap != null) {
      if(optionsMap.hasKey("groups")) {
        try {
          groups = convertReadableToJsonObject(optionsMap.getMap("groups"));
        } catch (JSONException ignored) { }
      }

      if(optionsMap.hasKey("timestamp")) {
        timestamp = (long) optionsMap.getDouble("timestamp");
      }

      if(optionsMap.hasKey("outOfSession")) {
        outOfSession = optionsMap.getBoolean("outOfSession");
      }
    }

    Amplitude.getInstance()
            .logEvent(eventType, eventProperties, groups, timestamp, outOfSession);
  }

  @ReactMethod
  public void logRevenue(String productIdentifier, int quantity, double price,
                         String revenueType,
                         String receipt, String receiptSignature,
                         ReadableMap eventProperties) {
    Revenue revenue = new Revenue()
            .setProductId(productIdentifier)
            .setQuantity(quantity)
            .setPrice(price)
            .setRevenueType(revenueType)
            .setReceipt(receipt, receiptSignature);

    try {
      JSONObject jProperties = convertReadableToJsonObject(eventProperties);
      revenue = revenue.setEventProperties(jProperties);
    } catch (JSONException ignored) { }

    Amplitude.getInstance().logRevenueV2(revenue);
  }

  @ReactMethod
  public void setGroup(String groupType, String groupName) {
    Amplitude.getInstance().setGroup(groupType, groupName);
  }

  @ReactMethod
  public void addToUserProperty(String property, int value) {
    Identify identify = new Identify().add(property, value);
    Amplitude.getInstance().identify(identify);
  }

  @ReactMethod
  public void setUserPropertyOnce(String property, String value) {
    Identify identify = new Identify().setOnce(property, value);
    Amplitude.getInstance().identify(identify);
  }

  private static JSONObject convertReadableToJsonObject(ReadableMap map) throws JSONException{
    JSONObject jsonObj = new JSONObject();
    ReadableMapKeySetIterator it = map.keySetIterator();

    while (it.hasNextKey()) {
      String key = it.nextKey();
      ReadableType type = map.getType(key);
      switch (type) {
        case Map:
          jsonObj.put(key, convertReadableToJsonObject(map.getMap(key)));
          break;
        case String:
          jsonObj.put(key, map.getString(key));
          break;
        case Number:
          jsonObj.put(key, map.getDouble(key));
          break;
        case Boolean:
          jsonObj.put(key, map.getBoolean(key));
          break;
        case Array:
          jsonObj.put(key, map.getArray(key));
          break;
        case Null:
          jsonObj.put(key, null);
          break;
      }
    }
    return jsonObj;
  }
}
