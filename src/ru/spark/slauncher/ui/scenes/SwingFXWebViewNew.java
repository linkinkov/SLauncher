package ru.spark.slauncher.ui.scenes;

import com.sun.javafx.application.PlatformImpl;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JPanel;
import netscape.javascript.JSObject;
import resources.Resources;
import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.configuration.Configuration;
import ru.spark.slauncher.managers.ProfileManager;
import ru.spark.slauncher.minecraft.auth.Account;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.editor.EditorFieldHandler;
import ru.spark.slauncher.ui.editor.EditorFileField;
import ru.spark.slauncher.ui.explorer.FileExplorer;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.util.MinecraftUtil;
import ru.spark.util.OS;
import ru.spark.util.U;
import ru.spark.util.async.AsyncThread;

public class SwingFXWebViewNew extends JPanel implements ComponentListener
{
    public static SwingFXWebViewNew ref;
    private Stage stage;
    private WebView browser;
    private JFXPanel jfxPanel;
    private JButton swingButton;
    private static WebEngine webEngine;
    private Label labelFromJavascript;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private static Socket connection;
    private UIScene scena;
    private ProfileManager manager;
    private Account tempAccount;
    private SwingFxWebViewNewListener listener;


    public SwingFXWebViewNew()
    {
        ref = this;
        initComponents();
    }

    public WebEngine getWebEngine() {
        return webEngine;
    }
    public void setListener(SwingFxWebViewNewListener listener)
    {
        this.listener = listener;
    }

    private void initComponents()
    {
        this.manager = SLauncher.getInstance().getProfileManager();
        addComponentListener(this);

        this.jfxPanel = new JFXPanel();
        createScene();

        setLayout(new BorderLayout());
        add(this.jfxPanel, "Center");
    }

    public void paintComponent(Graphics g) {}

    public void componentResized(ComponentEvent e)
    {
        if (this.browser != null) {
            this.browser.setPrefSize(getWidth(), getHeight());
        }
    }

