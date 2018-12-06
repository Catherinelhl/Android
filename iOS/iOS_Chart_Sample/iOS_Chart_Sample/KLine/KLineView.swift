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
        
        // 设置组合图表（蜡烛图，折线图）
        setCombinedChartView()
        
        // 设置柱状图
        setBarChartView()
        
        // 设置视图约束
        setSubviewsLayout()
    }
    
    // MARK: 设置组合图表
    private func setCombinedChartView() {
        
//        combinedChartView.backgroundColor = .orange
        // Y轴自动调节上下限
        combinedChartView.autoScaleMinMaxEnabled = true
        // Y轴是否可以缩放
        combinedChartView.scaleYEnabled = false
        // 是否显示图表介绍文本
        combinedChartView.chartDescription?.enabled = false
        /// 是否开启双指放大
        combinedChartView.doubleTapToZoomEnabled = false
        /// 设置上下左右边距
        combinedChartView.minOffset = 0
        /// 设置额外右边距
        combinedChartView.extraRightOffset = 10
        /// 设置额外左边距
        combinedChartView.extraLeftOffset = 10
        /// 是否绘制边框
        combinedChartView.drawBordersEnabled = true
        /// 边框宽度
        combinedChartView.borderLineWidth = 0.7
        
        
        /// X轴
        let xAxis = combinedChartView.xAxis
        /// X轴显示的位置
        xAxis.labelPosition = .bottom
        /// 是否绘制轴上的文本
        xAxis.drawLabelsEnabled = false
        /// 是否绘制轴的格子线
        xAxis.drawGridLinesEnabled = false
        
        /// 左侧Y轴
        let leftAxis = combinedChartView.leftAxis
        /// 是否绘制轴上的文本
        leftAxis.drawLabelsEnabled = false
        /// 是否绘制轴的格子线
        leftAxis.drawGridLinesEnabled = false
        
        /// 右侧Y轴
        let rightAxis = combinedChartView.rightAxis
        /// 轴上文本的位置
        rightAxis.labelPosition = .insideChart
        /// 设置轴上的格子线的每个虚线的长度（不设置为一条实线）
        rightAxis.gridLineDashLengths = [5.0,5.0]
        
        /// 图表图例
        let legend = combinedChartView.legend
        // 是否绘制在图表内部
        legend.drawInside = true
        // 设置图例在垂直方向上的位置
        legend.verticalAlignment = .top
        
        // 设置渲染 (自定义渲染内容时需要)
        combinedChartView.renderer = KLineCombinedChartRenderer(chart: combinedChartView, animator: combinedChartView.chartAnimator, viewPortHandler: combinedChartView.viewPortHandler)
        
        // 设置图表代理
        combinedChartView.delegate = self
        // 添加视图
        self.addSubview(combinedChartView)
    }
    
    // MARK: 设置柱状图
    private func setBarChartView() {
//        barChartView.backgroundColor = .cyan
        // Y轴自动调节上下限
        barChartView.autoScaleMinMaxEnabled = true
        // Y轴是否可以缩放
        barChartView.scaleYEnabled = false
        // 是否显示图表介绍文本
        barChartView.chartDescription?.enabled = false
        /// 是否开启双指放大
        barChartView.doubleTapToZoomEnabled = false
        /// 是否绘制边框
        barChartView.drawBordersEnabled = true
        /// 边框宽度
        barChartView.borderLineWidth = 0.7
        
        /// X轴
        let xAxis = barChartView.xAxis
        /// X轴显示的位置
        xAxis.labelPosition = .bottom
        /// 是否绘制轴上的文本
        xAxis.drawGridLinesEnabled = false
        
        /// 左侧Y轴
        let leftAxis = barChartView.leftAxis
        /// 是否绘制轴上的文本
        leftAxis.drawLabelsEnabled = false
        /// 是否绘制轴的格子线
        leftAxis.drawGridLinesEnabled = false
        
        /// 右侧Y轴
        let rightAxis = barChartView.rightAxis
        /// 轴上文本的位置
        rightAxis.labelPosition = .insideChart
        /// 设置轴上的格子线的每个虚线的长度（不设置为一条实线）
        rightAxis.gridLineDashLengths = [5.0,5.0]
        
        /// 是否显示图表图例
        barChartView.legend.enabled = false
        
        // 设置渲染 (自定义渲染内容时需要)
        barChartView.renderer = KLineBarChartRenderer.init(dataProvider: barChartView, animator: barChartView.chartAnimator, viewPortHandler: barChartView.viewPortHandler)
        
        // 设置图表代理
        barChartView.delegate = self
        // 添加视图
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
            make.bottom.equalTo(barChartView.snp.top)//.offset(15)
        }
    }
    
    // MARK: - 设置数据
    func setData(_ data:[[Any]]) {
        
        self.data = data
        // 设置组合图表数据
        setCombinedChartData(with: data)
        // 设置柱状图表数据
        setBarChartData(with: data)
        
    }
    // MARK: 设置组合图表数据
    private func setCombinedChartData(with data:[[Any]]) {
        // 初始化图表数据
        let combinedData = CombinedChartData.init()
        // 设置组合图表的蜡烛图数据
        combinedData.candleData = generateCanldeData(with: data)
        // 设置组合图表的折线图数据
        combinedData.lineData = generateLineData(with: data)
        
        // 设置X轴的最大值
        combinedChartView.xAxis.axisMaximum = combinedData.xMax + 5
        // 设置X轴的最小值
        combinedChartView.xAxis.axisMinimum = combinedData.xMin - 5
        // 设置组合图表的数据
        combinedChartView.data = combinedData
        
        let scaleX = combinedData.xMax >= 50 ? CGFloat(combinedData.xMax / 50) : 1
        // 缩放并移动到最后数据的位置
        combinedChartView.zoom(scaleX: scaleX , scaleY: 1, xValue: combinedData.xMax, yValue: combinedData.yMax, axis: .left)
        // 设置至少显示X轴数据为 7
        combinedChartView.setVisibleXRangeMinimum(7)
        
//        let srcMatrix = combinedChartView.viewPortHandler.touchMatrix
//        combinedChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: combinedChartView, invalidate: true)
    }
    
    // MARK: 设置柱状图数据
    private func setBarChartData(with data:[[Any]]) {
        // 设置柱状图数据
        let barData = generateBarData(with: data)
        barChartView.data = barData
        
        let xAxis = barChartView.xAxis
        // 格式化X轴数据显示
        xAxis.valueFormatter = DateValueFormatter(data: data)
        // 设置X轴的最大值
        xAxis.axisMaximum = barData.xMax + 5
        // 设置X轴的最小值
        xAxis.axisMinimum = barData.xMin - 5
        
        let scaleX = barData.xMax >= 50 ? CGFloat(barData.xMax / 50) : 1
        // 缩放并移动到最后数据的位置
        barChartView.zoom(scaleX: scaleX , scaleY: 1, xValue: barData.xMax, yValue: barData.yMax, axis: .left)
        // 设置至少显示X轴数据为 7
        barChartView.setVisibleXRangeMinimum(7)
        
//        let srcMatrix = barChartView.viewPortHandler.touchMatrix
//        barChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: barChartView, invalidate: true)
    }
    
    // MARK: 初始化蜡烛图数据
    private func generateCanldeData(with data:[[Any]]) -> CandleChartData {
        // 初始化数据实例数组
        var entries = [CandleChartDataEntry]()
        
        for (index,dataArray) in data.enumerated() {
            
            let jsonArray = JSON(dataArray)
            
            let open = jsonArray[1].stringValue.doubleValue
            let high = jsonArray[2].stringValue.doubleValue
            let low = jsonArray[3].stringValue.doubleValue
            let close = jsonArray[4].stringValue.doubleValue
            // 初始化数据实例
            let entry = CandleChartDataEntry(x: Double(index), shadowH: high, shadowL: low, open: open, close: close)
            entries.append(entry)
        }
        // 设置蜡烛图数据设置
        let set = CandleChartDataSet(values: entries, label: "ETHBTC")
        // 蜡烛图阴影颜色是否和蜡烛颜色一致
        set.shadowColorSameAsCandle = true
        // 阴影宽度
        set.shadowWidth = 0.7
     
        // 跌数据蜡烛颜色
        set.decreasingColor = .green
        // 跌数据蜡烛颜色是否填满
        set.decreasingFilled = true
        // 涨数据蜡烛颜色
        set.increasingColor = .red
        // 涨数据蜡烛颜色是否填满
        set.increasingFilled = true
        // 是否在每个蜡烛图上绘制数据
        set.drawValuesEnabled = false
        
        // 初始化蜡烛图数据
        return CandleChartData(dataSet: set)
    }
    
    // MARK: 初始化曲线图数据
    private func generateLineData(with data:[[Any]]) -> LineChartData{
        // 初始化MA10折线图数据设置
        let set_MA10 = getMA10LineChartSet(with: data)
        // 初始化MA50折线图数据设置
        let set_MA50 = getMA50LineChartSet(with: data)
        // 初始化折线图数据
        return LineChartData(dataSets: [set_MA10,set_MA50])
    }
    
    // MARK: 初始化柱状图数据
    private func generateBarData(with data:[[Any]]) -> BarChartData {
        // 初始化数据实例数组
        var entries = [BarChartDataEntry]()
        // 设置柱状图柱子颜色数组
        var barColorsArray = [UIColor]()
        for (index,dataArray) in data.enumerated() {
            let jsonArray = JSON(dataArray)
            let open = jsonArray[1].stringValue.doubleValue
            let volume = jsonArray[5].stringValue.doubleValue
            let close = jsonArray[4].stringValue.doubleValue
            
            // 初始化数据实例
            entries.append(BarChartDataEntry(x: Double(index), y: volume))
            // 根据涨跌添加不同的颜色
            if close - open >= 0 {
                barColorsArray.append(.red)
            }else {
                barColorsArray.append(.green)
            }
        }
        // 设置柱状图数据设置
        let set = BarChartDataSet(values: entries, label: "")
        // 设置柱状图每个柱子的颜色
        set.colors = barColorsArray
        // 是否在每个柱子上绘制数据
        set.drawValuesEnabled = false
        
        // 初始化柱状图数据
        return BarChartData(dataSet: set)
    }
    
    // MARK: 获取MA10折线图数据设置
    private func getMA10LineChartSet(with data:[[Any]]) -> LineChartDataSet{
        // 初始化数据实例数组
        var entries = [ChartDataEntry]()
        // 根据收盘价初始化MA10折线图数据
        let closePriceMA10Array = getClosePriceArray(with: data, days: 10)
        for (i , value) in closePriceMA10Array {
            entries.append(ChartDataEntry(x: Double(i), y: value))
        }
        // 初始化折线图数据设置
        let set = LineChartDataSet.init(values: entries, label: "MA(10)")
        // 设置折线图数据
        configLineSet(set)
        // 设置折线颜色
        set.setColor(.cyan)
        return set
    }
    // MARK: 获取MA50折线图数据
    private func getMA50LineChartSet(with data:[[Any]]) -> LineChartDataSet{
        // 初始化数据实例数组
        var entries = [ChartDataEntry]()
        // 根据收盘价初始化MA10折线图数据
        let closePriceMA10Array = getClosePriceArray(with: data, days: 50)
        for (i , value) in closePriceMA10Array {
            entries.append(ChartDataEntry(x: Double(i), y: value))
        }
        // 初始化折线图数据设置
        let set = LineChartDataSet.init(values: entries, label: "MA(50)")
        // 设置折线图数据
        configLineSet(set)
        // 设置折线颜色
        set.setColor(.purple)
        return set
    }
    // MARK: 根据收盘价 和 移动平均周期数 计算均线
    /// 根据收盘价 和 移动平均周期数 计算均线
    ///
    /// - Parameters:
    ///   - data: 数据
    ///   - days: 移动平均周期数
    /// - Returns: (x,y)
    private func getClosePriceArray(with data:[[Any]], days:Int) -> [(x:Int,y:Double)]{
        // 计算公式: MA = (C1+C2+C3+C4+C5+....+Cn)/n （C 为收盘价，n 为移动平均周期数）
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
    
    // MARK: 设置折线图数据
    private func configLineSet(_ set:LineChartDataSet){
        // 设置折线宽度
        set.lineWidth = 1
        // 设置折线绘制样式
        set.mode = .cubicBezier
        // 是否绘制折线图
        set.drawValuesEnabled = false
        // 是否在折点处绘制圆圈
        set.drawCirclesEnabled = false
        // 是否启用高亮
        set.highlightEnabled = false
    }
    
    // MARK: 显示数据MarkView标签
    private func showMarkView(value:String) {
        let textSize = value.textSize(withFontSize: 12)
        let marker = MarkerView.init(frame: CGRect(origin: .zero, size: CGSize(width: textSize.width + 20, height: textSize.height + 20)))
        marker.chartView = self.combinedChartView
        marker.backgroundColor = .clear
        
        let labelView = UIView.init(frame: CGRect(origin: CGPoint(x: 5, y: 5), size: CGSize(width: textSize.width + 10, height: textSize.height + 10)))
        labelView.backgroundColor = .gray
        
        let label = UILabel.init(frame: CGRect(origin: CGPoint(x: 5, y: 5), size: textSize))
        label.numberOfLines = 0
        label.text = value
        label.textColor = .white
        label.font = UIFont.systemFont(ofSize: 12)
        label.backgroundColor = .gray
        
        labelView.addSubview(label)
        marker.addSubview(labelView)
        
        self.combinedChartView.marker = marker
    }
}
// MARK: - 图表代理方法
extension KLineView : ChartViewDelegate {
    // MARK: 图表数据被选中
    func chartValueSelected(_ chartView: ChartViewBase, entry: ChartDataEntry, highlight: Highlight) {
        
        if let candleEntry = entry as? CandleChartDataEntry {
            var value = ""
            let jsonArray = JSON(data[Int(candleEntry.x)])

            let open = jsonArray[1].stringValue.nsDecimalValue
            let openString = "开盘:\(open.stringValue)" + "\n"
//            MyLog(openString)
            value += openString

            let close = jsonArray[4].stringValue.nsDecimalValue
            let closeString = "收盘:\(close.stringValue)" + "\n"
//            MyLog(closeString)
            value += closeString

            let high = jsonArray[2].stringValue.nsDecimalValue
            let highString = "最高:\(high.stringValue)" + "\n"
//            MyLog(highString)
            value += highString

            let low = jsonArray[3].stringValue.nsDecimalValue
            let lowString = "最低:\(low.stringValue)" + "\n"
//            MyLog(lowString)
            value += lowString

            let volume = jsonArray[5].stringValue.nsDecimalValue
            let volumeString = "交易量:\(volume.stringValue)" + "\n"
//            MyLog(volumeString)
            value += volumeString

            let timestamp = jsonArray[0].doubleValue
            MyLog(timestamp)
            let timeString = Date(timeIntervalSince1970: timestamp/1000).dateFormatter()
            let time = "时间:\(timeString)"
//            MyLog(time)
            value += time

            MyLog(value)
            showMarkView(value: value)

            // 蜡烛图被选中时高亮柱状图相对应的数据
            barChartView.highlightValue(x: candleEntry.x, dataSetIndex: 0, callDelegate:false)
            
        }else if let barEntry = entry as? BarChartDataEntry {
            // 柱状图被选中时高亮蜡烛图相对应的数据
            combinedChartView.highlightValue(x: barEntry.x, dataSetIndex: 0, dataIndex: 1, callDelegate:true)
            
        }
    }
    // MARK: 图表数据没有被选中 或 反选时
    func chartValueNothingSelected(_ chartView: ChartViewBase) {
        // 取消高亮
        barChartView.highlightValues(nil)
        combinedChartView.highlightValues(nil)
    }
    
