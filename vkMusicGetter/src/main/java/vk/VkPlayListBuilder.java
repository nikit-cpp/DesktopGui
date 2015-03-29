package vk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.nikit.cpp.player.Image;
import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;

public class VkPlayListBuilder {
	
	private static final Logger LOGGER = Logger.getLogger(VkPlayListBuilder.class);
	private DocumentBuilder builder;
	private String masterUrl;

	public VkPlayListBuilder(String masterUrl) throws ParserConfigurationException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builder = builderFactory.newDocumentBuilder();
		this.masterUrl = masterUrl;
	}

	public int getGidFromShortName(String shortName) throws VkPlayListBuilderException {
		URL url;
		try {
			url = new URL(masterUrl + "/method/groups.getById.xml?group_ids="
							+ shortName);
			InputStream is = url.openStream();

			Document xmlDocument = builder.parse(is);

			XPath xPath = XPathFactory.newInstance().newXPath();

			String expression = "/response/group/gid";
			String gid = xPath.compile(expression).evaluate(xmlDocument);
			LOGGER.debug("gid=" + gid);

			return Integer.parseInt(gid);

		} catch (IOException | XPathExpressionException | SAXException e) {
			throw new VkPlayListBuilderException("Some errors on gid getting", e);
		}
	}

	
	public PlayList getPlayListFromPost(Node post){
		List<Song> list = new ArrayList<Song>();
		PlayList pl = new PlayList(list);

		Element postElement = (Element) post;
		LOGGER.debug("post id : "+ postElement.getElementsByTagName("id").item(0).getTextContent());
		
		Node attNode = postElement.getElementsByTagName("attachments").item(0);
		if(attNode==null)
			return pl;
		NodeList attachments = attNode.getChildNodes();

		LOGGER.debug("\tattchments: " + attachments.getLength());
		// для каждого аттачмента - песни например, получаем интересующие нас аттрибуты
		Image image = null;
		for(int j=0; j<attachments.getLength(); ++j) {
			Node attItemNode = attachments.item(j);
			if(attItemNode.getNodeType() == Node.ELEMENT_NODE){
				Element attElement = (Element) attItemNode;
				
				LOGGER.debug("\t\t===");
				String type = attElement.getElementsByTagName("type").item(0).getTextContent();
				LOGGER.debug("\t\ttype : "+ type);
				
				Song song = null;
				
				switch(type){
				case "photo":
					image = makeImage(attElement);
					break;
				case "audio":
					song = makeSong(attElement);
					break;
				}
				if(song != null && image != null) {
					song.setImage(image.getBytes());
					list.add(song);
				}
				LOGGER.debug("\t\t===");
			}
		}

		
		
		return pl;
	}
	
	public Collection<PlayList> getPlayListsFromGroup(int gid) throws VkPlayListBuilderException {
		Collection<PlayList> pls = new ArrayList<PlayList>();
		try {
			URL url = new URL(masterUrl + "/method/wall.get.xml?owner_id=-" + gid);
			InputStream is = url.openStream();
			Document xmlDocument = builder.parse(is);
			NodeList posts = xmlDocument.getElementsByTagName("post");
			LOGGER.debug("lenght=" + posts.getLength());
			for (int i = 0; i < posts.getLength(); i++) {
				Node post = posts.item(i);
				if (post.getNodeType() == Node.ELEMENT_NODE) {
					PlayList pl = getPlayListFromPost(post);
					pls.add(pl);
				}				
			}
		} catch (IOException | SAXException e) {
			throw new VkPlayListBuilderException("Some errors on songs getting", e);
		}
		return pls;

	}
	
	public Collection<PlayList> getPlayListsFromGroup(String groupShortName) throws VkPlayListBuilderException {
		
		int groupId = getGidFromShortName(groupShortName);
		return getPlayListsFromGroup(groupId);
	}
	
	private Image makeImage(Element attElement) {
		String src_big = attElement.getElementsByTagName("src_big").item(0).getTextContent();
		LOGGER.debug("\t\t\t" + src_big);
		return new Image(src_big);
	}

	private Song makeSong(Element attElement) {
		String artist = attElement.getElementsByTagName("artist").item(0).getTextContent();
		String title = attElement.getElementsByTagName("title").item(0).getTextContent();
		String url = attElement.getElementsByTagName("url").item(0).getTextContent();
		
		LOGGER.debug("\t\t\t" + artist);
		LOGGER.debug("\t\t\t" + title);
		LOGGER.debug("\t\t\t" + url);

		return new Song(artist, title, url);
	}
}
