import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class TestPaint {
	

	public TestPaint() {
		
		JLabel label = new JLabel();
		JLabel labelHeading = new JLabel();
		JLabel labelLookup = new JLabel(new ImageIcon(".\\lookup.png"));
		JFrame morseFrame = new MorseKeyListener(label);
		Border border = new EmptyBorder(15, 15, 15, 15);
		JButton saveButton = new JButton("Save");
		
		String heading = "<html><h1>-- --- .-. ... . ....... -.-. --- -.. . ....... .. -. .--. ..- - ....... .- .--. .--.</h1></html>";
		
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
		morseFrame.add(saveButton, BorderLayout.PAGE_END);
		
		morseFrame.setSize(1100, 900);
		morseFrame.setTitle("-- --- .-. ... . ....... -.-. --- -.. . ....... .. -. .--. ..- - ....... .- .--. .--.");
		morseFrame.setVisible(true);
		
		morseFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
	}

}