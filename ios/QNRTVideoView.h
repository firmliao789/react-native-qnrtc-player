//
//  QNRTVideoView.h
//  Pods
//
//  Created by 廖毅 on 2022/1/23.
//

#import <QNRTPlayerKit/QNRTPlayerKit.h>

@interface QNRTPlayerView: QNRTVideoView

@property (nonatomic, copy) RCTBubblingEventBlock onRegionChange;

@end
