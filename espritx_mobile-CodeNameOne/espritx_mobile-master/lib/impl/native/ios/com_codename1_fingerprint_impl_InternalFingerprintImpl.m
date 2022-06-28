#import "com_codename1_fingerprint_impl_InternalFingerprintImpl.h"
#include "com_codename1_fingerprint_impl_InternalCallback.h"
#include "com_codename1_ui_Display.h"
#include "com_codename1_ui_CN.h"



@implementation com_codename1_fingerprint_impl_InternalFingerprintImpl

-(LAContext*) context {
    if (_context == nil) {
       _context = [[LAContext alloc] init];
    }
    return _context;
}

-(BOOL)isAvailable{
    if (NSClassFromString(@"LAContext") == NULL) {
        return NO;
    }

    NSError *error = nil;
    LAContext *laContext = [self context];

    BOOL _out = [laContext canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error];

    if (_out) {
        NSString* types = @"";
        if ([self isTouchIDAvailable]) {
            types = [types stringByAppendingString:@" touch"];
        }
        if ([self isFaceIDAvailable]) {
            types = [types stringByAppendingString:@" face"];
        }
        struct ThreadLocalData* tdata = getThreadLocalData();
        JAVA_OBJECT pkey = fromNSString(tdata, @"Fingerprint.types");
        JAVA_OBJECT pval = fromNSString(tdata, types);
        com_codename1_ui_CN_setProperty___java_lang_String_java_lang_String(tdata, pkey, pval);

    }
    return _out;
}

- (BOOL) isTouchIDAvailable {
    if (![LAContext class]) return NO;

    LAContext *myContext = [self context];
    NSError *authError = nil;
    if (![myContext canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&authError]) {
        NSLog(@"%@", [authError localizedDescription]);
        return NO;
        // if (authError.code == LAErrorTouchIDNotAvailable) {}
    }

    if (@available(iOS 11.0, *)) {
        if (myContext.biometryType == LABiometryTypeTouchID){
            return YES;
        } else {
            return NO;
        }
    } else {
        return YES;
    }
}

- (BOOL) isFaceIDAvailable {
    if (![LAContext class]) return NO;

    LAContext *myContext = [self context];
    NSError *authError = nil;
    if (![myContext canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&authError]) {
        NSLog(@"%@", [authError localizedDescription]);
        return NO;
    }

    if (@available(iOS 11.0, *)) {
        if (myContext.biometryType == LABiometryTypeFaceID){
            return YES;
        } else {
            return NO;
        }
    } else {
        return NO;
    }
}


-(void)scan:(NSString *)reason {
    if (reason == nil) {
        reason = @"Authenticate for server login";
    }
    dispatch_async(dispatch_get_main_queue(), ^{
        LAContext *context = [self context];
        [context evaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics localizedReason:reason reply:^(BOOL success, NSError *authenticationError){
            if (success) {
                com_codename1_fingerprint_impl_InternalCallback_scanSuccess__(getThreadLocalData());
            }
            else {
                com_codename1_fingerprint_impl_InternalCallback_scanFail__(getThreadLocalData());
            }
        }];
    });
}

-(BOOL)isSupported{
    return YES;
}

-(NSString*)getAppName {
    struct ThreadLocalData* threadStateData = getThreadLocalData();
    enteringNativeAllocations();
    JAVA_OBJECT d = com_codename1_ui_Display_getInstance___R_com_codename1_ui_Display(CN1_THREAD_GET_STATE_PASS_SINGLE_ARG);
    JAVA_OBJECT key = fromNSString(CN1_THREAD_GET_STATE_PASS_ARG @"AppName");

    JAVA_OBJECT defaultVal = fromNSString(CN1_THREAD_GET_STATE_PASS_ARG @"CodenameOneApp");

    JAVA_OBJECT res = com_codename1_ui_Display_getProperty___java_lang_String_java_lang_String_R_java_lang_String(CN1_THREAD_GET_STATE_PASS_ARG d, key, defaultVal);
    finishedNativeAllocations();

    NSString *nsres = toNSString(CN1_THREAD_GET_STATE_PASS_ARG res);
    return nsres;
}

