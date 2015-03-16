package gui;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;

import vk.CurlXPath;
import vk.CurlXPathException;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class SimpleLists extends JPanel {
	
	// TODO сделать множество групп
	private static final String GROUP_NAME = "rockmetal80";
	
	private static Logger LOGGER = Logger.getLogger(SimpleLists.class);
	private static final long serialVersionUID = 1L;
	private JList list;


	public SimpleLists() throws ParserConfigurationException,
			CurlXPathException {

		CurlXPath cxp = new CurlXPath();
		Collection<PlayList> cpl = cxp.getPlayListsFromGroup(GROUP_NAME);

		setLayout(new BorderLayout());
		final DatabaseListModel dblm = new DatabaseListModel(cpl);
		list = new JList(dblm);
		JScrollPane pane = new JScrollPane(list);

		list.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = list.locationToIndex(e.getPoint());
					LOGGER.debug("Double clicked on item " + index + " " + dblm.getElementAt(index));
				}
			}
		});

		add(pane, BorderLayout.CENTER); // CENTER раскукоживает
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


class DatabaseListModel extends AbstractListModel {
	// здесь будем хранить данные
	private ArrayList data = new ArrayList();

	public DatabaseListModel(Collection<PlayList> pls){
		setDataSource(pls);
	}
	private void setDataSource(Collection<PlayList> pls) {
		try {
			// получаем данные
			data.clear();
			for (PlayList pl: pls) {
				synchronized (data) {
					List<Song> songs = pl.getSongs();
					for (Song s : songs) {
						data.add(s);
					}
				}
				// оповещаем виды (если они есть)
				fireIntervalAdded(this, 0, data.size());
			}
			fireContentsChanged(this, 0, data.size());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	// методы модели для выдачи данных списку
	public int getSize() {
		synchronized (data) {
			return data.size();
		}
	}

	public Object getElementAt(int idx) {
		synchronized (data) {
			return data.get(idx);
		}
	}
}
