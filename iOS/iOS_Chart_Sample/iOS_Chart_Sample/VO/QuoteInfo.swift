//
//  QuoteInfo.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/12/6.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import HandyJSON

class QuoteInfo: HandyJSON {
    
    var USD : USDInfo?
    
    
    required init() {
    }

}

class USDInfo : HandyJSON {
    
    
    var price : NSNumber?
    var volume_24h : NSNumber?
    var percent_change_1h : NSNumber?
    var percent_change_24h : NSNumber?
    var percent_change_7d : NSNumber?
    var market_cap : NSNumber?
    var last_updated : String?
    
    
    required init() {
    }
}
