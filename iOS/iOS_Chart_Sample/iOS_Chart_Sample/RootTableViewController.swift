//
//  RootTableViewController.swift
//  iOS_Chart_Sample
//
//  Created by Dong on 2018/12/5.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit

class RootTableViewController: UITableViewController {
    
    private let dataSource = ["K线图","折线图"]
    
    private let cellId = "RootTableViewCell"

    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "图表列表"
        
        tableView.tableFooterView = UIView()
    }

    // MARK: - Table view data source

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: cellId) ?? UITableViewCell(style: .default, reuseIdentifier: cellId)

        cell.textLabel?.text = dataSource[indexPath.row]

        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.row {
        case 0:
            let kLineVC = KLineViewController()
            kLineVC.title = dataSource[indexPath.row]
            self.navigationController?.pushViewController(kLineVC, animated: true)
        case 1:
            let lineVC = LineViewController()
            lineVC.title = dataSource[indexPath.row]
            self.navigationController?.pushViewController(lineVC, animated: true)
        default:
            break
        }
    }
    

}
