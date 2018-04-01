package ru.spark.slauncher.ui.console;

import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.explorer.ExtensionFileFilter;
import ru.spark.slauncher.ui.explorer.FileExplorer;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.slauncher.ui.swing.EmptyAction;
import ru.spark.slauncher.ui.swing.TextPopup;
import ru.spark.util.FileUtil;
import ru.spark.util.OS;
import ru.spark.util.U;
import ru.spark.util.stream.StringStream;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.*;

public class ConsoleTextPopup extends TextPopup {
	private final Console console;
	
	private final FileExplorer explorer;
	private final Action saveAllAction;
	
	ConsoleTextPopup(Console console) {
		this.console = console;
		
		this.explorer = new FileExplorer();
		explorer.setFileFilter(new ExtensionFileFilter("log"));
		
		this.saveAllAction = new EmptyAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onSavingCalled(e);
			}
		};
	}
	
	@Override
	protected JPopupMenu getPopup(MouseEvent e, final JTextComponent comp) {
		JPopupMenu menu = super.getPopup(e, comp);
		
		if(menu == null)
			return null;
		
		menu.addSeparator();
		menu.add(saveAllAction).setText(Localizable.get("console.save.popup"));
		
		return menu;
	}
	
	protected void onSavingCalled(ActionEvent e) {
		explorer.setSelectedFile(new File(console.getName() + ".log"));
		
		int result = explorer.showSaveDialog(console.frame);
		
		if(result != FileExplorer.APPROVE_OPTION)
			return;
		
		File file = explorer.getSelectedFile();
		
		if(file == null) {
			U.log("Returned NULL. Damn it!");
			return;
		}
		
		String path = file.getAbsolutePath();
		
		if(!path.endsWith(".log"))
			path += ".log";
		
		file = new File(path);
		
		OutputStream output = null;		
		try {
			FileUtil.createFile(file);
			
			StringStream input = console.getStream();
			output = new BufferedOutputStream(new FileOutputStream(file));
			
			boolean addR = OS.WINDOWS.isCurrent();
			int caret = -1;
			char current;
			
			while(++caret < input.getLength()) {
				current = input.getCharAt(caret);
				
				if(current == '\n' && addR)
					output.write('\r');
				
				output.write(current);
			}
			
			output.close();
		} catch(Throwable throwable) {
			Alert.showLocError("console.save.error", throwable);
		} finally {
			if(output != null)
				try { output.close(); }
				catch(IOException ignored) {
					ignored.printStackTrace();
				}
		}
	}
}
 