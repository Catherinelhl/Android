//
//  KLineCandleChartRenderer.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/11/27.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Charts

class KLineCandleChartRenderer: CandleStickChartRenderer {
    
    override func drawDataSet(context: CGContext, dataSet: ICandleChartDataSet) {
        super.drawDataSet(context: context, dataSet: dataSet)
        
        guard
            let dataProvider = dataProvider
            else { return }
        
        let trans = dataProvider.getTransformer(forAxis: dataSet.axisDependency)
        
        let xBounds = XBounds()
        xBounds.set(chart: dataProvider, dataSet: dataSet, animator: animator)
        
        context.saveGState()
        var minValue: Double = Double.greatestFiniteMagnitude
        var maxValue: Double = -Double.greatestFiniteMagnitude
        
        // 初始化声明可见区域的最小最大值对应的X坐标点
        var minPositionX: Double!
        var maxPositionX: Double!
        
        for j in stride(from: xBounds.min, through: xBounds.range + xBounds.min, by: 1) {
            guard let e = dataSet.entryForIndex(j) as? CandleChartDataEntry else { continue }
            
            let xPos = e.x
            
//            let open = e.open
//            let close = e.close
            let high = e.high
            let low = e.low
            
            //写入最小的Y值的位置和数值
            if minValue > low {
                minValue = low
                minPositionX = xPos
            }
            //写入最大的Y值的位置和数值
            if maxValue < high {
                maxValue = high
                maxPositionX = xPos
            }
        }
        
        // 可见区域最左界的箭头数据
        guard let lowestVisbleEntry = dataSet.entryForIndex(xBounds.min) as? CandleChartDataEntry else {
            return
        }
        var lowestVisblePoint = CGPoint(x: lowestVisbleEntry.x, y: lowestVisbleEntry.low) // 此处主要是为了获取X坐标，lowestVisbleEntry.low可为high、open、close
        trans.pointValueToPixel(&lowestVisblePoint)
        
        // 可见区域最右界的箭头数据
        guard let highestVisbleEntry = dataSet.entryForIndex( xBounds.range + xBounds.min) as? CandleChartDataEntry else {
            return
        }
        var highestVisblePoint = CGPoint(x: highestVisbleEntry.x, y: highestVisbleEntry.high)
        trans.pointValueToPixel(&highestVisblePoint)
        
        // 可见区域中的最小值
        let minValueStr = String.init(format: "%.4f", minValue)
        var minPoint = CGPoint(x: CGFloat(minPositionX), y: CGFloat(minValue * animator.phaseY))
        // 点转化为像素
        trans.pointValueToPixel(&minPoint)
        calculateTextPosition(minValueStr, originPoint: &minPoint, lowestVisibleX: lowestVisblePoint.x, highestVisibleX: highestVisblePoint.x, isMaxValue: false, dataSet: dataSet)
        
        // 可见区域中的最大值
        let maxValueStr = String.init(format: "%.4f", maxValue)
        var maxPoint: CGPoint = CGPoint.init(x: CGFloat(maxPositionX), y: CGFloat(maxValue * animator.phaseY))
        trans.pointValueToPixel(&maxPoint)
        calculateTextPosition(maxValueStr, originPoint: &maxPoint, lowestVisibleX: lowestVisblePoint.x, highestVisibleX: highestVisblePoint.x, isMaxValue: true, dataSet: dataSet)
        
        context.restoreGState()
    }
    
    /// 计算绘制位置并绘制文本，注意坐标值(相对于图标)转像素值(相对于手机屏幕)
    private func calculateTextPosition(_ valueText: String, originPoint: inout CGPoint, lowestVisibleX: CGFloat, highestVisibleX: CGFloat, isMaxValue: Bool, dataSet:ICandleChartDataSet){
        let attributes: [NSAttributedString.Key : Any] = [.font: UIFont.init(name: "Helvetica", size: 12) ?? UIFont.systemFont(ofSize: 12), .foregroundColor: dataSet.minMaxValueTextColor]
        
        let stringText = NSString.init(string: "←\(valueText)")
        
        let textWidth = stringText.boundingRect(with: CGSize.init(width: 0, height: 12), options: .usesLineFragmentOrigin, attributes: attributes, context: nil).width + 2
        var resultText: NSString?
        
        if isMaxValue {
            originPoint.y -= 8
        } else {
            originPoint.y -= 9
        }
        if originPoint.x - 10 < lowestVisibleX {
            originPoint.x += 3
            resultText = NSString(string: "←" + valueText)
        }
        else if originPoint.x - textWidth - 10 < lowestVisibleX {
            originPoint.x += 3
            resultText = NSString(string: "←" + valueText)
        }
        else if ((originPoint.x + textWidth + 10 >= highestVisibleX) && (highestVisibleX - lowestVisibleX >= textWidth + 3)) {
            resultText = NSString(string: valueText + "→")
            originPoint.x -= (textWidth + 2)
        }
        else {
            originPoint.x += 3
            resultText = NSString(string: "←" + valueText)
        }
        resultText?.draw(at: originPoint, withAttributes: attributes)
    }
    
    // MARK: 重写绘制高亮方法
    override func drawHighlighted(context: CGContext, indices: [Highlight]) {
        
        guard
            let dataProvider = dataProvider,
            let candleData = dataProvider.candleData
            else { return }
        
        context.saveGState()
        
        for high in indices
        {
            guard
                let set = candleData.getDataSetByIndex(high.dataSetIndex) as? ICandleChartDataSet,
                set.isHighlightEnabled
                else { continue }
            
            guard let e = set.entryForXValue(high.x, closestToY: high.y) as? CandleChartDataEntry else { continue }
            
            if !isInBoundsX(entry: e, dataSet: set)
            {
                continue
            }
            
            let trans = dataProvider.getTransformer(forAxis: set.axisDependency)
            
            context.setStrokeColor(set.highlightColor.cgColor)
            context.setLineWidth(set.highlightLineWidth)
            
            if set.highlightLineDashLengths != nil
            {
                context.setLineDash(phase: set.highlightLineDashPhase, lengths: set.highlightLineDashLengths!)
            }
            else
            {
                context.setLineDash(phase: 0.0, lengths: [])
            }
            
            let closeValue = e.close * Double(animator.phaseY)
            let openValue = e.open * Double(animator.phaseY)
            let y = (closeValue + openValue) / 2.0
            
            let pt = trans.pixelForValues(x: e.x, y: y)
            
            high.setDraw(pt: pt)
            
            // draw the lines
            drawHighlightLines(context: context, point: pt, set: set)
        }
        
        context.restoreGState()
    }
    
    private func isInBoundsX(entry e: ChartDataEntry, dataSet: IBarLineScatterCandleBubbleChartDataSet) -> Bool
    {
        let entryIndex = dataSet.entryIndex(entry: e)
        return Double(entryIndex) < Double(dataSet.entryCount) * animator.phaseX
    }

}

var minMaxValueTextColorKey = 101
extension ICandleChartDataSet {
    /// 蜡烛图显示最大最小值文本颜色
    var minMaxValueTextColor : NSUIColor {
        set{
            objc_setAssociatedObject(self, &minMaxValueTextColorKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
        get{
            return (objc_getAssociatedObject(self, &minMaxValueTextColorKey) as? NSUIColor) ?? NSUIColor(red: 51/255.0, green: 51/255.0, blue: 51/255.0, alpha: 1.0)
        }
    }
}
