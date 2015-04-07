package service;

import java.awt.Event;

import org.sikuli.basics.HotkeyEvent;
import org.sikuli.basics.HotkeyListener;
import org.sikuli.basics.HotkeyManager;
import org.sikuli.script.RunTime;

import com.melloware.jintellitype.JIntellitype;

public class KeyboardService {
	void m() {
		//RunTime.loadLibrary("c:\\Programming\\Examples-java\\workspace\\SikuliX2_my\\lib\\windows\\JIntellitype.dll");
		
		JIntellitype.setLibraryLocation("c:\\Programming\\Examples-java\\workspace\\keyboard-windows\\lib\\JIntellitype.dll");
		HotkeyManager hkm = HotkeyManager.getInstance();
		hkm.addHotkey('v', Event.ALT_MASK, new HotkeyListener() {
			
			@Override
			public void hotkeyPressed(HotkeyEvent e) {
				System.out.println("Pressed " + e.keyCode + " " + e.modifiers);
			}
		});
	}

	public static void main(String... args){
		KeyboardService k = new KeyboardService();
		k.m();
	}
}
