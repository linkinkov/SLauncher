package ru.spark.slauncher.ui.console;

import ru.spark.slauncher.ui.loc.LocalizableButton;
import ru.spark.slauncher.ui.loc.LocalizableLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ExitCancelPanel extends JPanel {
	private static final long serialVersionUID = -1998881418330942647L;

	private LocalizableLabel label;
	private LocalizableButton button;
	private Font font = new Font("", Font.BOLD, 12);

	ExitCancelPanel(final ConsoleFrame cf) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.setBackground(Color.black);

		this.label = new LocalizableLabel(SwingConstants.CENTER);
		this.label.setForeground(Color.white);

		this.button = new LocalizableButton("console.close.cancel");

		label.setFont(font);
		label.setForeground(Color.white);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cf.cancelHiding();
			}
		});

		JPanel labelPan = new JPanel(), buttonPan = new JPanel();
		labelPan.setBackground(Color.black);
		buttonPan.setBackground(Color.black);
		labelPan.add(label);
		buttonPan.add(button);

		this.add("Center", labelPan);
		this.add("South", buttonPan);
	}

	void setTimeout(int timeout) {
		this.label.setText("console.close.text", timeout);
	}
}
