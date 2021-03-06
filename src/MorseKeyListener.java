import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MorseKeyListener extends JFrame implements KeyListener {
	
	private static final long serialVersionUID = 1L;

	private JLabel label;
	private int initLevel;
	private char trigger = 0;
	private char startStopScan = 0;
	private char print = 0;
	private char clear = 0;
	private List<Long> press;
	private Character c = 0;
	private List<Character> content;
	private List<String> words;
	private List<String> sentences;
	private List<Double> sentenceWpm;
	private ScanState scanState;
	private long startTime;
	private long endTime;
	private long startPause;
	private long endPause;
	private long startScan;
	private long endScan;
	private double wpm = 0;
	private boolean pressed;
	private int unit = 250;
	private int ditMin = 0;
	private int ditMax = ditMin + unit;
	private int dahMin = ditMax + 1;
	private int dahMax = dahMin + 3 * unit;
	private int letterPause = 3 * unit;
	private int wordPause = 7 * unit;
	
	public enum ScanState {
		NOT_SCANNING,
		SCANNING
	}
	
	public enum SIG {
		DIT,
		DAH
	}
	
	public MorseKeyListener(JLabel label) {
		this.label = label;
		press = new ArrayList<Long>();
		content = new ArrayList<Character>();
		words = new ArrayList<String>();
		sentences = new ArrayList<String>();
		sentenceWpm = new ArrayList<Double>();
		scanState = ScanState.NOT_SCANNING;
		initLevel = 0;
		this.label.setText(getMorseState("Enter trigger key"));
		addKeyListener(this);
		
		startTime = System.currentTimeMillis();
		endTime = System.currentTimeMillis();
		startPause = System.currentTimeMillis();
		endPause = System.currentTimeMillis();
		startScan = System.currentTimeMillis();
		endScan = System.currentTimeMillis();
	}
	
	public String getMorseState() {
		return getMorseState("");
	}
	
	public String getMorseState(String message) {
		
		// todo use stringbuilder
		// StringBuilder b = new StringBuilder();
		
		String morse = "";
		
		for (Long l : press) {
			if (l >= ditMin && l <= ditMax) {
				morse += ".";
			} else if (l >= dahMin && l <= dahMax) {
				morse += "-";
			}
		}
		
		String s = "<html>";
		s += "<p><u>Instructions</u>: Use the 'trigger' key to input 'dit' or 'dah' symbols, ";
		s += "defined by key press time duration (short keypress or long keypress). ";
		s += "Hit 'print' to print the buffer. Hit 'start/stop' to start or stop. In the case of stopping, stop scanning and add the sentence to the output.</p>";
		s += "<br /><u>To write a sentence</u>:<ol>";
		s += "<li>Hit 'start/stop'.</li>";
		s += "<li>Then use the 'trigger' key to input the words in the sentence by Morse code rules (see chart).</li>";
		s += "<li>Then hit 'print' to print the words.</li>";
		s += "<li>Then hit 'start/stop' again to stop scanning and output your sentence.</li>";
		s += "</ol>";
		
		if (message.equals("DOWN")) {
			s += "<b><font color=blue>!! " + message + " -.. --- .-- -.</font></b>";
		} else if (message.equals("UP")) {
			s += "<b><font color=red>!! " + message + " ..- .--.</font></b>";
		} else if (!message.equals("")) {
			s += "<b><font color=red>!! " + message + "</font></b>";
		}
		s += "<br /><br /><b>INPUT KEY CONTROLS</b>";
		s += "<br />" + "trigger: '" + trigger + "'";
		s += "<br />" + "start/stop: '" + startStopScan + "'";
		s += "<br />" + "print: '" + print + "'";
		s += "<br />" + "clear: '" + clear + "'";
		s += "<hr /><b>STATE</b>";
		s += "<br />" + "Current Scan State: <font color=blue>" + scanState + "</font>";
		s += "<hr /><b>DEBUG</b>";
		
		s += "<table><tr><th>Configuration</th><th>Value</th><th></th></tr>";
		s += "<tr><td>" + "InitLevel: " + "</td><td>" + initLevel + "</td><td></td></tr>";
		s += "<tr><td>" + "unit: " + "</td><td>" + unit + " ms" + "</td><td>" + ((double) unit / 1000) + " s</td></tr>";
		s += "<tr><td>" + "dit: " + "</td><td>" + ditMin + "-" + ditMax + " ms" + "</td><td>" + ((double)ditMin / 1000) + "-" + ((double)ditMax / 1000) + " s" + "</td></tr>";
		s += "<tr><td>" + "dah: " + "</td><td>" + dahMin + "-" + dahMax + " ms" + "</td><td>" + ((double)dahMin / 1000) + "-" + ((double)dahMax / 1000) + " s" + "</td></tr>";
		s += "<tr><td>" + "letterPause: " + "</td><td>" + letterPause + " ms" + "</td><td>" + ((double)letterPause / 1000) + " s" + "</td></tr>";
		s += "<tr><td>" + "wordPause: " + "</td><td>" + wordPause + " ms" + "</td><td>" + ((double)wordPause / 1000) + " s" + "</td></tr>";
		s += "<tr><td>" + "Characters: " + "</td><td>" + content.toString() + "</td><td></td></tr>";
		s += "<tr><td>" + "Words: " + "</td><td>" + words.toString() + "</td><td></td></tr>";
		s += "<tr><td>" + "key press duration data: " + "</td><td>" + press.toString() + "</td><td>" + morse + "</td></tr>";
		s += "</table>";
		
		s += "<hr /><b>OUTPUT</b>";
		s += "<br />";
		for (String str : sentences) {
			s += str;
		}
		if (sentences.size() == 0) {
			s += "<br />";
		}
		s += "<hr /><b>PERFORMANCE</b>";
		s += "<br />" + "Words per miunte (WPM): " + Math.round(wpm);
		
		s += "</html>";
		return s;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		char key = e.getKeyChar();
		
		if (initLevel == 0) {
			trigger = key;
			label.setText(getMorseState("Enter scan start/stop key"));
			initLevel++;
		} else if (initLevel == 1 && key != trigger) {
			startStopScan = key;
			label.setText(getMorseState("Enter print ( .--. .-. .. -. - ) key"));
			initLevel++;
		} else if (initLevel == 2 && key != trigger && key != startStopScan) {
			print = key;
			label.setText(getMorseState("Enter clear ( -.-. .-.. . .- .-. ) key"));
			initLevel++;
		} else if (initLevel == 3 && key != trigger && key != startStopScan && key != print) {
			clear = key;
			label.setText(getMorseState("Initialization complete. --. ---"));
			initLevel++;
		}  else if (initLevel >= 4) {

			if (key == trigger) {
				
				if (!pressed) {
					startTime = System.currentTimeMillis();
					endPause = System.currentTimeMillis();
					
					this.getContentPane().setBackground(Color.ORANGE);
//					System.out.println("(endPause - startPause): " + (endPause - startPause));
					if (((endPause) - (startPause)) > letterPause) {
						getCharacter();
						if (c != 0) {
							content.add(c);
							c = 0;
						}
						press.clear();
					}
					if (((endPause) - (startPause)) > wordPause && initLevel == 5 && scanState == ScanState.SCANNING) {
						
						System.out.println("wordPause triggered... words: " + words + " content: " + content + " press: " + press);
						
						
						getCharacter();
						if (c != 0) {
							content.add(c);
							c = 0;
						}
						press.clear();
						
						
						String s = "";
						for (Character i : content) {
							s += i;
						}
						if (!s.equals("")) {
							words.add(s);
						}
						
						content.clear();
						
						
					}
					label.setText(getMorseState("DOWN"));
					
					if (scanState == ScanState.NOT_SCANNING) {
						startScan = System.currentTimeMillis();
					}
					scanState = ScanState.SCANNING;
					pressed = true;
					
					if (initLevel == 4) {
						initLevel++;
					}
				}
				
			} else if (key == startStopScan) {
				
				if (scanState == ScanState.NOT_SCANNING) { // start scanning
					scanState = ScanState.SCANNING;
					startScan = System.currentTimeMillis();
					label.setText(getMorseState());
				} else if (scanState == ScanState.SCANNING) { // finished scanning
					scanState = ScanState.NOT_SCANNING;
					endScan = System.currentTimeMillis();
					press.clear();
					content.clear();
					
					String sentence = "";
					
					for (int i = 0; i < words.size(); i++) {
						String str = words.get(i);
						if (i < words.size() - 1) {
							sentence += str + " ";
						} else {
							sentence += str + ". ";
						}
					}
					
					
					
					if (!sentence.equals("")) {
						sentences.add(sentence);
					}
					
					System.out.println("startScan: " + startScan + " endScan: " + endScan + " words.size(): " + words.size());
					
					double wpmCurrentSentence = (double) (words.size() / ( (double) (endScan - startScan) / 1000)) * 60;
					sentenceWpm.add(wpmCurrentSentence);
					
					double sum = 0;
					for (Double d : sentenceWpm) {
						sum += d;
					}
					wpm = sum / (double) sentenceWpm.size();
					
					
					words.clear();
					
					c = 0;
					label.setText(getMorseState());
				}
				
			} else if (key == print) {
				getCharacter();
				if (c != 0) {
					content.add(c);
					c = 0;
				}

				String s = "";
				
				for (Character i : content) {
					s += i;
				}
				if (!s.equals("")) {
					words.add(s);
				}
				content.clear();
				
				
				press.clear();
				label.setText(getMorseState(".--. .-. .. -. -"));
			} else if (key == clear) {
				
				sentences.clear();
				sentenceWpm.clear();
				label.setText(getMorseState("-.-. .-.. . .- .-."));
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (scanState == ScanState.SCANNING && pressed) {
			endTime = System.currentTimeMillis();
			startPause = System.currentTimeMillis();
//			System.out.println("(endTime - startTime): " + (endTime - startTime));
			press.add((endTime - startTime));
			label.setText(getMorseState("UP"));
			pressed = false;
			
			
			this.getContentPane().setBackground(Color.WHITE);
		}
	}
	
	public SIG m(int d) {
		if (press.get(d) > ditMin && press.get(d) < ditMax) {
			return SIG.DIT;
		}
		if (press.get(d) > dahMin && press.get(d) < dahMax) {
			return SIG.DAH;
		}
		return null;
	}

	public void getCharacter() {
		
		int length = press.size();
		
		switch (length) {
		case 1:
			if (m(0) == SIG.DIT) { // .
				c = 'E';
			} else if (m(0) == SIG.DAH) { // -
				c = 'T';
			}
			break;
		case 2:
			if (m(0) == SIG.DIT && m(1) == SIG.DAH) { // .-
				c = 'A';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DIT) { // ..
				c = 'I';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DAH) { // --
				c = 'M';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DIT) { // -.
				c = 'N';
			}
			break;
		case 3:
			if (m(0) == SIG.DAH && m(1) == SIG.DIT && m(2) == SIG.DIT) { // -..
				c = 'D';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DAH && m(2) == SIG.DIT) { // --.
				c = 'G';
			}  else if (m(0) == SIG.DAH && m(1) == SIG.DIT && m(2) == SIG.DAH) { // -.-
				c = 'K';
			}  else if (m(0) == SIG.DAH && m(1) == SIG.DAH && m(2) == SIG.DAH) { // ---
				c = 'O';
			}  else if (m(0) == SIG.DIT && m(1) == SIG.DAH && m(2) == SIG.DIT) { // .-.
				c = 'R';
			}  else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DIT) { // ...
				c = 'S';
			}  else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DAH) { // ..-
				c = 'U';
			}  else if (m(0) == SIG.DIT && m(1) == SIG.DAH && m(2) == SIG.DAH) { // .--
				c = 'W';
			} 
			break;
		case 4:
			if (m(0) == SIG.DAH && m(1) == SIG.DIT && m(2) == SIG.DIT && m(3) == SIG.DIT) { // -...
				c = 'B';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DIT && m(2) == SIG.DAH && m(3) == SIG.DIT) { // -.-.
				c = 'C';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DAH && m(3) == SIG.DIT) { // ..-.
				c = 'F';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DIT && m(3) == SIG.DIT) { // ....
				c = 'H';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DAH && m(2) == SIG.DAH && m(3) == SIG.DAH) { // .---
				c = 'J';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DAH && m(2) == SIG.DIT && m(3) == SIG.DIT) { // .-..
				c = 'L';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DAH && m(2) == SIG.DAH && m(3) == SIG.DIT) { // .--.
				c = 'P';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DAH && m(2) == SIG.DIT && m(3) == SIG.DAH) { // --.-
				c = 'Q';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DIT && m(3) == SIG.DAH) { // ...-
				c = 'V';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DIT && m(2) == SIG.DIT && m(3) == SIG.DAH) { // -..-
				c = 'X';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DIT && m(2) == SIG.DAH && m(3) == SIG.DAH) { // -.--
				c = 'Y';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DAH && m(2) == SIG.DIT && m(3) == SIG.DIT) { // --..
				c = 'Z';
			}
			break;
		case 5:
			if (m(0) == SIG.DIT && m(1) == SIG.DAH && m(2) == SIG.DAH && m(3) == SIG.DAH && m(4) == SIG.DAH) { // .----
				c = '1';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DAH && m(3) == SIG.DAH && m(4) == SIG.DAH) { // ..---
				c = '2';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DIT && m(3) == SIG.DAH && m(4) == SIG.DAH) { // ...--
				c = '3';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DIT && m(3) == SIG.DIT && m(4) == SIG.DAH) { // ....-
				c = '4';
			} else if (m(0) == SIG.DIT && m(1) == SIG.DIT && m(2) == SIG.DIT && m(3) == SIG.DIT && m(4) == SIG.DIT) { // .....
				c = '5';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DIT && m(2) == SIG.DIT && m(3) == SIG.DIT && m(4) == SIG.DIT) { // -....
				c = '6';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DAH && m(2) == SIG.DIT && m(3) == SIG.DIT && m(4) == SIG.DIT) { // --...
				c = '7';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DAH && m(2) == SIG.DAH && m(3) == SIG.DIT && m(4) == SIG.DIT) { // ---..
				c = '8';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DAH && m(2) == SIG.DAH && m(3) == SIG.DAH && m(4) == SIG.DIT) { // ----.
				c = '9';
			} else if (m(0) == SIG.DAH && m(1) == SIG.DAH && m(2) == SIG.DAH && m(3) == SIG.DAH && m(4) == SIG.DAH) { // -----
				c = '0';
			}
		default:
			break;
		}
		
	}
	
}
