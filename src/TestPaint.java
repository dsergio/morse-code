import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestPaint {
	

	public TestPaint() {
		
		JLabel label = new JLabel();
		JLabel label2 = new JLabel();
		label2.setText("   ");
		label.setFont(new Font("Verdana", Font.PLAIN, 14));
	    label.setVerticalAlignment(JLabel.TOP);
		
		
		JFrame myFrame = new MorseKeyListener(label);
		
		myFrame.add(label2, BorderLayout.LINE_START);
		myFrame.add(label, BorderLayout.CENTER);
		myFrame.add(new JLabel(new ImageIcon(".\\lookup.png")), BorderLayout.LINE_END);
		
		myFrame.setSize(950, 800);
		myFrame.setTitle("-- --- .-. ... . ....... -.-. --- -.. . ....... .. -. .--. ..- - ....... .- .--. .--.");
		myFrame.setVisible(true);
		myFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
	}

}