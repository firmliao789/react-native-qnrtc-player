//
//  QNRTVideoView.m
//  react-native-qnrt-player
//
//  Created by 廖毅 on 2022/1/23.
//

#import "QNRTPlayerView.h"

@implementation QNRTPlayerView

//- (instancetype)initWidthPlayer:(QNRTPlayer *)player{
//    self.player=player;
//    return self;
//}

//- (void)insertReactSubview:(UIView *)view atIndex:(NSInteger)atIndex
//{
//    NSLog(@"insertReactSubview");
//}
//- (void)removeReactSubview:(UIView *)subview
//{
//    NSLog(@"removeReactSubview");
//    [subview removeFromSuperview];
//}
//
//- (void)layoutSubviews
//{
//    NSLog(@"layoutSubviews");
//    [super layoutSubviews];
//}

- (void)removeFromSuperview
{
    NSLog(@"removeFromSuperview");
    [self.player stop];
    [super removeFromSuperview];
}
@end
