//
//  KLineCombinedChartRenderer.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/11/27.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Charts

class KLineCombinedChartRenderer: CombinedChartRenderer {

    override func drawData(context: CGContext) {
        for (index,renderer) in subRenderers.enumerated() where renderer is CandleStickChartRenderer {
            if renderer is KLineCandleChartRenderer {
//                MyLog("KLineCandleChartRenderer")
                break
            }else {
                guard let chart = chart else { break }
//                MyLog("CandleStickChartRenderer")
                subRenderers[index] = KLineCandleChartRenderer(dataProvider: chart, animator: animator, viewPortHandler: viewPortHandler)
            }
        }
        super.drawData(context: context)
    }
}

