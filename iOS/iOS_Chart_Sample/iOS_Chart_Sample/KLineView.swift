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
    
    private var data = [[Any]]()

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
    
    // MARK: 设置组合图表
    private func setCombinedChartView() {
        // y轴自动调节上下限
        combinedChartView.autoScaleMinMaxEnabled = true
        combinedChartView.scaleYEnabled = false
        combinedChartView.chartDescription?.enabled = false
        combinedChartView.doubleTapToZoomEnabled = false
        
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
    
    // MARK: 设置柱状图
    private func setBarChartView() {
        //设置柱状图
        barChartView.autoScaleMinMaxEnabled = true
        barChartView.scaleYEnabled = false
        barChartView.chartDescription?.enabled = false
        barChartView.doubleTapToZoomEnabled = false
//        let highLighter = Highlight()
//        highLighter.drawY = 0.7
//        barChartView.highlighter = BarHighLightLine()
        
        let xAxis = barChartView.xAxis
        xAxis.labelPosition = .bottom
        xAxis.drawGridLinesEnabled = false
//        xAxis.valueFormatter = DateValueFormatter()
        
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
    
    // MARK: 设置子视图约束
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
    
    // MARK: - 设置数据
    func setData(_ data:[[Any]]) {
        
        self.data = data
        
        setCombinedChartData(with: data)

        setBarChartData(with: data)
        
    }
    // MARK: 设置组合图表数据
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
    
    // MARK: 设置柱状图数据
    private func setBarChartData(with data:[[Any]]) {
        /* 柱状图数据设置 */
        let barData = generateBarData(with: data)
        barChartView.data = barData
        
        let xAxis = barChartView.xAxis
        xAxis.valueFormatter = DateValueFormatter(data: data)
        xAxis.axisMaximum = barData.xMax + 5
        xAxis.axisMinimum = barData.xMin - 5
        
        let scaleX = barData.xMax >= 50 ? CGFloat(barData.xMax / 50) : 1
        barChartView.zoom(scaleX: scaleX , scaleY: 1, xValue: barData.xMax, yValue: barData.yMax, axis: .left)
        
//        let srcMatrix = barChartView.viewPortHandler.touchMatrix
//        barChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: barChartView, invalidate: true)
    }
    
    // MARK: 初始化蜡烛图数据
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
    
    // MARK: 初始化曲线图数据
    private func generateLineData(with data:[[Any]]) -> LineChartData{
        
        let set_MA10 = getMA10LineChartSet(with: data)
        let set_MA50 = getMA50LineChartSet(with: data)
        
        return LineChartData(dataSets: [set_MA10,set_MA50])
    }
    
    // MARK: 初始化柱状图数据
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
    
    // MARK: 获取MA10折线图数据
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
    // MARK: 获取MA50折线图数据
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
    
    // MARK: 设置折线图
    private func configLineSet(_ set:LineChartDataSet){
        set.lineWidth = 1
        set.mode = .cubicBezier
        set.drawValuesEnabled = false
        set.drawCirclesEnabled = false
        set.highlightEnabled = false
    }
    
    // MARK: 显示数据MarkView标签
    private func showMarkView(value:String) {
        let marker = MarkerView.init(frame: CGRect(x: 0, y: 0, width: 120, height: 90))
        marker.chartView = self.combinedChartView
        let label = UILabel.init(frame: CGRect(x: 0, y: 0, width: 120, height: 90))
        label.numberOfLines = 0
        label.text = value
        label.textColor = .white
        label.font = UIFont.systemFont(ofSize: 12)
        label.backgroundColor = .gray
//        label.textAlignment = .center
        marker.addSubview(label)
        self.combinedChartView.marker = marker
    }
}

extension KLineView : ChartViewDelegate {
    
    func chartValueSelected(_ chartView: ChartViewBase, entry: ChartDataEntry, highlight: Highlight) {
        
        if let candleEntry = entry as? CandleChartDataEntry {
            var value = ""
            let jsonArray = JSON(data[Int(candleEntry.x)])

            let open = jsonArray[1].stringValue.nsDecimalValue
            let openString = "开:\(open.stringValue)" + "\n"
            MyLog(openString)
            value += openString

            let close = jsonArray[4].stringValue.nsDecimalValue
            let closeString = "收:\(close.stringValue)" + "\n"
            MyLog(closeString)
            value += closeString

            let high = jsonArray[2].stringValue.nsDecimalValue
            let highString = "高:\(high.stringValue)" + "\n"
            MyLog(highString)
            value += highString

            let low = jsonArray[3].stringValue.nsDecimalValue
            let lowString = "低:\(low.stringValue)" + "\n"
            MyLog(lowString)
            value += lowString

            let volume = jsonArray[5].stringValue.nsDecimalValue
            let volumeString = "交易量:\(volume.stringValue)" + "\n"
            MyLog(volumeString)
            value += volumeString

            let timestamp = jsonArray[0].doubleValue
            MyLog(timestamp)
            let timeString = Date(timeIntervalSince1970: timestamp/1000).dateFormatter()
            let time = "时间:\(timeString)"
            MyLog(time)
            value += time

            showMarkView(value: value)

            barChartView.highlightValue(x: candleEntry.x, dataSetIndex: 0, callDelegate:false)
            
        }else if let barEntry = entry as? BarChartDataEntry {
            combinedChartView.highlightValue(x: barEntry.x, dataSetIndex: 0, dataIndex: 1, callDelegate:true)
            
        }
    }
    
    func chartValueNothingSelected(_ chartView: ChartViewBase) {
        barChartView.highlightValues(nil)
        combinedChartView.highlightValues(nil)
    }
    
    
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
    private var data = [[Any]]()
    init(dateFormat:String = "yy-MM-dd", data:[[Any]]) {
        super.init()
        dateFormatter.dateFormat = dateFormat
        self.data = data
    }
    
    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        if let dataArray = data[safe:Int(value)] {
            let timestamp = JSON(dataArray)[0].doubleValue
            return dateFormatter.string(from: Date.init(timeIntervalSince1970: timestamp/1000))
        }
        return ""
    }
}

extension String {
    var doubleValue:Double{
        get{
            return NSDecimalNumber.init(string: self).doubleValue
        }
    }
    var nsDecimalValue:NSDecimalNumber{
        return NSDecimalNumber(string: self)
    }

}
extension Date {
    func dateFormatter(_ dataFormat:String = "yyyy-MM-dd") -> String{
        let dataFormatter = DateFormatter()
        dataFormatter.dateFormat = dataFormat
        return dataFormatter.string(from: self)
    }
}
//MARK: - Array
extension Array {
    subscript (safe index:Int) -> Element? {
        return (0..<count).contains(index) ? self[index] : nil
    }
    
    public func object(at index:Int) -> Element? {
        return (0..<count).contains(index) ? self[index] : nil
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
