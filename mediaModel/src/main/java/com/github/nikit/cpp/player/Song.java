package com.github.nikit.cpp.player;

import java.io.File;
import java.util.UUID;

/**
 * Created by nik on 13.02.15.
 */
public class Song {
    private String name;
    private String artist;
    private String album;
    private UUID id;
    private byte[] image;
    private File file;
    
    private String url;
    private String imageUrl;
    
    public Song(){
        id = UUID.randomUUID();
    }

    public Song(String artist2, String title, String url2) {
		this.artist = artist2;
		this.name = title;
		this.url = url2;
		id = UUID.randomUUID();
	}


    public String getUrl() {
		return url;
	}

	public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



	public UUID getId() {
        return id;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public String toString(){
        return artist + " - " + name;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Song other = (Song) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
