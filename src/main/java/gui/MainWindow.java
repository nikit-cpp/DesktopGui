package gui;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import events.DownloadEvent;
import service.DownloadService;
import utils.IOHelper;
import vk.CurlXPath;
import vk.CurlXPathException;
import vkButtonedMp3Player.CustomPlayer;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow extends JPanel {
	
	private static final String SPRING_CONFIG = "spring-config.xml";
	private static int THREADS = 4; 

	private static Config config;
	private static CurlXPath cxp;
	private static CustomPlayer player;
	private static EventBus eventBus;
	
	private static Logger LOGGER = Logger.getLogger(MainWindow.class);
	private static final long serialVersionUID = 1L;
	private JList list;


	public MainWindow() throws ParserConfigurationException,
			CurlXPathException {

		
		Collection<PlayList> cpl = new ArrayList<PlayList>();
		for (String groupName : config.getGroupNames()){
			cpl.addAll(cxp.getPlayListsFromGroup(groupName));
		}

		setLayout(new BorderLayout());
		final PlayListListModel dblm = new PlayListListModel(cpl);
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
					Song s = (Song) dblm.getElementAt(index);
					eventBus.post(new DownloadEvent(s));

					LOGGER.debug("Double clicked on item " + index + " " + s);

				}
			}
		});

		add(pane, BorderLayout.CENTER); // CENTER раскукоживает
	}
	
	public static void main(String[] args) throws ParserConfigurationException, CurlXPathException {
		// Non-GUI work
		ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_CONFIG);

		cxp = new CurlXPath();
		player = new CustomPlayer();
	    config = (Config)context.getBean("config");
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
	    eventBus = new AsyncEventBus(executor);
	    DownloadService downloadService = (DownloadService) context.getBean("downloader");
	    eventBus.register(downloadService);

		
		// GUI stuff
		JFrame frame = new JFrame("List Model Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new MainWindow());
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
}


class PlayListListModel extends AbstractListModel {
	// здесь будем хранить данные
	private ArrayList data = new ArrayList();

	public PlayListListModel(Collection<PlayList> pls){
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
