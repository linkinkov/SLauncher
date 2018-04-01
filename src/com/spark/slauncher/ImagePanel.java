package com.spark.slauncher;

import ru.spark.util.OS;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImagePanel
        extends JComponent {
    public static int ofsetX = 0;
    public static int ofsetY = 0;
    private Image logo;
    private Image progress_bg;
    private Image progress_bar;
    private Image shadow;
    private Float progress = Float.valueOf(0.0F);
    private String label = "Загрузка...";

    public ImagePanel(Image logo, Image progress_bg, Image progress_bar, Image shadow) {
        this.logo = logo;
        this.progress_bg = progress_bg;
        this.progress_bar = progress_bar;
        this.shadow = shadow;
    }

    public static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public void setProgress(Float progress, String label) {
        this.progress = progress;
        this.label = label;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ImagePanel.this.repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        applyQualityRenderingHints(g2d);

        String OS = System.getProperty("os.name").toUpperCase();
        if (ru.spark.util.OS.CURRENT == ru.spark.util.OS.WINDOWS || ru.spark.util.OS.is(ru.spark.util.OS.OSX)) {
            ofsetX = 40;
            ofsetY = 40;
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));

            g2d.drawImage(this.shadow, 0, 0, this);

            g2d.setComposite(AlphaComposite.SrcOver.derive(1.0F));
        }
        g2d.setColor(new Color(92, 184, 92));

        g2d.fillRect(ofsetX, 0 + ofsetY, 400, 60);

        g2d.fillRoundRect(0 + ofsetX, -5 + ofsetY, 400, 60, 5, 5);

        g2d.setColor(new Color(202, 225, 202));

        g2d.fillRect(0 + ofsetX, 60 + ofsetY, 400, 20);

        g2d.fillRoundRect(0 + ofsetX, 60 + ofsetY, 400, 37, 5, 5);

        g2d.fillRect(0 + ofsetX, 60 + ofsetY, 400, 20);

        g2d.setColor(new Color(77, 165, 77));

        g2d.fillRect(0 + ofsetX, 60 + ofsetY, 400, 1);

        g2d.setColor(new Color(255, 255, 255, 50));

        g2d.fillRect(5 + ofsetX, -5 + ofsetY, 390, 1);

        g2d.drawImage(this.logo, 95 + ofsetX, 6 + ofsetY, this);

        g2d.drawImage(this.progress_bg, 7 + ofsetX, 66 + ofsetY, this);

        g2d.drawImage(this.progress_bar, 7 + ofsetX, 66 + ofsetY, Math.round(this.progress_bar.getWidth(this) * this.progress.floatValue()), 23, this);

        drawCenteredString(g2d, this.label, new Rectangle(0, 20 + ofsetX, 400 + ofsetY * 2, 60), new Font("Arial", 0, 14));
    }

    public void drawCenteredString(Graphics2D g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);

        int x = 80;

        int y = (rect.height - metrics.getHeight()) / 2 - metrics.getAscent();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setFont(font);

        TextLayout textLayout = new TextLayout(text, font, g.getFontRenderContext());

        g.setPaint(new Color(0, 127, 0));
        if (OS.CURRENT != OS.OSX) {
            g.setComposite(new XorComposite());
        }
        textLayout.draw(g, 60.0F, 82 + ofsetY);
    }

    private static class XorComposite
            implements Composite {
        public static XorComposite INSTANCE = new XorComposite();
        private ImagePanel.XorContext context = new ImagePanel.XorContext();

        public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
            return this.context;
        }
    }

    private static class XorContext
            implements CompositeContext {
        public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
            int w = Math.min(src.getWidth(), dstIn.getWidth());
            int h = Math.min(src.getHeight(), dstIn.getHeight());

            int[] srcRgba = new int[4];
            int[] dstRgba = new int[4];
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    src.getPixel(x, y, srcRgba);
                    dstIn.getPixel(x, y, dstRgba);
                    if ((dstRgba[0] == 245) && (dstRgba[1] == 245) && (dstRgba[2] == 245)) {
                        dstRgba[0] = 0;
                        dstRgba[1] = 127;
                        dstRgba[2] = 0;
                        dstOut.setPixel(x, y, dstRgba);
                    } else {
                        dstRgba[0] = 255;
                        dstRgba[1] = 255;
                        dstRgba[2] = 255;
                        dstOut.setPixel(x, y, dstRgba);
                    }
                    dstOut.setPixel(x, y, dstRgba);
                }
            }
        }

        public void dispose() {
        }
    }
}
