#import <Foundation/Foundation.h>
#import <LocalAuthentication/LocalAuthentication.h>

@interface com_codename1_fingerprint_impl_InternalFingerprintImpl : NSObject {
    LAContext *_context;
}

-(BOOL)isAvailable;
-(void)scan:(NSString *)reason;
-(BOOL)isSupported;
-(void)addPassword:(int)requestId param1:(NSString*)reason param2:(NSString*)account param3:(NSString*)password;
-(void)deletePassword:(int)requestId param1:(NSString*)reason param2:(NSString*)account;
-(void)getPassword:(int)requestId param1:(NSString*)reason param2:(NSString*)account;
-(void)cancelRequest:(int)requestId;
@end
