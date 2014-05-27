package run.window;

import java.awt.Dimension;

import javax.swing.JFrame;

import ui.icons.IconsLibrary;

/***
 * OpenView Window
 * 
 * @author martino
 * 
 */
public class Window extends JFrame {
	/**
	 * uid
	 */
	private static final long serialVersionUID = 7446192599263749847L;
	/***
	 * Main Panel
	 */
	private MainPanel panel_;

	/***
	 * Initialize geometric and functional informations
	 */
	public Window() {

		this.setTitle("Open View");
		this.setMinimumSize(new Dimension(600, 400));
		this.setIconImage(IconsLibrary.getIcon(IconsLibrary.AppIcon).getImage());
		panel_ = new MainPanel();
		this.setContentPane(panel_);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);

		this.setSize(800, 600);

	}
}
