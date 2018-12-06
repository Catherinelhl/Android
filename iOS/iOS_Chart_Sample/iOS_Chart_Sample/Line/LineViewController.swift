//
//  LineViewController.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/12/5.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class LineViewController: UIViewController {
    
    private let lineView : LineView
    
    init(coinName:String?) {
        lineView = LineView.init(coinName: coinName)
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.view.backgroundColor = .white
        NotificationCenter.default.addObserver(self, selector: #selector(screenRotateAction(_:)), name: UIApplication.didChangeStatusBarOrientationNotification, object: nil)
        
        self.view.addSubview(lineView)
        
        setPortraitLayout()
        
    }
    
    private func setPortraitLayout() {
        lineView.snp.remakeConstraints { (make) in
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
        lineView.snp.remakeConstraints { (make) in
            if #available(iOS 11.0, *) {
                make.edges.equalTo(self.view.safeAreaLayoutGuide).inset(UIEdgeInsets(top: 8, left: 0, bottom: 8, right: 0))
            } else {
                make.edges.equalToSuperview().inset(UIEdgeInsets(top: 8, left: 0, bottom: 8, right: 0))
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
