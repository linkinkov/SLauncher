package ru.spark.slauncher.ui.settings;

import ru.spark.slauncher.ui.images.ImageCache;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.slauncher.ui.loc.LocalizableComponent;
import ru.spark.slauncher.ui.swing.editor.EditorPane;
import ru.spark.slauncher.ui.swing.extended.BorderPanel;
import ru.spark.util.FileUtil;
import ru.spark.util.U;
import ru.spark.util.git.ITokenResolver;
import ru.spark.util.git.TokenReplacingReader;

import java.io.IOException;
import java.io.StringReader;

public class AboutPage extends BorderPanel implements LocalizableComponent {
	private final AboutPageTokenResolver resolver;

	private final String source;
	private final EditorPane editor;

	AboutPage() {
		String tempSource;

		try {
			tempSource = FileUtil.getResource(getClass().getResource("about.html"));
		} catch(Exception e) {
			U.log(e);
			tempSource = null;
		}

		this.source = tempSource;
		this.resolver = new AboutPageTokenResolver();
		this.editor = new EditorPane();

		updateLocale();
		setCenter(editor);
	}

	public EditorPane getEditor() {
		return editor;
	}

	public String getSource() {
		return source;
	}

	@Override
	public void updateLocale() {
		if(source == null) return;

		StringBuilder string = new StringBuilder();
		TokenReplacingReader replacer = new TokenReplacingReader(new StringReader(source), resolver);
		int read;

		try {

			while((read = replacer.read()) > 0)
				string.append((char) read);

		} catch(IOException ioE) {
			ioE.printStackTrace();
			return;
		} finally {
			U.close(replacer);
		}

		editor.setText(string.toString());
	}

	private class AboutPageTokenResolver implements ITokenResolver {
		private static final String
		image = "image:", loc = "loc:", width = "width",
		color = "color";

		@Override
		public String resolveToken(String token) {
			if(token.startsWith(image))
				return ImageCache.getRes(token.substring(image.length())).toExternalForm();

			if(token.startsWith(loc))
				return Localizable.get(token.substring(loc.length()));

			if(token.equals(width))
				return "445";

			if(token.equals(color))
				return "black";

			return token;
		}
	}

}
