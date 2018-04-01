package ru.spark.slauncher.ui.scenes;

import ru.spark.slauncher.configuration.Configuration;
import ru.spark.slauncher.ui.InfoPanel;
import ru.spark.slauncher.ui.MainPane;
import ru.spark.slauncher.ui.SideNotifier;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.settings.SettingsPanel;
import ru.spark.slauncher.ui.swing.extended.ExtendedPanel;
import ru.spark.util.Direction;
import ru.spark.util.U;

import java.awt.Dimension;


public class DefaultScene
        extends PseudoScene
{
    public static final Dimension UI_SCENA = new Dimension(1024, 768);
    public static final Dimension LOGIN_SIZE = new Dimension(240, 240);
    public static final Dimension SETTINGS_SIZE = new Dimension(500, 475);
    public static final int EDGE_INSETS = 10;
    public static final int INSETS = 15;
    public final LoginForm loginForm;
    public SettingsPanel settingsForm;
    public InfoPanel infoPanel;
    private SidePanel sidePanel;
    private ExtendedPanel sidePanelComp;
    private Direction lfDirection;
    public final UIScene uiScene;
    public static DefaultScene ref;

    public DefaultScene(MainPane main)
    {
        super(main);
        ref = this;

        this.settingsForm = new SettingsPanel(this);
        settingsForm.setSize(SETTINGS_SIZE);
        settingsForm.setVisible(false);
        this.loginForm = new LoginForm(this);

        this.uiScene = new UIScene(this);
        this.uiScene.setSize(UI_SCENA);
        add(this.uiScene);

        updateDirection();
    }

    public void onResize()
    {
        if (this.parent == null) {
            return;
        }
        setBounds(0, 0, this.parent.getWidth(), this.parent.getHeight());

        updateCoords();

        UI_SCENA.setSize(this.parent.getWidth(), this.parent.getHeight());
        this.uiScene.setSize(UI_SCENA);

        this.uiScene.validate();
    }

    private void updateCoords()
    {
        int w = getWidth();int h = getHeight();int hw = w / 2;int hh = h / 2;
        if (this.sidePanel == null)
        {
            int lf_x;
            switch (this.lfDirection)
            {
                case TOP_LEFT:
                case CENTER_LEFT:
                case BOTTOM_LEFT:
                    lf_x = 10;
                    break;
                case TOP:
                case CENTER:
                case BOTTOM:
                    break;
                case TOP_RIGHT:
                case CENTER_RIGHT:
                case BOTTOM_RIGHT:
                    break;
                default:
                    throw new RuntimeException("unknown direction:" + this.lfDirection);
            }
            int lf_y;
            switch (this.lfDirection)
            {
                case TOP_LEFT:
                case TOP:
                case TOP_RIGHT:
                    lf_y = 10;
                    break;
                case CENTER_LEFT:
                case CENTER:
                case CENTER_RIGHT:
                    break;
                case BOTTOM_LEFT:
                case BOTTOM:
                case BOTTOM_RIGHT:
                    break;
                default:
                    throw new RuntimeException("unknown direction:" + this.lfDirection);
            }
        }
        int n_y = 10;
        switch (this.lfDirection)
        {
            case TOP_LEFT:
            case CENTER_LEFT:
            case BOTTOM_LEFT:
                break;
            default:
                int i = 10;
        }
    }

    public SidePanel getSidePanel()
    {
        return this.sidePanel;
    }

    public void setSidePanel(SidePanel side)
    {
        if (this.sidePanel == side) {
            return;
        }
        boolean noSidePanel = side == null;
        if (this.sidePanelComp != null) {
            this.sidePanelComp.setVisible(false);
        }
        this.sidePanel = side;
        this.sidePanelComp = (noSidePanel ? null : getSidePanelComp(side));
        if (!noSidePanel) {
            this.sidePanelComp.setVisible(true);
        }
        updateCoords();
    }

    public void toggleSidePanel(SidePanel side)
    {
        if (this.sidePanel == side) {
            side = null;
        }
        setSidePanel(side);
    }

    public ExtendedPanel getSidePanelComp(SidePanel side)
    {
        if (side == null) {
            throw new NullPointerException("side");
        }
        switch (side)
        {
            case SETTINGS:
                return null;
        }
        throw new RuntimeException("unknown side:" + side);
    }

    public Direction getLoginFormDirection()
    {
        return this.lfDirection;
    }

    public void updateDirection()
    {
        loadDirection();
        updateCoords();
    }

    private void loadDirection()
    {
        Configuration config = getMainPane().getRootFrame().getConfiguration();

        Direction loginFormDirection = config.getDirection("gui.direction.loginform");
        if (loginFormDirection == null) {
            loginFormDirection = Direction.CENTER;
        }
        this.lfDirection = loginFormDirection;
    }

    public enum SidePanel
    {
        SETTINGS;

        public final boolean requiresShow;

        SidePanel(boolean requiresShow)
        {
            this.requiresShow = requiresShow;
        }

        SidePanel()
        {
            this(false);
        }
    }
}
