#import "QNRTPlayerManager.h"
#import <QNRTPlayerKit/QNRTPlayerKit.h>
#import "QNRTPlayerView.h"

@interface QNRTPlayerManager()<QNRTPlayerDelegate>

@property (nonatomic, strong) QNRTPlayer *player;
@property (nonatomic, strong) QNRTPlayerView *playView;

@end

@implementation QNRTPlayerManager

RCT_EXPORT_MODULE(QNRTPlayer)

- (QNRTPlayerView *)view
{
    self.player = [[QNRTPlayer alloc] init];
    self.player.delegate = self;
    self.playView=[[QNRTPlayerView alloc] init];
    self.player.statisticInterval=2;
    self.player.playView=self.playView;
    self.playView.player=self.player;
    return  self.playView;
}

RCT_CUSTOM_VIEW_PROPERTY(source,QNRTPSource, QNRTPlayer)
{
    [self.player playWithUrl:[NSURL URLWithString:json[@"uri"]] supportHttps:json[@"mHttpPost"]];
}

RCT_EXPORT_VIEW_PROPERTY(onPlayerStateChanged, RCTDirectEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onPlayerError, RCTDirectEventBlock);

#pragma mark QNRTPlayerDelegate

- (void)RTPlayer:(QNRTPlayer *)player didFailWithError:(NSError *)error {
    NSLog(@"QNRTPlayerDelegate didFailWithError:%@",error);
    self.playView.onPlayerError(@{
        @"code":@(error.code),
        @"message":error.description
    });
}

- (void)RTPlayer:(QNRTPlayer *)player playStateDidChange:(QNRTPlayState)playState {
    NSLog(@"QNRTPlayerDelegate playStateDidChange:%d",(int)playState);
    dispatch_async(dispatch_get_main_queue(), ^{
        self.playView.onPlayerStateChanged(@{
            @"state":@(playState)
        });
    });
}

- (void)RTPlayer:(QNRTPlayer *)player didGetStatistic:(NSDictionary *)statistic{
    NSLog(@"didGetStatistic%@",statistic);
}

@end
