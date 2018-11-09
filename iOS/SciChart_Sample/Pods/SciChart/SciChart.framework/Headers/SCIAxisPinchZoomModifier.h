//******************************************************************************
// SCICHART® Copyright SciChart Ltd. 2011-2018. All rights reserved.
//
// Web: http://www.scichart.com
// Support: support@scichart.com
// Sales:   sales@scichart.com
//
// SCIAxisPinchZoomModifier.h is part of SCICHART®, High Performance Scientific Charts
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

/** \addtogroup ChartModifiers
 *  @{
 */

#import <Foundation/Foundation.h>
#import "SCIRelativeZoomModifierBase.h"

/**
 * @brief The SCIAxisPinchZoomModifier class.
 * @discussion Provides drag to scale operations on Y-axis.
 */
@interface SCIAxisPinchZoomModifier : SCIRelativeZoomModifierBase

@property (nonatomic, readonly) BOOL gestureLocked;

/**
 * @brief The SCIAxisPinchZoomModifier class' property.
 * @discussion Defines which Axis to attach the AxisPinchZoomModifier to, matching by string Id
 */
@property (nonatomic, copy) NSString* axisId;

@end

/** @}*/