-(void)updatePassword:(int)requestId reason:(NSString*)reason account:(NSString*)account password:(NSString*)password {

    NSMutableDictionary *query = [NSMutableDictionary dictionary];

    [query setObject:(__bridge id)kSecClassGenericPassword forKey:(__bridge id) kSecClass];
    //[query setObject:@YES forKey:(__bridge id)kSecReturnData];
    //[query setObject:(__bridge id)kSecMatchLimitOne forKey:(__bridge id)kSecMatchLimit];
    [query setObject:account forKey:(__bridge id)kSecAttrAccount];
    [query setObject:[self getAppName] forKey:(__bridge id) kSecAttrService];
    [query setObject:reason forKey:(__bridge id)kSecUseOperationPrompt];


    NSMutableDictionary *changes = [NSMutableDictionary dictionary];
    [changes setObject:[password dataUsingEncoding:NSUTF8StringEncoding] forKey:(__bridge id)kSecValueData];

    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        OSStatus status = SecItemUpdate((__bridge CFDictionaryRef)query, (__bridge CFDictionaryRef)changes);
        if (status == errSecSuccess) {
            com_codename1_fingerprint_impl_InternalCallback_requestComplete___int_boolean(getThreadLocalData(), requestId, JAVA_TRUE);
        } else if (status == errSecUserCanceled) {
            JAVA_OBJECT jErrorMessage = fromNSString(getThreadLocalData(), @"__CANCELLED__");
            com_codename1_fingerprint_impl_InternalCallback_requestError___int_java_lang_String(getThreadLocalData(), requestId, jErrorMessage);
        } else {
            NSString* errorMessage = [self errorString:status];
            JAVA_OBJECT jErrorMessage = fromNSString(getThreadLocalData(), errorMessage);
            com_codename1_fingerprint_impl_InternalCallback_requestError___int_java_lang_String(getThreadLocalData(), requestId, jErrorMessage);
        }
    });
}

-(void)addPassword:(int)requestId param1:(NSString*)reason param2:(NSString*)account param3:(NSString*)password {
    SecAccessControlRef sacRef = SecAccessControlCreateWithFlags(kCFAllocatorDefault,
                 kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly,
                 kSecAccessControlTouchIDCurrentSet,
                 nil);

    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    [dict setObject:(__bridge id)kSecClassGenericPassword forKey:(__bridge id) kSecClass];
    [dict setObject:account forKey:(__bridge id)kSecAttrAccount];
    [dict setObject:[self getAppName] forKey:(__bridge id) kSecAttrService];
    [dict setObject:[password dataUsingEncoding:NSUTF8StringEncoding] forKey:(__bridge id)kSecValueData];
    [dict setObject:(__bridge id)sacRef forKey:(__bridge id)kSecAttrAccessControl];
    [dict setObject:reason forKey:(__bridge id)kSecUseOperationPrompt];

    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        OSStatus status = SecItemAdd((__bridge CFDictionaryRef)dict, nil);
        if (status == errSecDuplicateItem) {
            [self updatePassword:requestId reason:reason account:account password:password];
            return;
        }
        if (status == errSecSuccess) {
            com_codename1_fingerprint_impl_InternalCallback_requestComplete___int_boolean(getThreadLocalData(), requestId, JAVA_TRUE);
        } else if (status == errSecUserCanceled) {
            JAVA_OBJECT jErrorMessage = fromNSString(getThreadLocalData(), @"__CANCELLED__");
            com_codename1_fingerprint_impl_InternalCallback_requestError___int_java_lang_String(getThreadLocalData(), requestId, jErrorMessage);
        } else {
            NSString* errorMessage = [self errorString:status];
            JAVA_OBJECT jErrorMessage = fromNSString(getThreadLocalData(), errorMessage);
            com_codename1_fingerprint_impl_InternalCallback_requestError___int_java_lang_String(getThreadLocalData(), requestId, jErrorMessage);
        }
    });
}

