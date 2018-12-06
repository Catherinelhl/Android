//
//  LineView.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/12/6.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import Charts

class LineView: UIView {
    
    private let segmentedControl = UISegmentedControl(items: ["1d","7d","1m","3m","1y","YTD","ALL"])
    
    private let lineChartView = LineChartView()
    
    private let activityIV = UIActivityIndicatorView.init(style: .gray)
    
    private var urlString = "https://graphs2.coinmarketcap.com/currencies/"
    
    private let calendar = Calendar.current as NSCalendar

    init(coinName:String?) {
        super.init(frame: .zero)
        
        if coinName != nil {
            createSubviews()
            self.urlString = urlString + coinName! + "/"
            getData(with: urlString)
        }
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    // MARK: - 创建子视图
    private func createSubviews() {
        
        setSegmentedControl()
        
        setLineChartView()
        
        setActivityIndicatorView()
        
        segmentedControl.snp.makeConstraints { (make) in
            make.top.equalToSuperview()
            make.leading.equalToSuperview().offset(8)
            make.trailing.equalToSuperview().offset(-8)
            make.height.equalTo(25)
        }
        
        lineChartView.snp.makeConstraints { (make) in
            make.top.equalTo(segmentedControl.snp.bottom).offset(8)
            make.bottom.leading.trailing.equalToSuperview()
        }
        
        activityIV.snp.makeConstraints { (make) in
            make.center.equalTo(lineChartView)
        }
    }
    
    // MARK: 设置分段视图
    private func setSegmentedControl() {
        
        segmentedControl.selectedSegmentIndex = 6
        segmentedControl.addTarget(self, action: #selector(segmentedValueChangedAction(_:)), for: .valueChanged)
        
        self.addSubview(segmentedControl)
    }
    
    // MARK: 设置折线图
    private func setLineChartView() {
        
        lineChartView.scaleYEnabled = false
        lineChartView.autoScaleMinMaxEnabled = true
        lineChartView.chartDescription?.enabled = false
        
        let xAxis = lineChartView.xAxis
        xAxis.labelPosition = .bottom
        
        let rightAxis = lineChartView.rightAxis
        rightAxis.enabled = false
        
        let marker = BalloonMarker(color: UIColor(white: 180/255, alpha: 1),
                                   font: .systemFont(ofSize: 11),
                                   textColor: .white,
                                   insets: UIEdgeInsets(top: 8, left: 8, bottom: 20, right: 8))
        marker.chartView = lineChartView
        marker.minimumSize = CGSize(width: 40, height: 20)
        lineChartView.marker = marker
        
        self.addSubview(lineChartView)
    }
    
    // MARK: 设置加载指示器
    private func setActivityIndicatorView() {
        activityIV.hidesWhenStopped = true
        
        self.addSubview(activityIV)
    }
    
    // MARK: 设置图表数据
    private func setData(_ data:[[Double]]) {
        
        let lineData = generateLineData(data)
        lineChartView.data = lineData
        
        let xAxis = lineChartView.xAxis
        xAxis.axisMaximum = lineData.xMax + 5
        xAxis.axisMinimum = lineData.xMin - 5
        
        if segmentedControl.selectedSegmentIndex == 0 {
            xAxis.valueFormatter = DateValueFormatter(dateFormat: "HH:mm" ,data:data)
        }else {
            xAxis.valueFormatter = DateValueFormatter(data:data)
        }
        
        lineChartView.animate(xAxisDuration: 1.5)
    }
    
    private func generateLineData(_ data:[[Double]]) -> LineChartData {
        var entries = [ChartDataEntry]()
        for (index, dataArray) in data.enumerated() {
            let x = Double(index)
            let y = dataArray.last ?? 0
            entries.append(ChartDataEntry(x: x, y: y))
        }
        
        
        let set = LineChartDataSet(values: entries, label: "USD")
        
        set.drawIconsEnabled = false
        set.drawValuesEnabled = false
        set.setColor(.black)
        set.drawCirclesEnabled = false
        
        let gradientColors = [ChartColorTemplates.colorFromString("#00ff0000").cgColor,
                              ChartColorTemplates.colorFromString("#ffff0000").cgColor]
        let gradient = CGGradient(colorsSpace: nil, colors: gradientColors as CFArray, locations: nil)!
        
        set.fillAlpha = 1
        set.fill = Fill(linearGradient: gradient, angle: 90)
        set.drawFilledEnabled = true
        
        return LineChartData(dataSet: set)
        
    }
    
    // MARK: - 获取数据
    private func getData(with urlString:String) {
        self.loading()
        SessionManager.default.request(urlString).responseJSON { (response) in
            self.stopLoading()
            switch response.result {
            case .success(let value) :
                MyLog(value)
                if let usdData = JSON(value)["price_usd"].arrayObject as? [[Double]] {
                    self.setData(usdData)
                }
                
            case .failure(let aError):
                MyLog(aError)
            }
        }
    }

    // MARK: - 事件
    private func loading() {
        segmentedControl.isEnabled = false
        lineChartView.isHidden = true
        activityIV.startAnimating()
    }
    
    private func stopLoading() {
        self.segmentedControl.isEnabled = true
        lineChartView.isHidden = false
        activityIV.stopAnimating()
    }
    
    // MARK: - 点击事件
    @objc private func segmentedValueChangedAction(_ sender:UISegmentedControl) {
        
        let endTime = Date().timeIntervalSince1970 * 1000
//        var days:Double = 0
        var startTime:Double = 0
        var component = DateComponents.init()
        switch sender.selectedSegmentIndex {
        case 0:
            component.day = -1
        case 1:
            component.day = -7
        case 2:
            component.month = -1
        case 3:
            component.month = -3
        case 4:
            component.year = -1
        default:
            break
        }
        
        if sender.selectedSegmentIndex < 5 {
            if let startDate = calendar.date(byAdding: component, to: Date()) {
                startTime = startDate.timeIntervalSince1970 * 1000
            }
        }else if sender.selectedSegmentIndex == 5 {
            var firstDate:NSDate? = nil
            var interval:TimeInterval = 0
            calendar.range(of: .year, start: &firstDate, interval: &interval, for: Date())
            if firstDate != nil {
                startTime = firstDate!.timeIntervalSince1970 * 1000
            }
        }
        
        let urlString = self.urlString + "\(Int(startTime))" + "/\(Int(endTime))"
        
        getData(with: urlString)
    }
}
