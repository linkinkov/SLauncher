package ru.spark.slauncher.ui.scenes;


import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import ru.spark.slauncher.ui.SLauncherFrame;
import ru.spark.slauncher.ui.center.CenterPanel;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UIScene extends CenterPanel
{
    public DefaultScene parent;
    public WebEngine webEngine;
    public WebView browser;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private static Socket connection;
    private SwingFXWebViewNew view;

    public void start(Stage stage) {}

    public UIScene(DefaultScene p)
    {
        super(CenterPanel.defaultTheme, new Insets(0, 0, 0, 0));
        this.parent = p;

        this.view = new SwingFXWebViewNew();
        this.view.setListener(SLauncherFrame.ref);
        add(this.view);
    }
}
