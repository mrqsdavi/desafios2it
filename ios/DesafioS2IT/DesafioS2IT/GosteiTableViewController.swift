//
//  GosteiTableViewController.swift
//  DesafioS2IT
//
//  Created by Davi on 17/09/17.
//  Copyright © 2017 Davi. All rights reserved.
//

import UIKit
import CoreData
import SDWebImage

class GosteiTableViewController: UITableViewController {
    
    var songs: [NSManagedObject] = []

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        guard let appDelegate =
            UIApplication.shared.delegate as? AppDelegate else {
                return
        }
        
        let managedContext =
            appDelegate.persistentContainer.viewContext
        
        let fetchRequest =
            NSFetchRequest<NSManagedObject>(entityName: "SongEntity")
        
        do {
            songs = try managedContext.fetch(fetchRequest)
            self.tableView.reloadData()
        } catch let error as NSError {
            print("Could not fetch. \(error), \(error.userInfo)")
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }


    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return songs.count
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 40
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "songcell", for: indexPath) as! SongTableViewCell

        // Configure the cell...
        let song: Song = managedObjectToSong(obj: self.songs[indexPath.row])
        
        let url = URL(string: song.artworkUrl30!.replacingOccurrences(of: "30x30", with: "100x100"))!
        
        cell.title.text = song.trackName
        cell.detail.text = song.artistName
        cell.imageIcon.sd_setImage(with: url, placeholderImage: UIImage(named: ""))
        
        return cell
    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange sectionInfo: NSFetchedResultsSectionInfo, atSectionIndex sectionIndex: Int, for type: NSFetchedResultsChangeType) {
        switch type {
        case .insert:
            tableView.insertSections(IndexSet(integer: sectionIndex), with: .fade)
        case .delete:
            tableView.deleteSections(IndexSet(integer: sectionIndex), with: .fade)
        default:
            break;
        }
    }

    func managedObjectToSong(obj: NSManagedObject) -> Song {
        let song = Song()
        song.trackId = obj.value(forKeyPath: "trackId") as? Int
        song.trackName = obj.value(forKeyPath: "trackName") as? String
        song.artistName = obj.value(forKeyPath: "artistName") as? String
        song.artworkUrl30 = obj.value(forKeyPath: "artworkUrl30") as? String
        
        return song
    }
}
