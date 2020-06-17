import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class TestPaint {
	

	public TestPaint() {
		
		Border border = new EmptyBorder(15, 15, 15, 15);
		JLabel label = new JLabel();
		JLabel labelHeading = new JLabel();
		ImageIcon image = new ImageIcon(".\\lookup.png");		
		JLabel labelLookup = new JLabel(image);
		JFrame morseFrame = new MorseKeyListener(label);
		
		
		String title = "-- --- .-. ... . ....... -.-. --- -.. . ....... .. -. .--. ..- - ....... .- .--. .--.";		
		String heading = "<html><h1>" + title + "</h1></html>";
		
		labelHeading.setText(heading);
		labelHeading.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		labelHeading.setHorizontalAlignment(JLabel.CENTER);
		
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
	    label.setVerticalAlignment(JLabel.TOP);
		label.setBorder(border);
		
		labelLookup.setBorder(border);
		
		morseFrame.add(labelHeading, BorderLayout.PAGE_START);
		morseFrame.add(label, BorderLayout.CENTER);
		morseFrame.add(labelLookup, BorderLayout.LINE_END);
		
		morseFrame.setSize(1100, 900);
		morseFrame.setTitle(title);
		morseFrame.setVisible(true);
		
		morseFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
	}

}