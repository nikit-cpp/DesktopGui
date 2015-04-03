package player;
// Thanks to Josh M
// http://stackoverflow.com/questions/12057214/jlayer-pause-and-resume-song/12057216#12057216
public class CustomPlayerRunner {

	public static void main(String[] args) throws InterruptedException {
		CustomPlayer player = new CustomPlayer();
		player.stop();
		player.play(CustomPlayer.UNEXISTED_POSITION);
		
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