-(NSString*)errorString:(OSStatus)status {
    if (@available(iOS 11.3, *)) {
        return (__bridge NSString*)SecCopyErrorMessageString(status, NULL);
    } else {
        return [NSString stringWithFormat:@"Error code %d", status];
    }
}

-(void)deletePassword:(int)requestId param1:(NSString*)reason param2:(NSString*)account {

    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    [dict setObject:(__bridge id)kSecClassGenericPassword forKey:(__bridge id) kSecClass];
    //[dict setObject:(id)kSecMatchLimitOne forKey:(__bridge id)kSecMatchLimit];
    [dict setObject:account forKey:(__bridge id)kSecAttrAccount];
    [dict setObject:[self getAppName] forKey:(__bridge id) kSecAttrService];
    //[dict setObject:reason forKey:(__bridge id)kSecUseOperationPrompt];

    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{

        OSStatus status = SecItemDelete((__bridge CFDictionaryRef)dict);
        if (status == errSecSuccess) {
            com_codename1_fingerprint_impl_InternalCallback_requestComplete___int_boolean(getThreadLocalData(), requestId, JAVA_TRUE);
        } else if (status == errSecUserCanceled) {
            JAVA_OBJECT jErrorMessage = fromNSString(getThreadLocalData(), @"__CANCELLED__");
            com_codename1_fingerprint_impl_InternalCallback_requestError___int_java_lang_String(getThreadLocalData(), requestId, jErrorMessage);
        } else {
            NSString* errorMessage = [self errorString:status];
            JAVA_OBJECT jErrorMessage = fromNSString(getThreadLocalData(), errorMessage);
            com_codename1_fingerprint_impl_InternalCallback_requestError___int_java_lang_String(getThreadLocalData(), requestId, jErrorMessage);
        }
    });
}
-(void)getPassword:(int)requestId param1:(NSString*)reason param2:(NSString*)account {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];

    [dict setObject:(__bridge id)kSecClassGenericPassword forKey:(__bridge id) kSecClass];
    [dict setObject:@YES forKey:(__bridge id)kSecReturnData];
    [dict setObject:(__bridge id)kSecMatchLimitOne forKey:(__bridge id)kSecMatchLimit];
    [dict setObject:account forKey:(__bridge id)kSecAttrAccount];
    [dict setObject:[self getAppName] forKey:(__bridge id) kSecAttrService];
    [dict setObject:reason forKey:(__bridge id)kSecUseOperationPrompt];

    dispatch_async(dispatch_get_main_queue(), ^{
        CFTypeRef dataRef = NULL;
        OSStatus status = SecItemCopyMatching((__bridge CFDictionaryRef)dict, &dataRef);
        if (status == errSecSuccess) {
            NSData* data = (__bridge NSData *)dataRef;
            NSString* dataStr = [[[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding] autorelease];
            JAVA_OBJECT jDataStr = fromNSString(getThreadLocalData(), dataStr);
            com_codename1_fingerprint_impl_InternalCallback_requestSuccess___int_java_lang_String(getThreadLocalData(), requestId, jDataStr);
        } else if (status == errSecUserCanceled) {
            JAVA_OBJECT jErrorMessage = fromNSString(getThreadLocalData(), @"__CANCELLED__");
            com_codename1_fingerprint_impl_InternalCallback_requestError___int_java_lang_String(getThreadLocalData(), requestId, jErrorMessage);
        } else {
            NSString* errorMessage = [self errorString:status];
            JAVA_OBJECT jErrorMessage = fromNSString(getThreadLocalData(), errorMessage);
            com_codename1_fingerprint_impl_InternalCallback_requestError___int_java_lang_String(getThreadLocalData(), requestId, jErrorMessage);
        }
    });
}

-(void)cancelRequest:(int)requestId {
    NSLog(@"User requested cancelling fingerprint/faceID request.  Not implemented on iOS.");
}

-(void)dealloc {
    if (_context != nil) {
        [_context release];
        _context = nil;
    }
#ifndef CN1_USE_ARC
    [super dealloc];
#endif
}

@end
