#import "AmplitudeSDK.h"

@implementation RNAmplitudeSDK

RCT_EXPORT_MODULE()

// initializes Amplitude
RCT_EXPORT_METHOD(initialize:(NSString* )writeKey setTrackSessionEvents:(BOOL) trackSessionEvents)
{
    [self trackSessionEvents: trackSessionEvents];
    [[Amplitude instance] initializeApiKey: writeKey];
}

RCT_EXPORT_METHOD(trackSessionEvents:(BOOL) track)
{
    [Amplitude instance].trackingSessionEvents = track;
}

+ (BOOL) shouldDisable:(NSDictionary *) options withKey:(NSString *) key
{
    id val = [options objectForKey: key];
    return val && ![val boolValue];
}

RCT_EXPORT_METHOD(setTrackingOptions:(NSDictionary *)options)
{
    AMPTrackingOptions *trackingOptions = [AMPTrackingOptions options];

    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_IDFA]) {
        trackingOptions = [trackingOptions disableIDFA];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_IDFV]) {
        trackingOptions = [trackingOptions disableIDFV];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_CARRIER]) {
        trackingOptions = [trackingOptions disableCarrier];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_CITY]) {
        trackingOptions = [trackingOptions disableCity];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_COUNTRY]) {
        trackingOptions = [trackingOptions disableCountry];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_DEVICE_MANUFACTURER]) {
        trackingOptions = [trackingOptions disableDeviceManufacturer];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_DEVICE_MODEL]) {
        trackingOptions = [trackingOptions disableDeviceModel];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_DMA]) {
        trackingOptions = [trackingOptions disableDMA];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_IP_ADDRESS]) {
        trackingOptions = [trackingOptions disableIPAddress];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_LANGUAGE]) {
        trackingOptions = [trackingOptions disableLanguage];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_LAT_LNG]) {
        trackingOptions = [trackingOptions disableLatLng];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_OS_NAME]) {
        trackingOptions = [trackingOptions disableOSName];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_OS_VERSION]) {
        trackingOptions = [trackingOptions disableOSVersion];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_PLATFORM]) {
        trackingOptions = [trackingOptions disablePlatform];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_REGION]) {
        trackingOptions = [trackingOptions disableRegion];
    }
    if ([RNAmplitudeSDK shouldDisable:options withKey:AMP_TRACKING_OPTION_VERSION_NAME]) {
        trackingOptions = [trackingOptions disableVersionName];
    }

    [[Amplitude instance] setTrackingOptions:trackingOptions];
}

RCT_EXPORT_METHOD(enableLocationListening)
{
    [[Amplitude instance] enableLocationListening];
}

RCT_EXPORT_METHOD(disableLocationListening)
{
    [[Amplitude instance] disableLocationListening];
}

RCT_EXPORT_METHOD(useAdvertisingIdForDeviceId)
{
    [[Amplitude instance] useAdvertisingIdForDeviceId];
}

RCT_EXPORT_METHOD(setOffline:(BOOL)offline)
{
    [[Amplitude instance] setOffline:offline];
}

RCT_EXPORT_METHOD(setUserId:(NSString *)userId)
{
    [[Amplitude instance] setUserId:userId];
}

RCT_EXPORT_METHOD(setUserProperties:(NSDictionary *)properties)
{
    [[Amplitude instance] setUserProperties:properties];
}

RCT_EXPORT_METHOD(setOptOut:(BOOL) optOut)
{
    [[Amplitude instance] setOptOut:optOut];
}

RCT_REMAP_METHOD(isOptedOut,
                 isOptedOutWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{

    NSNumber *optOut = [NSNumber numberWithBool:[Amplitude instance].optOut];
    resolve(optOut);
}

RCT_EXPORT_METHOD(clearUserProperties)
{
    [[Amplitude instance] clearUserProperties];
}

RCT_EXPORT_METHOD(regenerateDeviceId)
{
    [[Amplitude instance] regenerateDeviceId];
}

RCT_REMAP_METHOD(getDeviceId,
                 getDeviceIdWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve([[Amplitude instance] getDeviceId]);
}

RCT_REMAP_METHOD(getSessionId,
                 getSessionIdWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve([NSNumber numberWithLongLong:[[Amplitude instance] getSessionId]]);
}

RCT_EXPORT_METHOD(logEvent:(NSString *)eventType properties:(NSDictionary *)properties options:(NSDictionary *)options)
{
    [[Amplitude instance] logEvent:eventType
               withEventProperties:properties
                        withGroups:[options objectForKey:@"groups"]
                     withTimestamp:[options objectForKey:@"timestamp"]
                      outOfSession:[[options objectForKey:@"outOfSession"] boolValue]];
}

RCT_EXPORT_METHOD(logRevenue:(NSString *)productIdentifier quantity:(int)quantity price:(double)price revenueType:(NSString*)revenueType receipt:(NSString*)receipt eventProperties:(NSDictionary*)eventProperties)
{
    AMPRevenue *revenue = [[[AMPRevenue revenue] setProductIdentifier:productIdentifier] setQuantity: quantity];
    [revenue setPrice:[NSNumber numberWithDouble:price]];
    [revenue setRevenueType:revenueType];
    [revenue setReceipt:[receipt dataUsingEncoding:NSUTF8StringEncoding]];
    [revenue setEventProperties:eventProperties];
    [[Amplitude instance] logRevenueV2:revenue];
}

RCT_EXPORT_METHOD(setGroup:(NSString *)groupType groupName:(NSObject *)groupName)
{
    [[Amplitude instance] setGroup:groupType groupName:groupName];
}

RCT_EXPORT_METHOD(addToUserProperty:(NSString *)property value:(int)value)
{
    AMPIdentify *identify = [[AMPIdentify identify] add:property value:[NSNumber numberWithInt:value]];
    [[Amplitude instance] identify:identify];
}

RCT_EXPORT_METHOD(setUserPropertyOnce:(NSString *)property value:(NSString *)value)
{
    AMPIdentify *identify = [[AMPIdentify identify] setOnce:property value:value];
    [[Amplitude instance] identify:identify];
}

@end
