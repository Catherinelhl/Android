//
//  ViewController.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/11/15.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class ViewController: UIViewController {
    
    private let kLineView = KLineView()
    private let kLineDataView = KLineDataView()

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.view.addSubview(kLineView)
        
        kLineView.snp.makeConstraints { (make) in
            make.centerY.equalToSuperview()
            if #available(iOS 11.0, *) {
                make.leading.trailing.equalTo(self.view.safeAreaLayoutGuide)
            } else {
                make.leading.trailing.equalToSuperview()
            }
            make.height.equalTo(320)
        }
        
        getData()
    }

    // MARK: - 获取币安数据
    private func getData() {
        var param:[String:Any] = ["symbol":"ETHBTC",
                                  "interval":"1d"]
        param["limit"] = 1000  // Int类型 Default 500; max 1000
        SessionManager.default.request("https://api.binance.com/api/v1/klines", method: .get, parameters: param).responseJSON { (response) in
            switch response.result {
            case .success(let value):
                MyLog(value)
                if let data = JSON(value).arrayObject as? [[Any]]{
                    self.kLineView.setData(data)
                }
            case .failure(let aError):
                MyLog(aError)
            }
        }
    }
    

}

