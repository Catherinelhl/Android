//******************************************************************************
// SCICHART® Copyright SciChart Ltd. 2011-2018. All rights reserved.
//
// Web: http://www.scichart.com
// Support: support@scichart.com
// Sales:   sales@scichart.com
//
// SCIWaveRenderableSeriesAnimation.h is part of SCICHART®, High Performance Scientific Charts
// For full terms and conditions of the license, see http://www.scichart.com/scichart-eula/
//
// This source code is protected by international copyright law. Unauthorized
// reproduction, reverse-engineering, or distribution of all or any portion of
// this source code is strictly prohibited.
//
// This source code contains confidential and proprietary trade secrets of
// SciChart Ltd., and should at no time be copied, transferred, sold,
// distributed or made available without express written permission.
//******************************************************************************

/** \addtogroup Animations
 *  @{
 */

#import "SCIAnimation.h"
#import "SCIRenderableSeriesAnimationProtocol.h"
#import "SCIOhlcRenderableSeriesAnimationProtocol.h"
#import "SCIMountainRenderableSeriesAnimationProtocol.h"
#import "SCIColumnRenderableSeriesAnimationProtocol.h"
#import "SCIScatterRenderableSeriesAnimationProtocol.h"
#import "SCICandleStickRenderableSeriesAnimationProtocol.h"
#import "SCIStackedSeriesCollectionAnimnationProtocol.h"
#import "SCIBubbleRenderableSeriesAnimationProtocol.h"
#import "SCIBandRenderableSeriesAnimationProtocol.h"
#import "SCIErrorBarsRenderableSeriesAnimationProtocol.h"
#import "SCIFixedErrorBarsRenderableSeriesAnimationProtocol.h"

/**
 Make wave effect when data is changed.
 */
@interface SCIWaveRenderableSeriesAnimation : SCIAnimation <SCIRenderableSeriesAnimationProtocol, SCIMountainRenderableSeriesAnimationProtocol, SCIColumnRenderableSeriesAnimationProtocol, SCIScatterRenderableSeriesAnimationProtocol, SCIOhlcRenderableSeriesAnimationProtocol, SCICandleStickRenderableSeriesAnimationProtocol, SCIStackedSeriesCollectionAnimnationProtocol, SCIBubbleRenderableSeriesAnimationProtocol, SCIBandRenderableSeriesAnimationProtocol, SCIErrorBarsRenderableSeriesAnimationProtocol, SCIFixedErrorBarsRenderableSeriesAnimationProtocol>

/**
 Wave duration before next point is started to animate. Default is 0.5.
 Means that when half way of current wave is drawn then next wave is started to draw.
 */
@property (nonatomic) float durationOfStepData;

@end

/** @}*/
