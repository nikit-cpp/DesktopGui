package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import player.State;
import events.NextSong;
import events.PlayEvent;
import events.PlayFinished;
import events.PlayStarted;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import config.Config;
import events.DownloadEvent;
import service.DownloadService;
import service.DownloadServiceException;
import service.PlayerService;
import vk.VkPlayListBuilder;
import vk.VkPlayListBuilderException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainWindow extends JFrame {
	
	private static final String SPRING_CONFIG = "spring-config.xml";
	private static final String STOPPED = "Stopped";
	private static Config config;
	private static VkPlayListBuilder playlistBuilder;
	private static EventBus eventBus;
	private static PlayerService playerService;
	
	private static Logger LOGGER = Logger.getLogger(MainWindow.class);
	private static final long serialVersionUID = 1L;
	private JList<Song> list;
	private JPanel contentsPanel;
	private JLabel statusLabel;
	static MainWindow instance = null;
	private JButton btnPrev;
	private JButton btnPlay;
	private JPanel buttonsPanel;
	private JButton btnNext;
	private JButton btnStop;
	private SelectedListCellRenderer listRenderer;


	public MainWindow() throws ParserConfigurationException, VkPlayListBuilderException {
		initNonGui();
		eventBus.register(this);
		instance = this;
		
		setTitle("Vk Caching Player");
		setSize(600, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		
		Collection<PlayList> cpl = new ArrayList<PlayList>();
		for (String groupName : config.getGroupNames()){
			cpl.addAll(playlistBuilder.getPlayListsFromGroup(groupName));
		}
		List<Song> data = new ArrayList<Song>();
		for (PlayList pl: cpl) {
			for (Song s : pl.getSongs()) {
				data.add(s);
			}
		}
		PlayList playList = new PlayList(data);

		playerService.setPlayList(playList);
		
		final PlayListListModel playListModel = new PlayListListModel(playList);
		list = new JList<Song>(playListModel);
		listRenderer = new SelectedListCellRenderer();
		list.setCellRenderer(listRenderer);
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
					eventBus.post(new PlayEvent(s));

					LOGGER.debug("Double clicked on item " + index + " " + s);

				}
			}
		});

		
		contentsPanel = new JPanel();

		getContentPane().add(contentsPanel);
		contentsPanel.setLayout(new BorderLayout(0, 0));
		contentsPanel.add( new JScrollPane(list) , BorderLayout.CENTER);
		
		buttonsPanel = new JPanel();
		contentsPanel.add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnPrev = new JButton("Prev");
		buttonsPanel.add(btnPrev);
		
		btnPlay = new JButton("Play");
		buttonsPanel.add(btnPlay);
		
		btnStop = new JButton("Stop");
		buttonsPanel.add(btnStop);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventBus.post(new NextSong());
			}
		});
		buttonsPanel.add(btnNext);
		
		// create the status bar panel and shove it down the bottom of the frame
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel(STOPPED);
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
	}
	public static void main(String[] args) throws ParserConfigurationException, VkPlayListBuilderException {
		
		MainWindow mainFrame = new MainWindow();
		mainFrame.setVisible( true );
	}
	
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
	    playerService = (PlayerService) context.getBean("playerService");
	    eventBus.register(downloadService);
	    eventBus.register(playerService);
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void onPlayStarted(PlayStarted e) throws DownloadServiceException {
		final String s = e.getPath();
		final String message = "Playing '" + s + "'";
		LOGGER.debug(message);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText(message);
			}
		});
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void onPlayFinished(PlayFinished e){
		LOGGER.debug("Play finished");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText(STOPPED);
			}
		});
	}

	@AllowConcurrentEvents
	@Subscribe
	public void onDownload(DownloadEvent e) throws DownloadServiceException {
		final String message = "Downloading '" + e.getSong().getUrl() + "'";
		LOGGER.debug(message);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				statusLabel.setText(message);
			}
		});
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void play(final PlayEvent e) {
		final int index =playerService.getPlayList().getSongId(e.getSong().getId());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				listRenderer.hilight(index);
				list.updateUI();
			}
		});
	}
	
	public static EventBus getEventBus() {
		return eventBus;
	}
	public static PlayerService getPlayerService() {
		return playerService;
	}
}


class PlayListListModel extends AbstractListModel<Song> {
	private static final long serialVersionUID = 1L;
	// здесь будем хранить данные
	private List<Song> data = new ArrayList<Song>();

	public PlayListListModel(PlayList playList){
		setDataSource(playList);
	}
	private void setDataSource(PlayList playList) {
		try {
			// получаем данные
			data.clear();
			
			synchronized (data) {
				data = playList.getSongs();
			}
			// оповещаем виды (если они есть)
			fireIntervalAdded(this, 0, data.size());
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

	public Song getElementAt(int idx) {
		synchronized (data) {
			return data.get(idx);
		}
	}
}

class SelectedListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	private int hilighted = -1;
	public void hilight(int index){
		hilighted = index;
	}
	
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (index== hilighted) {
            c.setBackground(Color.RED);
        }
        return c;
    }
}
