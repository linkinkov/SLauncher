package com.spark.slauncher;

import ru.spark.slauncher.handlers.LocalServer;
import ru.spark.util.OS;
import ru.spark.util.SwingUtil;
import ru.spark.util.U;
import sun.misc.Launcher;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SLauncher {
    public static String test = "";
    public static boolean gameStatusInstall;
    public static boolean isGameStatusStart;
    protected static PreloaderFrame frame;

    public static void main(String[] args) {


        TrustManager[] trustAllCerts = { new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        } };
        SSLContext sc = null;
        try
        {
            sc = SSLContext.getInstance("SSL");

            sc.init(null, trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (KeyManagementException e)
        {
            e.printStackTrace();
        }
        String jarFilePath = null;
        try {
            jarFilePath = Paths.get(SLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String decodedPath = jarFilePath;
        File jarFile = new File(decodedPath);
        try {
            LocalServer.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new PreloaderFrame();

        SwingUtil.setFavicons(frame);

        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setVisible(false);
        frame.getWidth();
        if (OS.is(OS.WINDOWS) || OS.is(OS.OSX)) {
            frame.setBackground(new Color(0, 0, 0, 0));
        }
        frame.setVisible(true);
        PreloaderFrame.getInstance().setProgress(0.02F, " ");
        if (jarFile.isFile()) {
            copyFolderFormJarToStroe("resources/ru/turikhay/tlauncher/ui/scenes", appStoreDir().getAbsolutePath());
            U.log("test dir:" + appStoreDir().getAbsolutePath());
        } else {
            copyFolderToStore("resources/ru/turikhay/tlauncher/ui/scenes", appStoreDir().getAbsolutePath());
            U.log("test dir:" + appStoreDir().getAbsolutePath());
        }
        createFile();

        PreloaderFrame.getInstance().setProgress(0.03F, " ");

        ru.spark.slauncher.SLauncher.main(args);
    }


    public static void createFile() {
        try {
            File file = new File(appStoreDir().getAbsolutePath() + File.separatorChar + "SLauncher_v_" + 1.7D + ".txt");

            U.log(appStoreDir().getAbsolutePath() + File.separatorChar + "SLauncher_v_" + 1.7D + ".txt");

            String content = "Version=1.7\nSlauncher=Installed";
            if (!file.exists()) {
                file.createNewFile();
                gameStatusInstall = true;
                isGameStatusStart = true;

                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();

                U.log("file NOT exsist ");
            } else {
                isGameStatusStart = true;
                gameStatusInstall = false;
                U.log("file exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File appStoreDir() {
        String workingDirectory;
        if (OS.is(OS.WINDOWS)) {
            workingDirectory = System.getenv("AppData");
        } else {
            workingDirectory = System.getProperty("user.home");

            workingDirectory = workingDirectory + "/Library/Application Support";
        }
        File file = new File(workingDirectory + File.separatorChar + "SLauncher");

        file.mkdirs();

        return file;
    }


    public static void copyFolderFormJarToStroe(String src_path, String dest_path) {
        try {
            OutputStream outputStream = null;
            InputStream inputStream = null;

            String path = src_path;
            File jarFile = null;
            try {
                jarFile = new File(Paths.get(SLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile().getPath());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            JarFile jar = new JarFile(jarFile);

            Enumeration<JarEntry> entries = jar.entries();

            String out = "";
            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();

                String entryName = entry.getName();
                if (entryName.startsWith(path + "/")) {
                    String fileOrDirName = entryName.substring(entryName.lastIndexOf("/") + 1);

                    int beginIndex = entryName.lastIndexOf(path + "/") + (path + "/").length();
                    int endIndex = entryName.lastIndexOf("/");
                    String pathToFile = "";
                    if (beginIndex < endIndex) {
                        pathToFile = entryName.substring(beginIndex, endIndex);
                    }
                    Boolean isRootFile = false;
                    if (pathToFile.equals("")) {
                        isRootFile = true;
                    } else {
                        new File(dest_path + File.separatorChar + pathToFile).mkdirs();
                    }
                    if (!entry.isDirectory()) {
                        inputStream = jar.getInputStream(entry);

                        String dest = dest_path + File.separatorChar + fileOrDirName;
                        if (!isRootFile.booleanValue()) {
                            dest = dest_path + File.separatorChar + pathToFile + File.separatorChar + fileOrDirName;
                        }
                        File outFile = new File(dest);

                        outFile.createNewFile();

                        outputStream = new FileOutputStream(outFile);

                        int read = 0;
                        byte[] bytes = new byte['?'];
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                        System.out.println(entryName);

                        test = test + entryName + " /n";
                    }
                }
            }
            jar.close();
        } catch (IOException e) {
            e.printStackTrace();

            test += " error ";
            return;
        }
    }


    public static void copyFolderToStore(String src_path, String dest_path) {
        try {
            OutputStream outputStream = null;
            InputStream inputStream = null;

            U.log("dest_path:" + dest_path);

            File jarFile = new File(Paths.get(SLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile().getPath());

            URL url = Launcher.class.getResource("/" + src_path);
            if (url != null) {
                File apps = new File(url.toURI());
                for (File app : apps.listFiles()) {
                    System.out.println(app);

                    String fileOrDirName = app.getName();

                    U.log(new Object[]{"fileOrDirName:" + fileOrDirName});
                    if (app.isDirectory()) {
                        new File(dest_path + File.separatorChar + fileOrDirName).mkdirs();

                        copyFolderToStore(src_path + "/" + fileOrDirName, dest_path + File.separatorChar + fileOrDirName);
                    } else {
                        inputStream = new FileInputStream(app);

                        File outFile = new File(dest_path + "/" + fileOrDirName);

                        outFile.createNewFile();

                        outputStream = new FileOutputStream(outFile);

                        int read = 0;
                        byte[] bytes = new byte['?'];
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            test += " error ";
        } catch (URISyntaxException localURISyntaxException) {
        }
    }
}
