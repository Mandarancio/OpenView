package gui.components.ovgui;

import gui.components.OVComponent;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import org.w3c.dom.Element;

import core.Setting;
import core.Value;
import core.support.FontStyle;
import core.support.OrientationEnum;

public class OVLabel extends OVComponent {

	/**
     *
     */
	private static final long serialVersionUID = 6981012611985250683L;
	private String text_ = "Label";
	private OrientationEnum textAlignment_ = OrientationEnum.CENTER;
	private static String Text = "Text", FSize = "Size", FStyle = "Style",
			Alignment = "Alignment", Foreground = "Color";

	public OVLabel(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.SizeW).setValue(90);
		getSetting(ComponentSettings.SizeH).setValue(30);
	}

	public OVLabel(Element el, OVContainer father) {
		super(el, father);
	}

	@Override
	protected void initBasicSettings() {
		super.initBasicSettings();
		Setting s = new Setting(Text, new Value("Label"));
		addSetting(ComponentSettings.SpecificCategory, s);

		s = new Setting(FSize, 14, 6, 80);
		addSetting(ComponentSettings.SpecificCategory, s);

		s = new Setting(FStyle, FontStyle.PLAIN);
		addSetting(ComponentSettings.SpecificCategory, s);

		s = new Setting(Alignment, OrientationEnum.CENTER);
		addSetting(ComponentSettings.SpecificCategory, s);

		s = new Setting(Foreground, Color.lightGray);
		addSetting(ComponentSettings.SpecificCategory, s);

		getSetting(ComponentSettings.SizeW).setMin(new Value(45));
	}

	@Override
	protected void paintOVComponent(Graphics2D g) {
		g.setFont(getFont());
		g.setColor(getForeground());
		paintText(text_, g, textAlignment_);
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		String setting = s.getName();
		if (setting.equals(Text)) {
			text_ = v.getString();
			repaint();
		} else if (setting.equals(FSize)) {
			try {
				if (getFont() != null) {
					Font f = new Font(getFont().getFontName(), getFont()
							.getStyle(), v.getInt());
					setFont(f);
					repaint();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (setting.equals(FStyle)) {
			if (getFont() != null) {
				FontStyle style = FontStyle.PLAIN;
				try {
					style = (FontStyle) v.getEnum();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String name = getFont().getFontName();
				int size = getFont().getSize();
				Font f;
				if (style == FontStyle.PLAIN) {
					f = new Font(name, Font.PLAIN, size);
				} else if (style == FontStyle.BOLD) {
					f = new Font(name, Font.BOLD, size);
				} else {
					f = new Font(name, Font.ITALIC, size);
				}
				setFont(f);
				repaint();
			}
		} else if (setting.equals(Alignment)) {
			try {
				textAlignment_ = (OrientationEnum) v.getEnum();
			} catch (Exception e) {
				e.printStackTrace();
			}
			repaint();
		} else if (setting.equals(Foreground)) {
			try {
				setForeground(v.getColor());
				repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			super.valueUpdated(s, v);
		}
	}
}
