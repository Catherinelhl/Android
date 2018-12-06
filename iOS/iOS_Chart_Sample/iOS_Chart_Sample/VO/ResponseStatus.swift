//
//  ResponseStatus.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/12/6.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import HandyJSON

class ResponseStatus: HandyJSON {

    var timestamp : String?
    var error_code : Int?
    var error_message : String?
    var elapsed : Int?
    var credit_count : Int?
    
    required init() {
    }
}
