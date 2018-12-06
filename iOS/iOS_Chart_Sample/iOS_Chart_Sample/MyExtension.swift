//
//  MyExtension.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/11/27.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit


extension String {
    var doubleValue:Double{
        get{
            return NSDecimalNumber.init(string: self).doubleValue
        }
    }
    var nsDecimalValue:NSDecimalNumber{
        return NSDecimalNumber(string: self)
    }
    
    public func textSize(withFontSize fontSize:CGFloat) -> CGSize {
        return NSString(string: self).size(withAttributes: [.font:UIFont.systemFont(ofSize: fontSize)])
    }
    
    public func textSize(withFont font:UIFont) -> CGSize {
        return NSString(string: self).size(withAttributes: [.font:font])
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

extension UIColor {
    class func hexColor(_ hex:UInt) -> UIColor {
        return UIColor.init(red: CGFloat((hex & 0xFF0000) >> 16) / 255.0, green: CGFloat((hex & 0x00FF00) >> 8) / 255.0, blue: CGFloat(hex & 0x0000FF) / 255.0, alpha: 1)
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
