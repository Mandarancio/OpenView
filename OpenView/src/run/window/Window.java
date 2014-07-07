package run.window;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import run.init.SettingsUtils;
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
		this.setMinimumSize(new Dimension(600, 500));
		this.setIconImage(IconsLibrary.getIcon(IconsLibrary.AppIcon).getImage());
		panel_ = new MainPanel();
		this.setContentPane(panel_);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);

		this.setSize(900, 700);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SettingsUtils.save();
			}
		});
	}
}
