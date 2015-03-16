package gui;

//SimpleLists.java
//Простейший способ создания списков
import javax.swing.*;

import java.util.*;

public class SimpleLists extends JFrame {
	private static final long serialVersionUID = 1L;

	public SimpleLists() {
		super("SimpleLists");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// создаем списки
		JPanel contents = new JPanel();
		// динамически наполним вектор
		Vector<String> big = new Vector<String>();
		for (int i = 0; i < 50; i++) {
			big.add("# " + i);
		}
		JList<Vector<String>> bigList = new JList(big);
		//bigList.setPrototypeCellValue("12345");
		// добавим списки в панель
		contents.add(new JScrollPane(bigList));
		// выведем окно на экран
		setContentPane(contents);
		setSize(300, 200);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SimpleLists();
	}
}