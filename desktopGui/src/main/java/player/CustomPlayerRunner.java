package player;

import java.io.File;

import com.github.nikit.cpp.player.Song;

// Thanks to Josh M
// http://stackoverflow.com/questions/12057214/jlayer-pause-and-resume-song/12057216#12057216
public class CustomPlayerRunner {

	public static void main(String[] args) throws InterruptedException {
		CustomPlayer player = new CustomPlayer(null);
		player.stop();
		Song song = new Song();
		song.setFile(new File("/media/files596/Мои документы/Моя музыка/music/Accept - Can`t stand the Night.mp3"));
		player.play(song);
		
		System.out.println("hu");
		
        // after 5 secs, pause
        Thread.sleep(5000);
        System.out.println("paused");
        player.pause();     

        // after 5 secs, resume
        Thread.sleep(5000);
        System.out.println("resumed");
        player.resume();

	}

}
