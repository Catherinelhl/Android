//******************************************************************************
// SCICHART® Copyright SciChart Ltd. 2011-2018. All rights reserved.
//
// Web: http://www.scichart.com
// Support: support@scichart.com
// Sales:   sales@scichart.com
//
// CandlestickChartView.swift is part of the SCICHART® Examples. Permission is hereby granted
// to modify, create derivative works, distribute and publish any part of this source
// code whether for commercial, private or personal use.
//
// The SCICHART® examples are distributed in the hope that they will be useful, but
// without any warranty. It is provided "AS IS" without warranty of any kind, either
// expressed or implied.
//******************************************************************************

class CandlestickChartView: SingleChartLayout {
    
    override func initExample() {
        let priceSeries = DataManager.getPriceDataIndu()
        let size = priceSeries!.size()
        
        let xAxis = SCICategoryDateTimeAxis()
        xAxis.growBy = SCIDoubleRange(min: SCIGeneric(0), max: SCIGeneric(0.1))
        xAxis.visibleRange = SCIDoubleRange(min: SCIGeneric(size - 30), max: SCIGeneric(size))
        
        let yAxis = SCINumericAxis()
        yAxis.growBy = SCIDoubleRange(min: SCIGeneric(0.1), max: SCIGeneric(0.1))
        yAxis.autoRange = .always
        
//        let xData: [Double] = [0, 1,   2,   3,   4,   5,    6,   7,    8,   9]
//        let yData: [Double] = [0, 0.1, 0.3, 0.5, 0.4, 0.35, 0.3, 0.25, 0.2, 0.1]
        
        let dataSeries0 = SCIXyDataSeries(xType: .dateTime, yType: .double)
        dataSeries0.seriesName = "Line Series"
        dataSeries0.appendRangeX(SCIGeneric(priceSeries!.dateData()) , y: SCIGeneric(priceSeries!.openData()),count:priceSeries!.size())
        
        let lineSeries = SCIFastLineRenderableSeries()
        lineSeries.dataSeries = dataSeries0
        lineSeries.strokeStyle = SCISolidPenStyle(colorCode: 0xFF4682B4, withThickness: 2)
        
//        let dataSeries1 = SCIXyDataSeries(xType: .dateTime, yType: .double)
//        dataSeries1.appendRangeX(SCIGeneric(priceSeries!.dateData()), y: SCIGeneric(Double.random(in: 0...10000)), count: priceSeries!.size())
//
//        let columnSeries = SCIFastColumnRenderableSeries()
//        columnSeries.dataSeries = dataSeries1
        
        
        let dataSeries = SCIOhlcDataSeries(xType: .dateTime, yType: .double)
        dataSeries.appendRangeX(SCIGeneric(priceSeries!.dateData()), open: SCIGeneric(priceSeries!.openData()), high: SCIGeneric(priceSeries!.highData()), low: SCIGeneric(priceSeries!.lowData()), close: SCIGeneric(priceSeries!.closeData()), count: priceSeries!.size())
        
        let rSeries = SCIFastCandlestickRenderableSeries()
        rSeries.dataSeries = dataSeries;
        rSeries.strokeUpStyle = SCISolidPenStyle(colorCode: 0xFF00AA00, withThickness: 1.0)
        rSeries.fillUpBrushStyle = SCISolidBrushStyle(colorCode: 0x9000AA00)
        rSeries.strokeDownStyle = SCISolidPenStyle(colorCode: 0xFFFF0000, withThickness: 1.0)
        rSeries.fillDownBrushStyle = SCISolidBrushStyle(colorCode: 0x90FF0000)
        
        SCIUpdateSuspender.usingWithSuspendable(surface) {
            self.surface.xAxes.add(xAxis)
            self.surface.yAxes.add(yAxis)
            self.surface.renderableSeries.add(rSeries)
            self.surface.renderableSeries.add(lineSeries)
//            self.surface.renderableSeries.add(columnSeries)
            
            self.surface.chartModifiers = SCIChartModifierCollection(childModifiers: [SCIPinchZoomModifier(), SCIZoomExtentsModifier(), SCIZoomPanModifier()])
            
            rSeries.addAnimation(SCIWaveRenderableSeriesAnimation(duration: 3, curveAnimation: .easeOut))
            lineSeries.addAnimation(SCIWaveRenderableSeriesAnimation(duration: 3, curveAnimation: .easeOut))
//            columnSeries.addAnimation(SCIWaveRenderableSeriesAnimation(duration: 3, curveAnimation: .easeOut))
        }
    }
}