    // MARK: 图表缩放时
    func chartScaled(_ chartView: ChartViewBase, scaleX: CGFloat, scaleY: CGFloat) {
        // 获取当前被缩放图表的 AffineTransform 属性
        let srcMatrix = chartView.viewPortHandler.touchMatrix
        // 把获取到的 AffineTransform 属性刷新到两个图表中
        combinedChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: combinedChartView, invalidate: true)
        barChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: barChartView, invalidate: true)
    }
    // MARK: 图表移动时
    func chartTranslated(_ chartView: ChartViewBase, dX: CGFloat, dY: CGFloat) {
        // 获取当前被缩放图表的 AffineTransform 属性
        let srcMatrix = chartView.viewPortHandler.touchMatrix
        // 把获取到的 AffineTransform 属性刷新到两个图表中
        combinedChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: combinedChartView, invalidate: true)
        barChartView.viewPortHandler.refresh(newMatrix: srcMatrix, chart: barChartView, invalidate: true)
    }
}

// MARK: - 格式化X轴数据为日期
class DateValueFormatter: NSObject ,IAxisValueFormatter {
    private let dateFormatter = DateFormatter.init()
    private var data : [[Any]]?
    init(dateFormat:String = "yy-MM-dd", data:[[Any]]? = nil) {
        super.init()
        dateFormatter.dateFormat = dateFormat
        self.data = data
    }
    
    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        if let data = self.data {
            if let dataArray = data[safe:Int(value)] {
                let timestamp = JSON(dataArray)[0].doubleValue
                return dateFormatter.string(from: Date.init(timeIntervalSince1970: timestamp/1000))
            }
        }else {
            return dateFormatter.string(from: Date.init(timeIntervalSince1970: value/1000))
        }
        return ""
    }
}
