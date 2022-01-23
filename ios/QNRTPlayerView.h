//
//  QNRTVideoView.h
//  Pods
//
//  Created by 廖毅 on 2022/1/23.
//

#import <QNRTPlayerKit/QNRTPlayerKit.h>
#import <React/RCTComponent.h>

@interface QNRTPlayerView: QNRTVideoView

@property (nonatomic, copy) RCTDirectEventBlock onPlayerStateChanged;

@property (nonatomic, copy) RCTDirectEventBlock onPlayerError;

@end
