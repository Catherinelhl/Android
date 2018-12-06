//
//  KLineViewController.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/11/15.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class KLineViewController: UIViewController {
    
    private let kLineView = KLineView()

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.view.backgroundColor = .white
        
        NotificationCenter.default.addObserver(self, selector: #selector(screenRotateAction(_:)), name: UIApplication.didChangeStatusBarOrientationNotification, object: nil)
        
        self.view.addSubview(kLineView)
        
        setPortraitLayout()
        
        getData()
    }
    
    private func setPortraitLayout() {
        kLineView.snp.remakeConstraints { (make) in
            make.centerY.equalToSuperview()
            if #available(iOS 11.0, *) {
                make.leading.trailing.equalTo(self.view.safeAreaLayoutGuide)
            } else {
                make.leading.trailing.equalToSuperview()
            }
            make.height.equalTo(320)
        }
    }
    
    private func setLandscapeLayout() {
        kLineView.snp.remakeConstraints { (make) in
            if #available(iOS 11.0, *) {
                make.edges.equalTo(self.view.safeAreaLayoutGuide).inset(UIEdgeInsets(top: 8, left: 0, bottom: 8, right: 0))
            } else {
                make.edges.equalToSuperview().inset(UIEdgeInsets(top: 8, left: 0, bottom: 8, right: 0))
            }
        }
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
    
    @objc private func screenRotateAction(_ notification:Notification) {
        if UIApplication.shared.statusBarOrientation.isPortrait {
            self.setPortraitLayout()
        }else {
            self.setLandscapeLayout()
        }
        
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }

}

