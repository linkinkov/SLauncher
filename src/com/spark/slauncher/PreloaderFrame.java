package com.spark.slauncher;


import resources.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PreloaderFrame
        extends JFrame {
    private static PreloaderFrame REF;
    private ImagePanel progressPanel = null;

    public PreloaderFrame() {
        REF = this;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int preloaderWidth = 400;
        int proloaderHeight = 100;
        if (ru.spark.util.OS.CURRENT == ru.spark.util.OS.WINDOWS || ru.spark.util.OS.is(ru.spark.util.OS.OSX)) {
            preloaderWidth = 480;
            proloaderHeight = 170;
        }
        setDefaultCloseOperation(3);

        setBounds((width - preloaderWidth) / 2, (height - proloaderHeight) / 2, preloaderWidth, proloaderHeight);
        try {
            BufferedImage logo = ImageIO.read(Resources.class.getResource("images/logo.png"));
            BufferedImage progress_bg = ImageIO.read(Resources.class.getResource("progress_bg.png"));
            BufferedImage progress_bar = ImageIO.read(Resources.class.getResource("progress_bar.png"));
            BufferedImage shadow = ImageIO.read(Resources.class.getResource("shadow.png"));

            this.progressPanel = new ImagePanel(logo, progress_bg, progress_bar, shadow);

            setContentPane(this.progressPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PreloaderFrame getInstance() {
        return REF;
    }

    public void dispose() {
        super.dispose();

        REF = null;
        this.progressPanel = null;
    }

    public void setProgress(Float progress, String label) {
        if (this.progressPanel != null) {
            this.progressPanel.setProgress(progress, label);
        }
    }
}