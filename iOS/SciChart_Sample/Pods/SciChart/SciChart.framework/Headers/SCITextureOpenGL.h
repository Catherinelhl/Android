//******************************************************************************
// SCICHART® Copyright SciChart Ltd. 2011-2018. All rights reserved.
//
// Web: http://www.scichart.com
// Support: support@scichart.com
// Sales:   sales@scichart.com
//
// SCITextureOpenGL.h is part of SCICHART®, High Performance Scientific Charts
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

#import <Foundation/Foundation.h>
#import <OpenGLES/ES2/gl.h>
#import <CoreGraphics/CoreGraphics.h>

@class UIImage, UIColor;
@protocol SCIRenderContext2DProtocol;
@interface SCITextureOpenGL : NSObject

@property (nonatomic) NSArray <UIColor *> *colors;
@property (nonatomic) NSArray <NSNumber *> *stops;
@property (nonatomic) CGSize textureSize;

-(instancetype)initWithId:(GLuint)textureId;
-(instancetype)initWithByteData:(GLubyte*)data Width:(int)width Height:(int)height;
-(instancetype)initWithByteData:(GLubyte*)data Width:(int)width Height:(int)height WrapParameter:(GLint)wrapParameter renderContext:(id<SCIRenderContext2DProtocol>)renderContext;
-(instancetype)initWithImage:(UIImage*)image;
-(instancetype)initWithFloatData:(GLfloat*)data Width:(int)width Height:(int)height;
-(instancetype)initWithGradientCoords:(float*)coords Colors:(uint*)colors Count:(int)count;
-(instancetype)initWithGradientCoords:(float*)coords Colors:(uint*)colors Count:(int)count Detalization:(int)detalization;

-(instancetype)initWithByteData:(GLubyte*)data Width:(int)width Height:(int)height Context:(id<SCIRenderContext2DProtocol>)context;
-(instancetype)initWithImage:(UIImage*)image Context:(id<SCIRenderContext2DProtocol>)context;
-(instancetype)initWithFloatData:(GLfloat*)data Width:(int)width Height:(int)height Context:(id<SCIRenderContext2DProtocol>)context;
-(instancetype)initWithGradientCoords:(float*)coords Colors:(uint*)colors Count:(int)count Context:(id<SCIRenderContext2DProtocol>)context;
-(instancetype)initWithGradientCoords:(float*)coords Colors:(uint*)colors Count:(int)count Detalization:(int)detalization Context:(id<SCIRenderContext2DProtocol>)context;

-(void)updateWithByteData:(GLubyte*)data Width:(int)width Height:(int)height;
-(void)updateWithFloatData:(GLfloat*)data Width:(int)width Height:(int)height;

-(GLuint)textureId;
-(BOOL) isValid;

@end
