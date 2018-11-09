//******************************************************************************
// SCICHART® Copyright SciChart Ltd. 2011-2018. All rights reserved.
//
// Web: http://www.scichart.com
// Support: support@scichart.com
// Sales:   sales@scichart.com
//
// AppDelegate.m is part of the SCICHART® Examples. Permission is hereby granted
// to modify, create derivative works, distribute and publish any part of this source
// code whether for commercial, private or personal use.
//
// The SCICHART® examples are distributed in the hope that they will be useful, but
// without any warranty. It is provided "AS IS" without warranty of any kind, either
// expressed or implied.
//******************************************************************************

#import "AppDelegate.h"
#import "SCDConstants.h"
#import <CommonCrypto/CommonCrypto.h>
#import <SciChart/SciChart.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    _window = [[UIWindow alloc] initWithFrame:UIScreen.mainScreen.bounds];
    [_window makeKeyAndVisible];
    
    
    NSString *licensing = [[NSString alloc] initWithFormat:@"<LicenseContract>%@%@%@%@%@%@%@</LicenseContract>",
                           @"<Customer>SciChart</Customer>",
                           @"<OrderId>ABTSOFT-Dev-1</OrderId>",
                           @"<LicenseCount>1</LicenseCount>",
                           @"<IsTrialLicense>true</IsTrialLicense>",
                           @"<SupportExpires>12/07/2018 00:00:00</SupportExpires>",
                           @"<ProductCode>SC-IOS-2D-ENTERPRISE-SRC</ProductCode>",
                           @"<KeyCode>20bbefddf5a0308fb5c2109b023b83ce0511f5bdbef5857ad46e6f715c991ebeecd346c22930f8369158f228ac8e56dcddf5fe52f21871b7846d889fe001e663d78950874c4bc8e5de0e4b4987b89e610e953dff85319bfda177f43e1e4deba1114ca9a6addc0a7e9ead2543bef0a626889364e51864a327506db91a36a2315d95d6defc7f5317b62ffb955d8a2ed327b5b48aa688ce1d51c6261ed2a09332f790e8c9696cf0908bee68d0208220</KeyCode>"];
    
    [SCIChartSurface setRuntimeLicenseKey:licensing];
    [SCIChartSurface setDisplayLinkRunLoopMode:NSRunLoopCommonModes];
    

    UIStoryboard * mainStoryBoard = [UIStoryboard storyboardWithName:kMainStoryBoard bundle:nil];
    if ([NSUserDefaults.standardUserDefaults objectForKey:kFirstTimeLaunching]) {
        UINavigationController * navController = [mainStoryBoard instantiateViewControllerWithIdentifier:kMainNavController];
        _window.rootViewController = navController;
    } else {
        UINavigationController * navController = [mainStoryBoard instantiateViewControllerWithIdentifier:kFirstTimeNavController];
        _window.rootViewController = navController;
    }

    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
