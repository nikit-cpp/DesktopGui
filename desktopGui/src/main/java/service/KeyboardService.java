package service;

import java.awt.Event;
import java.io.File;
import java.io.IOException;

import my.superpackage.LibLoader;
import my.superpackage.LoadLibException;

import org.sikuli.basics.FileManager;
import org.sikuli.basics.HotkeyEvent;
import org.sikuli.basics.HotkeyListener;
import org.sikuli.basics.HotkeyManager;
import org.sikuli.basics.LinuxHotkeyManager;
import org.sikuli.script.RunTime;

public class KeyboardService {
	void m() throws IOException, InterruptedException, LoadLibException {
		LibLoader.extractLib();
		HotkeyManager hkm = HotkeyManager.getInstance();

		hkm.addHotkey('v', Event.ALT_MASK, new HotkeyListener() {
			
			@Override
			public void hotkeyPressed(HotkeyEvent e) {
				System.out.println("Pressed " + e.keyCode + " " + e.modifiers);
			}
		});
	}

	public static void main(String... args) throws IOException, InterruptedException, LoadLibException{
		KeyboardService k = new KeyboardService();
		k.m();
	}
}
