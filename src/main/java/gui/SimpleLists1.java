package gui;

//SimpleLists.java
//Простейший способ создания списков
import javax.swing.*;
import java.util.*;

public class SimpleLists1 extends JFrame {
	// данные для списков
	private String[] data1 = { "Один", "Два", "Три", "Четыре", "Пять" };
	private String[] data2 = { "Просто", "Легко", "Элементарно",
			"Как дважды два" };

	public SimpleLists1() {
		super("SimpleLists1");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// создаем списки
		JPanel contents = new JPanel();

		// динамически наполним вектор
		Vector big = new Vector();
		for (int i = 0; i < 500; i++) {
			big.add("# " + i + " olololollolololololololololololo");
		}
		JList bigList = new JList(big);
		bigList.setPrototypeCellValue("12345");
		// добавим списки в панель
		contents.add(new JScrollPane(bigList));
		// выведем окно на экран
		setContentPane(contents);
		setSize(300, 200);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SimpleLists1();
	}
}