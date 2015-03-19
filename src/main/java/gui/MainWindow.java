package gui;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import events.DownloadEvent;
import service.DownloadService;
import service.PlayService;
import vk.VkPlayListBuilder;
import vk.VkPlayListBuilderException;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow extends JFrame {
	
	private static final String SPRING_CONFIG = "spring-config.xml";
	private static int THREADS = 4; 

	private static Config config;
	private static VkPlayListBuilder cxp;
	private static EventBus eventBus;
	
	private static Logger LOGGER = Logger.getLogger(MainWindow.class);
	private static final long serialVersionUID = 1L;
	private JList list;
	private JPanel contents;


	public MainWindow() throws ParserConfigurationException,
	VkPlayListBuilderException {
		
		initNonGui();
		
		setTitle("List Model Example");
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		
		Collection<PlayList> cpl = new ArrayList<PlayList>();
		for (String groupName : config.getGroupNames()){
			cpl.addAll(cxp.getPlayListsFromGroup(groupName));
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

		cxp = (VkPlayListBuilder)context.getBean("vkPlaylistBuilder");
	    config = (Config)context.getBean("config");
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
	    eventBus = new AsyncEventBus(executor);
	    DownloadService downloadService = (DownloadService) context.getBean("downloader");
	    downloadService.setEventBus(eventBus); // TODO refactor java.util.Executors io spring.xml http://stackoverflow.com/questions/8416655/best-way-to-refactor-this-in-spring/8416805#8416805
	    PlayService pls = new PlayService();
	    eventBus.register(downloadService);
	    eventBus.register(pls);

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
