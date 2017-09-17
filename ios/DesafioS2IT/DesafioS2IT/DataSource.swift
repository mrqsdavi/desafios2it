//
//  DataSource.swift
//  DesafioS2IT
//
//  Created by Davi on 17/09/17.
//  Copyright © 2017 Davi. All rights reserved.
//

import Foundation
import Alamofire
import Gloss

class DataSource {
    
    static let sharedInstance = DataSource()
    
    var url = "https://itunes.apple.com/search?term=rock";
    
    func songs(completion: @escaping (_ result: NSArray?)->()){
        
        Alamofire.request(url).responseJSON { response in
            print("Request: \(String(describing: response.request))")   // original url request
            print("Response: \(String(describing: response.response))") // http url response
            print("Result: \(response.result)")                         // response serialization result
            
            
            let songs = NSMutableArray()
            
            switch response.result {
            case .success:
                if let dic = response.result.value as! NSDictionary?  {
                    if let objJson = dic.object(forKey: "results") as! NSArray? {
                        for element in objJson {
                            let s: Song = Song(json: element as! JSON)!
                            songs.add(s)
                        }
                    }
                }
                completion(songs)
                
                
            case .failure( _):
                completion(nil)
            }
            
            
        }
        
    }
    
}
