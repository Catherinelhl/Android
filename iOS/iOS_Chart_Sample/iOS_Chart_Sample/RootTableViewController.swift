//
//  RootTableViewController.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/12/5.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class RootTableViewController: UITableViewController {
    
//    private let refreshControl = UIRefreshControl.init()
    
    private var dataSource:[[Any]] = [["K线图"]]
    
    private let cellId = "RootTableViewCell"
    
    private let urlString = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"

    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "图表列表"
        
        tableView.tableFooterView = UIView()
        
        setRefreshControl()
        
        getCoinList()
    }
    
    private func setRefreshControl() {
        self.refreshControl = UIRefreshControl.init()
        self.refreshControl?.addTarget(self, action: #selector(getCoinList), for: .valueChanged)
        
        self.refreshControl?.beginRefreshing()
        
    }
    
    @objc private func getCoinList() {
        var header = SessionManager.defaultHTTPHeaders
        header["X-CMC_PRO_API_KEY"] = "280c9a41-9dc2-496a-8a6c-4b4063be9345"
        SessionManager.default.request(urlString,headers:header).responseJSON { (response) in
            self.refreshControl?.endRefreshing()
            switch response.result {
            case .success(let value):
                let json = JSON.init(value)
                if let coinList = CoinList.deserialize(from: json.dictionaryObject)?.data {
                    
                    if self.dataSource.count > 1 {
                        self.dataSource.removeLast()
                    }
                    
                    self.dataSource.append(coinList)
                    
                    self.tableView.reloadData()
                }
                
                
            case .failure(let aError):
                MyLog(aError)
            }
        }
    }

    // MARK: - Table view data source
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if section == 0 {
            return "交易所数据"
        }else {
            return "货币列表"
        }
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return dataSource.count
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource[section].count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: cellId) ?? UITableViewCell(style: .subtitle, reuseIdentifier: cellId)

        if indexPath.section == 0 {
            cell.textLabel?.text = dataSource[indexPath.section][indexPath.row] as? String
            cell.detailTextLabel?.text = ""
        }else {
            if let coinInfo = dataSource[indexPath.section][indexPath.row] as? CoinInfo ,
                let coinName = coinInfo.name ,
                let coinSymbol = coinInfo.symbol ,
                let usdPrice = coinInfo.quote?.USD?.price?.stringValue ,
                let usdMarketValue = coinInfo.quote?.USD?.market_cap?.stringValue{
                
                cell.textLabel?.text = coinName + "（\(coinSymbol)）"
                cell.detailTextLabel?.numberOfLines = 0
                cell.detailTextLabel?.text = "价格：$ \(usdPrice)" + "\n" + "市值：$ \(usdMarketValue)"
            }
        }

        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.section {
        case 0:
            let kLineVC = KLineViewController()
            kLineVC.title = dataSource[indexPath.section][indexPath.row] as? String
            self.navigationController?.pushViewController(kLineVC, animated: true)
        case 1:
            let cell = tableView.cellForRow(at: indexPath)
            let lineVC = LineViewController.init(coinName: (dataSource[indexPath.section][indexPath.row] as? CoinInfo)?.slug)
            lineVC.title = cell?.textLabel?.text
            self.navigationController?.pushViewController(lineVC, animated: true)
        default:
            break
        }
    }
    

}
