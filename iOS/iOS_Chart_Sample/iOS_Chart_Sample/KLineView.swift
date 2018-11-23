//
//  KLineView.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/11/15.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Charts
import SnapKit
import SwiftyJSON

class KLineView: UIView {
    
    private let combinedChartView = CombinedChartView()
    private let barChartView = BarChartView()

    init() {
        super.init(frame: .zero)
        createSubviews()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    private func createSubviews() {
        
        setCombinedChartView()
        
        setBarChartView()
        
        setSubviewsLayout()
    }
    
    private func setCombinedChartView() {
        // y轴自动调节上下限
        combinedChartView.autoScaleMinMaxEnabled = true
        combinedChartView.scaleYEnabled = false
        combinedChartView.chartDescription?.enabled = false
        
        let xAxis = combinedChartView.xAxis
        xAxis.labelPosition = .bottom
        xAxis.enabled = false
        
        let leftAxis = combinedChartView.leftAxis
        leftAxis.drawLabelsEnabled = false
        leftAxis.gridLineDashLengths = [5.0,5.0]
        
        let rightAxis = combinedChartView.rightAxis
        rightAxis.labelPosition = .insideChart
        rightAxis.gridLineDashLengths = [5.0,5.0]
        
        
        combinedChartView.legend.drawInside = true
        
        combinedChartView.delegate = self
        self.addSubview(combinedChartView)
    }
    
    private func setBarChartView() {
        //设置柱状图
        barChartView.autoScaleMinMaxEnabled = true
        barChartView.scaleYEnabled = false
        barChartView.chartDescription?.enabled = false
        
        barChartView.xAxis.labelPosition = .bottom
        barChartView.xAxis.drawGridLinesEnabled = false
        
        let leftAxis = barChartView.leftAxis
        leftAxis.drawLabelsEnabled = false
        leftAxis.gridLineDashLengths = [5.0,5.0]
        
        let rightAxis = barChartView.rightAxis
        rightAxis.labelPosition = .insideChart
        rightAxis.gridLineDashLengths = [5.0,5.0]
        
        barChartView.legend.enabled = false
        
        barChartView.delegate = self
        self.addSubview(barChartView)
    }
    
    private func setSubviewsLayout() {
        
        barChartView.snp.makeConstraints { (make) in
            make.bottom.leading.trailing.equalToSuperview()
            make.height.equalTo(88)
        }
        
        combinedChartView.snp.makeConstraints { (make) in
            make.top.leading.trailing.equalToSuperview()
            make.bottom.equalTo(barChartView.snp.top)
        }
    }
    
    func setData(_ data:[[Any]]) {
        
        setCombinedChartData(with: data)

        setBarChartData(with: data)
        
    }
    
    private func setCombinedChartData(with data:[[Any]]) {
        let combinedData = CombinedChartData.init()
        combinedData.candleData = generateCanldeData(with: data)
        combinedData.lineData = generateLineData(with: data)
        
        combinedChartView.xAxis.axisMaximum = combinedData.xMax + 5
        combinedChartView.xAxis.axisMinimum = combinedData.xMin - 5
        combinedChartView.data = combinedData
        
        let scaleX = combinedData.xMax >= 50 ? CGFloat(combinedData.xMax / 50) : 1
        combinedChartView.zoom(scaleX: scaleX , scaleY: 1, xValue: combinedData.xMax, yValue: combinedData.yMax, axis: .left)
        
//        let srcMatrix = combinedChartView.viewPortHandler.touchMatrix
//        combinedChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: combinedChartView, invalidate: true)
    }
    
    private func setBarChartData(with data:[[Any]]) {
        /* 柱状图数据设置 */
        let barData = generateBarData(with: data)
        barChartView.data = barData
        
        barChartView.xAxis.axisMaximum = barData.xMax + 5
        barChartView.xAxis.axisMinimum = barData.xMin - 5
        
        let scaleX = barData.xMax >= 50 ? CGFloat(barData.xMax / 50) : 1
        barChartView.zoom(scaleX: scaleX , scaleY: 1, xValue: barData.xMax, yValue: barData.yMax, axis: .left)
        
//        let srcMatrix = barChartView.viewPortHandler.touchMatrix
//        barChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: barChartView, invalidate: true)
    }
    
    private func generateCanldeData(with data:[[Any]]) -> CandleChartData {
        
        var entries = [CandleChartDataEntry]()
        for (index,dataArray) in data.enumerated() {
            
            let jsonArray = JSON(dataArray)
            
            let open = jsonArray[1].stringValue.doubleValue
            let high = jsonArray[2].stringValue.doubleValue
            let low = jsonArray[3].stringValue.doubleValue
            let close = jsonArray[4].stringValue.doubleValue
            
            let entry = CandleChartDataEntry(x: Double(index), shadowH: high, shadowL: low, open: open, close: close)
            entries.append(entry)
        }
        
        let set = CandleChartDataSet(values: entries, label: "")
        set.shadowColorSameAsCandle = true
        set.shadowWidth = 0.7
     
        set.decreasingColor = .green
        set.decreasingFilled = true
        set.increasingColor = .red
        set.increasingFilled = true
        set.drawValuesEnabled = false
        
        return CandleChartData(dataSet: set)
    }
    
    private func generateLineData(with data:[[Any]]) -> LineChartData{
        
        let set_MA10 = getMA10LineChartSet(with: data)
        let set_MA50 = getMA50LineChartSet(with: data)
        
        return LineChartData(dataSets: [set_MA10,set_MA50])
    }
    
    private func generateBarData(with data:[[Any]]) -> BarChartData {
        var entries = [BarChartDataEntry]()
        var barColorsArray = [UIColor]()
        for (index,dataArray) in data.enumerated() {
            let jsonArray = JSON(dataArray)
            let open = jsonArray[1].stringValue.doubleValue
            let volume = jsonArray[5].stringValue.doubleValue
            let close = jsonArray[4].stringValue.doubleValue
            
            entries.append(BarChartDataEntry(x: Double(index), y: volume))
            if close - open >= 0 {
                barColorsArray.append(.red)
            }else {
                barColorsArray.append(.green)
            }
        }
        
        let set = BarChartDataSet(values: entries, label: "")
        set.colors = barColorsArray
        set.drawValuesEnabled = false
        
        return BarChartData(dataSet: set)
    }
    
    private func getMA10LineChartSet(with data:[[Any]]) -> LineChartDataSet{
        var entries = [ChartDataEntry]()
        let closePriceMA10Array = getClosePriceArray(with: data, days: 10)
        for (i , value) in closePriceMA10Array {
            entries.append(ChartDataEntry(x: Double(i), y: value))
        }
        
        let set = LineChartDataSet.init(values: entries, label: "MA(10)")
        configLineSet(set)
        set.setColor(.cyan)
        return set
    }
    
    private func getMA50LineChartSet(with data:[[Any]]) -> LineChartDataSet{
        var entries = [ChartDataEntry]()
        let closePriceMA10Array = getClosePriceArray(with: data, days: 50)
        for (i , value) in closePriceMA10Array {
            entries.append(ChartDataEntry(x: Double(i), y: value))
        }
        
        let set = LineChartDataSet.init(values: entries, label: "MA(50)")
        configLineSet(set)
        set.setColor(.purple)
        return set
    }
    
    private func getClosePriceArray(with data:[[Any]], days:Int) -> [(Int,Double)]{
        
        var closePriceArray = [(Int,Double)]()
        var daysClosePrice = 0.0
        var count = 0
        for (i , dataArray) in data.enumerated().reversed() {
            let jsonArray = JSON(dataArray)
            if i - days - 1 < 0 {break}
            count += 1
            daysClosePrice += jsonArray[4].stringValue.doubleValue
            if count == days {
                let price = daysClosePrice / Double(days)
                closePriceArray.insert((i,price),at: 0)
                count = 0
                daysClosePrice = 0
            }
        }
        return closePriceArray
    }
    
    private func configLineSet(_ set:LineChartDataSet){
        set.lineWidth = 1
        set.mode = .cubicBezier
        set.drawValuesEnabled = false
        set.drawCirclesEnabled = false
    }
    
}

extension KLineView : ChartViewDelegate {
    func chartScaled(_ chartView: ChartViewBase, scaleX: CGFloat, scaleY: CGFloat) {
        let srcMatrix = chartView.viewPortHandler.touchMatrix
        combinedChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: combinedChartView, invalidate: true)
        barChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: barChartView, invalidate: true)
    }
    
    func chartTranslated(_ chartView: ChartViewBase, dX: CGFloat, dY: CGFloat) {
        let srcMatrix = chartView.viewPortHandler.touchMatrix
        combinedChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: combinedChartView, invalidate: true)
        barChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: barChartView, invalidate: true)
    }
}

class DateValueFormatter: NSObject ,IAxisValueFormatter {
    private let dateFormatter = DateFormatter.init()
    init(dateFormat:String = "yy-MM-dd") {
        super.init()
        dateFormatter.dateFormat = dateFormat
    }
    
    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        return dateFormatter.string(from: Date.init(timeIntervalSince1970: value/1000))
    }
}

extension String {
    var doubleValue:Double{
        get{
            return NSDecimalNumber.init(string: self).doubleValue
        }
    }
}

// MARK: - 打印方法
func MyLog<T>(_ message : T,file:String = #file,methodName: String = #function, lineNumber: Int = #line){
    #if DEBUG
    let fileName = (file as NSString).lastPathComponent
    let dateForm = DateFormatter.init()
    dateForm.dateFormat = "HH:mm:ss:SSS"
    print("[\(fileName)][\(lineNumber)][\(dateForm.string(from: Date()))]\(methodName):\(message)")
    #endif
    
}
