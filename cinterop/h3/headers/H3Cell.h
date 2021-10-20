//
//  H3Cell.h
//  h3
//
//  Created by Steve Kim on 2021/07/14.
//

#import <Foundation/Foundation.h>
#import "h3api.h"
#import "GeoCoord.h"

NS_ASSUME_NONNULL_BEGIN

@interface H3Cell : NSObject
@property (nonatomic, readonly) BOOL isPentagon;
@property (nonatomic, readonly) BOOL isResClassIII;
@property (nonatomic, readonly) BOOL isValid;
@property (nonatomic, readonly) int32_t baseCellNumber;
@property (nonatomic, readonly) int32_t resolution;
@property (nonatomic, readonly) H3Index index;
@property (nonatomic, readonly) NSArray<GeoCoord *> *geoCoords;

- (id)initWithIndex:(H3Index)index;
- (id)initWithString:(NSString *)string;
- (BOOL)areNeighborCells:(H3Index)destination;
- (NSArray<H3Cell *> *)children:(int)childRes;
- (int64_t)childrenSize:(int)childRes;
- (H3Index)toParent:(int)parentRes;
@end

NS_ASSUME_NONNULL_END
