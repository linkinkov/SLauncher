package ru.spark.slauncher.ui.swing.editor;

import ru.spark.slauncher.ui.loc.LocalizableLabel;
import ru.spark.util.OS;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class EditorPane extends JEditorPane {
	private static final long serialVersionUID = -2857352867725574106L;

	public EditorPane(Font font) {

		if(font != null)
			setFont(font);
		else
			font = getFont();

		StyleSheet css = new StyleSheet();

		css.importStyleSheet(getClass().getResource("styles.css"));
		css.addRule(
				new StringBuilder()
				.append("body { font-family: ").append(font.getFamily())
				.append("; font-size: ").append(font.getSize()).append("pt; } ")
				.append("a { text-decoration: underline; }")
				.toString()
				);

		ExtendedHTMLEditorKit html = new ExtendedHTMLEditorKit();
		html.setStyleSheet(css);

		getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		setMargin(new Insets(0, 0, 0, 0));
		setEditorKit(html);
		setEditable(false);
		setOpaque(false);

		addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
					return;

				URL url = e.getURL();

				if (url == null)
					return;

				OS.openLink(url);
			}
		});
	}

	public EditorPane() {
		this(new LocalizableLabel().getFont());
	}

	public EditorPane(URL initialPage) throws IOException {
		this();
		setPage(initialPage);
	}

	public EditorPane(String url) throws IOException {
		this();
		setPage(url);
	}

	public EditorPane(String type, String text) {
		this();
		setContentType(type);
		setText(text);
	}

}
