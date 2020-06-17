import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class TestPaint {
	

	public TestPaint() {
		
		JLabel label = new JLabel();
		
		label.setFont(new Font("Verdana", Font.PLAIN, 14));
	    label.setVerticalAlignment(JLabel.TOP);
	    
	    Border border = new EmptyBorder(10, 10, 10, 10);
		label.setBorder(border);
		
		JFrame myFrame = new MorseKeyListener(label);
		
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