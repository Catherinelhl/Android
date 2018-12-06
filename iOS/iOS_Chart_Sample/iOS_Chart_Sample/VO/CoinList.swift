//
//  CoinList.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/12/6.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import HandyJSON

class CoinList: HandyJSON {

    var status : ResponseStatus?
    
    var data : [CoinInfo]?
    
    required init() {
    }
}