    public void updateSize()
    {
        if (this.browser != null)
        {
            Dimension size = getSize();
            this.browser.setPrefSize(size.getWidth(), size.getHeight());
        }
    }

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}

    private void createScene()
    {
        PlatformImpl.startup(new Runnable()
        {
            public void run()
            {
                SwingFXWebViewNew.this.stage = new Stage();

                SwingFXWebViewNew.this.stage.setTitle("Hello Java FX");
                SwingFXWebViewNew.this.stage.setResizable(true);

                Group root = new Group();
                Scene scene = new Scene(root, getWidth(), getHeight());
                SwingFXWebViewNew.this.stage.setScene(scene);

                CookieHandler.setDefault(new CookieManager());

                SwingFXWebViewNew.this.browser = new WebView();

                SwingFXWebViewNew.this.browser.setContextMenuEnabled(false);
                SwingFXWebViewNew.webEngine = SwingFXWebViewNew.this.browser.getEngine();
                SwingFXWebViewNew.this.stage.setMinHeight(700);
                SwingFXWebViewNew.this.stage.setMinWidth(1200);
                JSObject window = (JSObject)SwingFXWebViewNew.webEngine.executeScript("window");
                window.setMember("app", new SwingFXWebViewNew.JavaApplication());
                //SwingFXWebViewNew.webEngine.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

                SwingFXWebViewNew.webEngine.setOnResized(new EventHandler<WebEvent<Rectangle2D>>()
                {
                    public void handle(WebEvent<Rectangle2D> ev)
                    {
                        Rectangle2D r = ev.getData();
                        SwingFXWebViewNew.this.stage.setWidth(r.getWidth());
                        SwingFXWebViewNew.this.stage.setHeight(r.getHeight());
                    }
                });
                SwingFXWebViewNew.webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>()
                {
                    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState)
                    {
                        if (newState == Worker.State.SUCCEEDED)
                        {
                            JSObject window = (JSObject)SwingFXWebViewNew.webEngine.executeScript("window");
                            window.setMember("app", new SwingFXWebViewNew.JavaApplication());
                            if (SwingFXWebViewNew.this.listener != null) {
                            }
                        }
                    }
                });


                ObservableList<Node> children = root.getChildren();
                children.add(SwingFXWebViewNew.this.browser);
                SwingFXWebViewNew.this.jfxPanel.setScene(scene);
                Platform.runLater(new Runnable()
                {
                    public void run()
                    {
                        SwingFXWebViewNew.webEngine.setJavaScriptEnabled(true);
                        SwingFXWebViewNew.webEngine.load("http://slauncher.ru/main/index.html");
                        //SwingFXWebViewNew.webEngine.load("file:///C:/Users/Spark/AppData/Roaming/MRLauncher//index.html");
                        SwingFXWebViewNew.this.listener.completeLoad();
                    }
                });
            }
        });
    }

    public static class JavaApplication
    {

        public static void StartGameLocal(String version)
        {
            LoginForm lf = SLauncher.getInstance().getFrame().mp.defaultScene.loginForm;
            Configuration global = SLauncher.getInstance().getSettings();


            global.set("login.version", version);
            try
            {
                global.save();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            global.store();
            global.get("login.version");
            lf.startLauncher();
        }

        public static void cancelDownload()
                throws IOException
        {
            U.log("stop Downnload");

            SLauncher.getInstance().getDownloader().stopDownload();
        }

        public static void vkButton()
        {
            Runnable run = new Runnable()
            {
                public void run()
                {
                    Desktop d = Desktop.getDesktop();
                    U.log(d);
                    try
                    {
                        d.browse(new URI("https://vk.com/slauncher"));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (URISyntaxException e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            AsyncThread.execute(run);
        }
        public static void openFolder()
        {
            Runnable run = new Runnable()
            {
                public void run()
                {
                    OS.openFolder(MinecraftUtil.getWorkingDirectory());
                }
            };
            AsyncThread.execute(run);
        }

        public static void refreshServers() {}

        public static void toMaincraftSite()
        {
            Runnable run = new Runnable()
            {
                public void run()
                {
                    Desktop d = Desktop.getDesktop();
                    U.log(d);
                    try
                    {
                        d.browse(new URI("https://minecraft.net/store/minecraft"));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (URISyntaxException e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            AsyncThread.execute(run);
        }

        public static void TechSupport()
        {
            Runnable run = new Runnable()
            {
                public void run()
                {
                    Desktop d = Desktop.getDesktop();
                    try
                    {
                        d.browse(new URI("https://vk.me/slauncher"));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (URISyntaxException e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            AsyncThread.execute(run);
        }

        public static void onChangeDirectory(String val)
        {
            Runnable run = new Runnable()
            {
                public EditorFieldHandler directory;

                public void run()
                {
                    final FileExplorer explorer = new FileExplorer();

                    explorer.setFileSelectionMode(1);
                    explorer.setCurrentDirectory(new File(val));

                    int ret = explorer.showDialog(new EditorFileField(val, explorer));
                    if (ret == 0) {
                        Platform.runLater(new Runnable()
                        {
                            public void run()
                            {
                                String path = Paths.get(explorer.getSelectedFile().toPath().toUri()).toFile().getPath();
                                if (OS.is(OS.WINDOWS)) {
                                    path = path.replace(File.separator, File.separator + File.separator);
                                }
                                SwingFXWebViewNew.webEngine.executeScript("setMainPath('" + path + "')");
                            }
                        });
                    }
                }
            };
            AsyncThread.execute(run);
        }

        public static void onChangeJavaPath(String val)
        {
            Runnable run = new Runnable()
            {
                public void run()
                {
                    final FileExplorer explorer = new FileExplorer();

                    explorer.setFileSelectionMode(1);
                    int ret = explorer.showDialog(new EditorFileField(val, explorer));
                    if (ret == 0)
                    {
                        U.log(explorer.getSelectedFile());

                        U.log(explorer.getSelectedFile().toPath());

                        U.log(Paths.get(explorer.getSelectedFile().toPath().toUri()).toFile().getPath());

                        Platform.runLater(new Runnable()
                        {
                            public void run()
                            {
                                String path = Paths.get(explorer.getSelectedFile().toPath().toUri()).toFile().getPath();
                                if (OS.is(OS.WINDOWS)) {
                                    path = path.replace(File.separator, File.separator + File.separator);
                                }
                                SwingFXWebViewNew.webEngine.executeScript("setJavaPath('" + path + "')");
                            }
                        });
                    }
                }
            };
            AsyncThread.execute(run);
        }



        public static void AlertBadPassword()
        {
            Runnable run = new Runnable()
            {
                public void run()
                {
                    Alert.showMessage("Ошибка ", "Неверный логин или пароль!");
                }
            };
            AsyncThread.execute(run);
        }
    }
}
