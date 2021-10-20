//
//  GeoCoord.h
//  h3
//
//  Created by Steve Kim on 2021/07/16.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface GeoCoord : NSObject
@property (nonatomic) double lat;
@property (nonatomic) double lng;
- (id)initWithLat:(double)lat lng:(double)lng;
@end

NS_ASSUME_NONNULL_END
