package gui;

//SimpleLists.java
//Простейший способ создания списков
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;

import vk.CurlXPath;
import vk.CurlXPathException;

import java.util.*;

public class SimpleLists extends JFrame {
	
	private static final String GROUP_NAME = "rockmetal80";
	
	private static final long serialVersionUID = 1L;

	public SimpleLists() throws ParserConfigurationException, CurlXPathException {
		super("SimpleLists");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel contents = new JPanel();
		
		CurlXPath cxp = new CurlXPath();
		Collection<PlayList> cpl = cxp.getPlayListsFromGroup(GROUP_NAME);
		PlayList result = new PlayList();
		for(PlayList pl: cpl){
			List<Song> songs = pl.getSongs();
			for(Song s: songs) {
				result.addSong(s);
			}
		}
		
		Vector big = new Vector();
		for (int i = 0; i < result.size(); i++) {
			big.add(result.getSongs().get(i));
		}
		JList bigList = new JList(big);
		contents.setSize(400, 400);
		
		contents.add(new JScrollPane(bigList));

		setContentPane(contents);
		setSize(500, 500);
		setVisible(true);
	}

	public static void main(String[] args) throws ParserConfigurationException, CurlXPathException {
		new SimpleLists();
	}
}