package roguelike.applications;

import javax.swing.JFrame;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import roguelike.mob.Colors;
import roguelike.screens.*;

public class ApplicationMain extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1060623638149583738L;
	
	private AsciiPanel terminal;
	private Screen screen;
	
	public ApplicationMain(){
		super();
	}
	
	@Override
	public void repaint(){
		terminal.clear();
		screen.displayOutput(terminal);
		super.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		screen = screen.respondToUserInput(e);
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }

	public static void main(String[] args){
		ApplicationMain app = new ApplicationMain();
		Colors.initializeColors();
		app.terminal = new AsciiPanel(88, 32, AsciiFont.CP437_9x16);
		app.add(app.terminal);
		app.pack();
		app.screen = new StartScreen();
		app.addKeyListener(app);
		app.repaint();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);

	}
}
