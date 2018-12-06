//
//  CoinInfo.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/12/6.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import HandyJSON

class CoinInfo: HandyJSON {

    var id : Int?
    var name : String?
    var symbol : String?
    var slug : String?
    var circulating_supply : Int?
    var total_supply : Int?
    var max_supply : Int?
    var date_added : String?
    var num_market_pairs : Int?
    var tags : [String]?
    
    var platform : String?
    var cmc_rank : Int?
    var last_updated : String?
    var quote : QuoteInfo?
    
    required init() {
    }
}
