import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MorseKeyListener extends JFrame implements KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLabel label;
	
	private int initLevel;
	private char trigger = 0;
	private char startStopScan = 0;
	private char print = 0;
	private List<Long> press;
	Character c = 0;
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
	private int modValue = 100000;
	
	public enum ScanState {
		NOT_SCANNING,
		SCANNING
	}
	
	public enum MorseChar {
		DOT,
		DASH
	}
	int unit = 250;
	
	int dotMin = 0;
	int dotMax = dotMin + unit;
	
	int dashMin = dotMax + 1;
	int dashMax = dashMin + 3 * unit;
	int letterPause = 3 * unit;
	int wordPause = 7 * unit;
	
	public MorseKeyListener(JLabel label) {
		this.label = label;
		press = new ArrayList<Long>();
		content = new ArrayList<Character>();
		words = new ArrayList<String>();
		sentences = new ArrayList<String>();
		sentenceWpm = new ArrayList<Double>();
		scanState = ScanState.NOT_SCANNING;
		initLevel = 0;
		this.label.setText(getMorseState("Enter trigger key:"));
		addKeyListener(this);
		
		startTime = System.currentTimeMillis() % modValue;
		endTime = System.currentTimeMillis() % modValue;
		startPause = System.currentTimeMillis() % modValue;
		endPause = System.currentTimeMillis() % modValue;
		startScan = System.currentTimeMillis() % modValue;
		endScan = System.currentTimeMillis() % modValue;
	}
	
	public String getMorseState() {
		return getMorseState("");
	}
	
	public String getMorseState(String message) {
		String s = "<html>";
		s += "<br>-- --- .-. ... . ....... -.-. --- -.. . ....... .. -. .--. ..- - ....... .- .--. .--.";
		s += " (Morse Code Input App)";
		s += "<br><br><u>Instructions</u>: Use the 'trigger' key to input 'dit' or 'dah' symbols defined by key press time duration (short keypress or long keypress). ";
		s += "To write a sentence:";
		s += "<br> 1. Hit 'start/stop'.";
		s += "<br> 2. Then use the 'trigger' key to input the words in the sentence.";
		s += "<br> 3. Then hit 'print' to print the words.";
		s += "<br> 4. Then hit 'start/stop' again.";
		s += "<br><br>";
		
		s += "<b>" + message + "</b>";
		s += "<br><br>--------------------- <u>INPUT KEY CONTROLS</u>";
		s += "<br>" + "trigger: '" + trigger + "'";
		s += "<br>" + "start/stop: '" + startStopScan + "'";
		s += "<br>" + "print: '" + print + "'";
		s += "<br><br>--------------------- <u>STATE</u>";
		s += "<br>" + "Current Scan State: " + scanState;
		s += "<br><br>--------------------- <u>DEBUG</u>";
		s += "<br>" + "unit: " + unit + " ms";
		s += "<br>" + "dit: " + dotMin + "-" + dotMax + " ms";
		s += "<br>" + "dah: " + dashMin + "-" + dashMax + " ms";
		s += "<br>" + "letterPause: " + letterPause + " ms";
		s += "<br>" + "wordPause: " + wordPause + " ms";
		s += "<br>" + "Current Character: '" + c + "'";
		s += "<br>" + "Character Content: " + content.toString();
		s += "<br>" + "Words: " + words.toString();
		s += "<br>" + "key press duration data: " + press.toString();
		s += "<br><br>--------------------- <u>OUTPUT</u>";
		s += "<br>";
		for (String str : sentences) {
			s += str;
		}
		s += "<br><br>--------------------- <u>PERFORMANCE</u>";
		s += "<br>" + "Words per miunte (WPM): " + Math.round(wpm);
		
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
			label.setText(getMorseState("Enter scan start/stop key:"));
			initLevel++;
		} else if (initLevel == 1 && key != trigger) {
			startStopScan = key;
			label.setText(getMorseState("Enter print key:"));
			initLevel++;
		}  else if (initLevel == 2 && key != trigger && key != startStopScan) {
			print = key;
			label.setText(getMorseState("Initialization complete."));
			initLevel++;
		} else if (initLevel >= 3) {

			if (key == trigger) {
				
				if (!pressed) {
					startTime = System.currentTimeMillis() % modValue;
					endPause = System.currentTimeMillis() % modValue;
					
//					System.out.println("(endPause - startPause): " + (endPause - startPause));
					if (((endPause) - (startPause)) > letterPause) {
						getCharacter();
						if (c != 0) {
							content.add(c);
							c = 0;
						}
						press.clear();
					}
					if (((endPause) - (startPause)) > wordPause && initLevel == 4 && scanState == ScanState.SCANNING) {
						
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
						
						
					}
					label.setText(getMorseState("TD"));
					
					if (scanState == ScanState.NOT_SCANNING) {
						startScan = System.currentTimeMillis() % modValue;
					}
					scanState = ScanState.SCANNING;
					pressed = true;
					
					if (initLevel == 3) {
						initLevel++;
					}
				}
				
			} else if (key == startStopScan) {
				
				if (scanState == ScanState.NOT_SCANNING) { // start scanning
					scanState = ScanState.SCANNING;
					startScan = System.currentTimeMillis() % modValue;
					label.setText(getMorseState());
				} else if (scanState == ScanState.SCANNING) { // finished scanning
					scanState = ScanState.NOT_SCANNING;
					endScan = System.currentTimeMillis() % modValue;
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
				label.setText(getMorseState("print"));
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (scanState == ScanState.SCANNING && pressed) {
			endTime = System.currentTimeMillis() % modValue;
			startPause = System.currentTimeMillis() % modValue;
//			System.out.println("(endTime - startTime): " + (endTime - startTime));
			press.add((endTime - startTime));
			label.setText(getMorseState("TU"));
			pressed = false;
			
			
		}
	}
	
	public MorseChar getM(int d) {
		if (press.get(d) > dotMin && press.get(d) < dotMax) {
			return MorseChar.DOT;
		}
		if (press.get(d) > dashMin && press.get(d) < dashMax) {
			return MorseChar.DASH;
		}
		return null;
	}

	public void getCharacter() {
		
		int length = press.size();
		
		switch (length) {
		case 1:
			if (getM(0) == MorseChar.DOT) { // .
				c = 'E';
			} else if (getM(0) == MorseChar.DASH) { // -
				c = 'T';
			}
			break;
		case 2:
			if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DASH) { // .-
				c = 'A';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT) { // ..
				c = 'I';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH) { // --
				c = 'M';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DOT) { // -.
				c = 'N';
			}
			break;
		case 3:
			if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT) { // -..
				c = 'D';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DOT) { // --.
				c = 'G';
			}  else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DASH) { // -.-
				c = 'K';
			}  else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DASH) { // ---
				c = 'O';
			}  else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DOT) { // .-.
				c = 'R';
			}  else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT) { // ...
				c = 'S';
			}  else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DASH) { // ..-
				c = 'U';
			}  else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DASH) { // .--
				c = 'W';
			} 
			break;
		case 4:
			if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DOT) { // -...
				c = 'B';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DOT) { // -.-.
				c = 'C';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DOT) { // ..-.
				c = 'F';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DOT) { // ....
				c = 'H';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DASH) { // .---
				c = 'J';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DOT) { // .-..
				c = 'L';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DOT) { // .--.
				c = 'P';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DASH) { // --.-
				c = 'Q';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DASH) { // ...-
				c = 'V';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DASH) { // -..-
				c = 'X';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DASH) { // -.--
				c = 'Y';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DOT) { // --..
				c = 'Z';
			}
			break;
		case 5:
			if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DASH && getM(4) == MorseChar.DASH) { // .----
				c = '1';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DASH && getM(4) == MorseChar.DASH) { // ..---
				c = '2';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DASH && getM(4) == MorseChar.DASH) { // ...--
				c = '3';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DOT && getM(4) == MorseChar.DASH) { // ....-
				c = '4';
			} else if (getM(0) == MorseChar.DOT && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DOT && getM(4) == MorseChar.DOT) { // .....
				c = '5';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DOT && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DOT && getM(4) == MorseChar.DOT) { // -....
				c = '6';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DOT && getM(3) == MorseChar.DOT && getM(4) == MorseChar.DOT) { // --...
				c = '7';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DOT && getM(4) == MorseChar.DOT) { // ---..
				c = '8';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DASH && getM(4) == MorseChar.DOT) { // ----.
				c = '9';
			} else if (getM(0) == MorseChar.DASH && getM(1) == MorseChar.DASH && getM(2) == MorseChar.DASH && getM(3) == MorseChar.DASH && getM(4) == MorseChar.DASH) { // -----
				c = '0';
			}
		default:
			break;
		}
		
	}
	
}
