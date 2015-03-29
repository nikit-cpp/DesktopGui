package player;
// Thanks to Josh M
// http://stackoverflow.com/questions/12057214/jlayer-pause-and-resume-song/12057216#12057216
public class CustomPlayerRunner {

	public static void main(String[] args) throws InterruptedException {
		CustomPlayer player = new CustomPlayer();
		player.prepareFor("/media/files596/Мои документы/Моя музыка/music/Accept - Can`t stand the Night.mp3");
		player.play();
		
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
