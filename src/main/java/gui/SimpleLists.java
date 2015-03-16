package gui;

//SimpleLists.java
//Простейший способ создания списков
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;

import vk.CurlXPath;
import vk.CurlXPathException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class SimpleLists extends JPanel {

	private static final String GROUP_NAME = "rockmetal80";

	private static final long serialVersionUID = 1L;
	private JList list;

	private DefaultListModel model;

	public SimpleLists() throws ParserConfigurationException,
			CurlXPathException {

		CurlXPath cxp = new CurlXPath();
		Collection<PlayList> cpl = cxp.getPlayListsFromGroup(GROUP_NAME);
		PlayList result = new PlayList();
		for (PlayList pl : cpl) {
			List<Song> songs = pl.getSongs();
			for (Song s : songs) {
				result.addSong(s);
			}
		}

		setLayout(new BorderLayout());
		model = new DefaultListModel();
		list = new JList(model);
		JScrollPane pane = new JScrollPane(list);
		JButton addButton = new JButton("Add Element");
		for (int i = 0; i < result.size(); i++) {
			model.addElement(result.getSongs().get(i));
		}
		
		list.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 2) {
		            int index = list.locationToIndex(e.getPoint());
		            System.out.println("Double clicked on Item " + index);
		         }
			}
		});

		add(pane, BorderLayout.NORTH);
	}

	public static void main(String[] args) throws ParserConfigurationException,
			CurlXPathException {
		JFrame frame = new JFrame("List Model Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new SimpleLists());
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
}