//
//  Song.swift
//  DesafioS2IT
//
//  Created by Davi on 17/09/17.
//  Copyright © 2017 Davi. All rights reserved.
//

import Foundation
import Gloss

class Song: Decodable {
    
    var trackId: Int?
    var trackName: String?
    var artistName: String?
    var artworkUrl30: String?
    
    
    init(){
        
    }
    
    required init?(json: JSON) {
        self.trackId = "trackId" <~~ json
        self.trackName = "trackName" <~~ json
        self.artistName = "artistName" <~~ json
        self.artworkUrl30 = "artworkUrl30" <~~ json
    }
    
}
