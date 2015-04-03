package com.github.nikit.cpp.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nik on 04.03.15.
 */
public class PlayList {
    private List<Song> songs;

    public PlayList() {
        songs = new ArrayList<Song>();
    }

    public PlayList(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Song getSong(UUID id) {
        for (Song c : songs) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
    
    public int getSongId(UUID id) {
    	int index = -1;
    	int j = 0;
        for (Song c : songs) {
            if (c.getId().equals(id)){
            	index = j;
            	break;
            }else{
            	j++;
            }
        }
        return index;
    }
    
    public void addSong(Song s) {
    	songs.add(s);
    }
    
    public int size() {
    	return songs.size();
    }

	public Song getNextSong(Song song) {
		if(song!=null){
			int i = 0;
	        for (Song c : songs) {
	            if (c.getId().equals(song.getId())){
	            	int newIndex = i+1;
	            	if(newIndex!=songs.size())
	                	return songs.get(newIndex);
	            	else
	            		return null;
	            }else{
	            	++i;
	            }
	        }
		}
		return null;
	}
	
	public Song getPrevSong(Song song) {
		if(song!=null){
			int i = 0;
	        for (Song c : songs) {
	            if (c.getId().equals(song.getId())){
	            	int newIndex = i-1;
	            	if(newIndex!=-1)
	                	return songs.get(newIndex);
	            	else
	            		return null;
	            }else{
	            	++i;
	            }
	        }
		}
		return null;
	}
}
