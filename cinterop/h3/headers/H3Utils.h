//
//  H3Utils.h
//  h3
//
//  Created by Steve Kim on 2021/07/14.
//

#import <Foundation/Foundation.h>
#import "h3api.h"
#import "GeoCoord.h"

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, DistanceUnit) {
    km,
    m
};

@interface H3Utils : NSObject
+ (double)degsToRads:(double)degrees;
+ (double)radsToDegs:(double)radians;
+ (double)getHexagonAreaAvg:(int)res unit:(DistanceUnit)unit;
+ (double)getHexagonEdgeLengthAvg:(int)res unit:(DistanceUnit)unit;
+ (H3Index)geoToH3:(GeoCoord *)geoCoord res:(int)res;
+ (LinkedGeoPolygon)h3SetToLinkedGeo:(const H3Index *)h3Set numHexes:(const int)numHexes;
+ (NSString *)h3ToString:(H3Index)index;
@end

NS_ASSUME_NONNULL_END
