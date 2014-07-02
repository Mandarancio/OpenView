package gui.settings.viewers;

import gui.support.Setting;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import core.support.Rule;

public class ViewerManager {
	private static HashMap<Rule, Class<? extends Viewer>> viewers_ = new HashMap<>();
	private static Class<? extends Viewer> spareViewer_ = ConstViewer.class;

	public static void initialize() {
		viewers_.put(StringViewer.rule(), StringViewer.class);
		viewers_.put(BooleanViewer.rule(), BooleanViewer.class);
		viewers_.put(NumericViewer.rule(), NumericViewer.class);
		viewers_.put(ColorViewer.rule(), ColorViewer.class);
		viewers_.put(FileViewer.rule(), FileViewer.class);
		viewers_.put(ComboViewer.rule(), ComboViewer.class);
		viewers_.put(ConstViewer.rule(), ConstViewer.class);
	}

	public static void addViewer(Rule rule, Class<? extends Viewer> v) {
		viewers_.put(rule, v);
	}

	public static Class<? extends Viewer> getViewer(Setting s) {
		Class<? extends Viewer> selected = null;
		for (Rule rule : viewers_.keySet()) {
			if (rule.check(s)) {
				selected = viewers_.get(rule);
			}
		}
		if (selected != null) {
			return selected;
		}
		return spareViewer_;
	}

	public static Viewer initViewer(Setting s) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException {
		Class<? extends Viewer> c = getViewer(s);
		Constructor<?> cs[] = c.getConstructors();
		if (cs.length != 0)
			return (Viewer) c.getConstructors()[0].newInstance(s);
		else
			return null;
	}
}
