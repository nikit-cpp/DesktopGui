package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import player.PlayFinished;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import config.Config;
import events.DownloadEvent;
import events.PlayEvent;
import service.DownloadService;
import service.DownloadServiceException;
import service.PlayerService;
import utils.IOHelper;
import vk.VkPlayListBuilder;
import vk.VkPlayListBuilderException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow extends JFrame {
	
	private static final String SPRING_CONFIG = "spring-config.xml";

	private static Config config;
	private static VkPlayListBuilder playlistBuilder;
	private static EventBus eventBus;
	
	private static Logger LOGGER = Logger.getLogger(MainWindow.class);
	private static final long serialVersionUID = 1L;
	private JList list;
	private JPanel contents;
	private JLabel statusLabel;


	public MainWindow() throws ParserConfigurationException,
	VkPlayListBuilderException {
		initNonGui();
		eventBus.register(this);
		
		setTitle("List Model Example");
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		
		Collection<PlayList> cpl = new ArrayList<PlayList>();
		for (String groupName : config.getGroupNames()){
			cpl.addAll(playlistBuilder.getPlayListsFromGroup(groupName));
		}
		
		final PlayListListModel playListModel = new PlayListListModel(cpl);
		list = new JList(playListModel);
		//

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
					Song s = (Song) playListModel.getElementAt(index);
					eventBus.post(new DownloadEvent(s));

					LOGGER.debug("Double clicked on item " + index + " " + s);

				}
			}
		});

		//add(pane, BorderLayout.CENTER); // CENTER раскукоживает
		instance = this;
		contents = new JPanel();
		contents.setLayout(new BorderLayout());

		getContentPane().add(contents);
		contents.add( new JScrollPane(list) );
		
		// create the status bar panel and shove it down the bottom of the frame
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("Ready");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
	}
	public static void main(String[] args) throws ParserConfigurationException, VkPlayListBuilderException {
		
		MainWindow mainFrame = new MainWindow();
		mainFrame.setVisible( true );
	}
	
	static MainWindow instance = null;
	public static MainWindow getInstance(){
		return instance ;
	}
	
	private void initNonGui(){
		// Non-GUI work
		ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_CONFIG);

		playlistBuilder = (VkPlayListBuilder)context.getBean("vkPlaylistBuilder");
	    config = (Config)context.getBean("config");
	    eventBus = (EventBus) context.getBean("eventBus");
	    DownloadService downloadService = (DownloadService) context.getBean("downloader");
	    PlayerService pls = (PlayerService) context.getBean("playerService");
	    eventBus.register(downloadService);
	    eventBus.register(pls);

	}
	
	@Subscribe
	public void onPlay(PlayEvent e) throws DownloadServiceException {
		final String s = e.getPath();
		final String message = "Playing '" + s + "'";
		LOGGER.debug(message);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText(message);
			}
		});
	}
	
	@Subscribe
	synchronized public void play(PlayFinished e){
		LOGGER.debug("Play finished");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText("Stopped");
			}
		});
	}

	
	@Subscribe
	public void onDownload(DownloadEvent e) throws DownloadServiceException {
		final String s = e.getSong().toString();
		final String message = "Downloading '" + s + "'";
		LOGGER.debug(message);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText(message);
			}
		});
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
