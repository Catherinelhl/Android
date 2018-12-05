//
//  KLineBarChartDataRenderer.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/11/26.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Charts

class KLineBarChartRenderer: BarChartRenderer {

    // MARK: 重写绘制高亮状态
    override func drawHighlighted(context: CGContext, indices: [Highlight]) {
//        super.drawHighlighted(context: context, indices: indices)
        guard
            let dataProvider = dataProvider,
            let barData = dataProvider.barData
            else { return }
        context.saveGState()
        for high in indices {
            guard
                let set = barData.getDataSetByIndex(high.dataSetIndex) as? IBarChartDataSet,
                set.isHighlightEnabled
                else { continue }
            guard let e = set.entryForXValue(high.x, closestToY: high.y) as? BarChartDataEntry else { continue }
            let trans = dataProvider.getTransformer(forAxis: set.axisDependency)
            
            let pt = trans.pixelForValues(x: e.x, y: e.y)
            // 绘制高亮状态下的 指引线
            context.setStrokeColor(set.highlightLineColor.cgColor)
            context.setLineWidth(set.highlightLineWidth)
            context.beginPath()
            context.move(to: CGPoint(x: pt.x, y: viewPortHandler.contentTop))
            context.addLine(to: CGPoint(x: pt.x, y: viewPortHandler.contentBottom))
            context.strokePath()
        }
        context.restoreGState()
    }
}

var highlightLineColorKey = 100
extension IBarChartDataSet {
    /// 柱状图高亮指引线颜色
    var highlightLineColor : NSUIColor {
        set{
            objc_setAssociatedObject(self, &highlightLineColorKey, newValue, .OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
        get{
            return (objc_getAssociatedObject(self, &highlightLineColorKey) as? NSUIColor) ?? NSUIColor(red: 255.0/255.0, green: 187.0/255.0, blue: 115.0/255.0, alpha: 1.0)
        }
    }
}
