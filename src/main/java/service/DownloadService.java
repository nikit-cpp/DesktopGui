package service;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javazoom.jl.player.Player;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import utils.IOHelper;
import vkButtonedMp3Player.CustomPlayer;

import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.Subscribe;

import events.DownloadEvent;
import gui.Config;

public class DownloadService {
	private static Logger LOGGER = Logger.getLogger(DownloadService.class);
	
	private Config config;
	private CustomPlayer player = new CustomPlayer();

	@Subscribe
	public void download(DownloadEvent e) {
		Song s = e.getSong();
		try {
			String filename = s.toString()+".mp3";
			filename = IOHelper.toFileSystemSafeName(filename);
			File dest = new File(config.getCacheFolder(), filename);
			String url = s.getUrl();
			LOGGER.debug("Downloading "+ url +" to " + dest);
			FileUtils.copyURLToFile(new URL(url), dest);
			LOGGER.debug("Downloading complete ");
			
			player.setPath(dest.getAbsolutePath());
			player.play();
		
		} catch (MalformedURLException e1) {
			LOGGER.error("MalformedURLException", e1);
		} catch (IOException e1) {
			LOGGER.error("IOException", e1);
		}
	
	  }

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
