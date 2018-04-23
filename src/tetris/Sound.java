package tetris;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	public static synchronized void playSound(final String url) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(Sound.class.getResource(url));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}).start();
	}
}
 