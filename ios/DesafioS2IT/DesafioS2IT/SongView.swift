//
//  SongView.swift
//  DesafioS2IT
//
//  Created by Davi on 17/09/17.
//  Copyright © 2017 Davi. All rights reserved.
//

import UIKit

class SongView: UIView {
    
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var detail: UILabel!
    @IBOutlet weak var image: UIImageView!

    class func instanceFromNib() -> SongView {
        return UINib(nibName: "SongView", bundle: nil).instantiate(withOwner: nil, options: nil)[0] as! SongView
    }

}
