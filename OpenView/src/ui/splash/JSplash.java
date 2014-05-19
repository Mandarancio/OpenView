package ui.splash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public final class JSplash extends JWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = 239211542403955456L;
	private JLabel lblVersion = new JLabel("Version: beta");
	private JLabel lblStatus = new JLabel();

	public JSplash() {
		init();
		center();
	}

	private void init() {
		JPanel pnlImage = new JPanel();
		ImageIcon image = new ImageIcon(getClass().getResource("splash.png"));
		JLabel lblBack = new JLabel(image);
		Border raisedbevel = BorderFactory.createEmptyBorder();
		Border loweredbevel = BorderFactory.createEmptyBorder();

		lblBack.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
		getLayeredPane().add(lblBack, new Integer(Integer.MIN_VALUE));

		pnlImage.setLayout(null);
		pnlImage.setOpaque(false);
		pnlImage.setBorder(BorderFactory.createCompoundBorder(raisedbevel,
				loweredbevel));

		pnlImage.add(this.lblVersion);
		pnlImage.add(this.lblStatus);

		this.lblStatus.setForeground(Color.darkGray);
		this.lblStatus.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.lblStatus.setBounds(5, 250, 490, 20);
		this.lblStatus.setHorizontalAlignment(SwingConstants.CENTER);

		this.lblVersion.setForeground(Color.darkGray);
		this.lblVersion.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.lblVersion.setBounds(5, 275, 120, 20);

		setContentPane(pnlImage);
		setSize(image.getIconWidth(), image.getIconHeight());
	}

	private void center() {
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		int nX = (int) (scr.getWidth() - getWidth()) / 2;
		int nY = (int) (scr.getHeight() - getHeight()) / 2;

		setLocation(nX, nY);
	}

	public void setStatus(String status) {
		lblStatus.setText(status);
	}
}
