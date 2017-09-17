//
//  MatchViewController.swift
//  DesafioS2IT
//
//  Created by Davi on 17/09/17.
//  Copyright © 2017 Davi. All rights reserved.
//

import UIKit
import CoreData
import MRProgress
import Koloda
import SDWebImage

class MatchViewController: UIViewController, KolodaViewDelegate, KolodaViewDataSource, UIAlertViewDelegate {
    
    @IBOutlet weak var kolodaView: KolodaView!
    @IBOutlet weak var downButton: UIButton!
    @IBOutlet weak var upButton: UIButton!
    
    var songs: NSArray! = NSArray()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.kolodaView.dataSource = self
        self.kolodaView.delegate = self
        
        let downImage = UIImage(named: "down")?.withRenderingMode(.alwaysTemplate)
        let upImage = UIImage(named: "up")?.withRenderingMode(.alwaysTemplate)
        self.downButton.setImage(downImage, for: UIControlState.normal)
        self.downButton.tintColor = UIColor.white
        self.downButton.addTarget(self, action: #selector(dislike), for:UIControlEvents.touchUpInside)
        self.upButton.setImage(upImage, for: UIControlState.normal)
        self.upButton.tintColor = UIColor.white
        self.upButton.addTarget(self, action: #selector(like), for:UIControlEvents.touchUpInside)
        
    }
    
    func like() {
        self.kolodaView.swipe(SwipeResultDirection.right, force: true)
    }
    
    func dislike() {
        self.kolodaView.swipe(SwipeResultDirection.left, force: true)
    }
    
    func saveLike(){
        guard let appDelegate =
            UIApplication.shared.delegate as? AppDelegate else {
                return
        }
        
        let songObject: Song = self.songs.object(at: self.kolodaView.currentCardIndex-1) as! Song
        
        let managedContext = appDelegate.persistentContainer.viewContext
        let entity = NSEntityDescription.entity(forEntityName: "SongEntity", in: managedContext)!
        let song = NSManagedObject(entity: entity, insertInto: managedContext)
        
        song.setValue(songObject.trackId, forKeyPath: "trackId")
        song.setValue(songObject.trackName, forKeyPath: "trackName")
        song.setValue(songObject.artistName, forKeyPath: "artistName")
        song.setValue(songObject.artworkUrl30, forKeyPath: "artworkUrl30")
        do {
            try managedContext.save()
        } catch let error as NSError {
            print("Could not save. \(error), \(error.userInfo)")
        }
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        loadData()
    }
    
    func showError(msgErro: String, tituloErro: String){
        let alertController = UIAlertController(title: tituloErro, message: msgErro, preferredStyle: .alert)
        
        let cancelAction = UIAlertAction(title: "Cancelar", style: .cancel) { action in
            
        }
        alertController.addAction(cancelAction)
        
        let OKAction = UIAlertAction(title: "Tentar novamente", style: .default) { action in
            self.loadData()
        }
        alertController.addAction(OKAction)
        
        self.present(alertController, animated: true) {
            
        }
    }
    
    func loadData(){
        let overlay = MRProgressOverlayView.showOverlayAdded(to: self.view, title: "", mode: .indeterminate, animated: true)
        
        if Reachability.isConnectedToNetwork() {
            DataSource.sharedInstance.songs() {
                (result: NSArray?) in
                overlay?.dismiss(true)
                
                if(result != nil){
                    self.songs = result
                    self.kolodaView.resetCurrentCardIndex()
                }else {
                    self.showError(msgErro: "Um erro inesperado ocorreu", tituloErro: "Erro")
                }
                
                
                
            };
        }else{
            overlay?.dismiss(true)
            showError(msgErro: "Não foi possível uma conexão com a internet", tituloErro: "Sem conexão")
        }
        
        
    }
    
    func kolodaNumberOfCards(_ koloda: KolodaView) -> Int {
        return self.songs.count
    }
    
    func kolodaSpeedThatCardShouldDrag(_ koloda: KolodaView) -> DragSpeed {
        return DragSpeed.moderate
    }
    
    func koloda(_ koloda: KolodaView, viewForCardAt index: Int) -> UIView {
        let songView = SongView.instanceFromNib()
        let song: Song = songs.object(at: index) as! Song
        
        let url = URL(string: song.artworkUrl30!.replacingOccurrences(of: "30x30", with: "600x600"))!
        
        songView.title.text = song.trackName
        songView.detail.text = song.artistName
        songView.image.sd_setImage(with: url, placeholderImage: UIImage(named: ""))
        
        return songView
    }
    
    func koloda(_ koloda: KolodaView, didSwipeCardAt index: Int, in direction: SwipeResultDirection) {
        if direction == SwipeResultDirection.right {
            saveLike()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

}
