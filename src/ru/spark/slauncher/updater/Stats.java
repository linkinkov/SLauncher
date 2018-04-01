package ru.spark.slauncher.updater;

import net.minecraft.launcher.Http;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.managers.ServerList;
import ru.spark.slauncher.minecraft.auth.Account;
import ru.spark.slauncher.minecraft.crash.Crash;
import ru.spark.util.OS;
import ru.spark.util.U;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Stats {
    private static final ExecutorService service = null;
    private static boolean allow = true;

    public static boolean getAllowed()
    {
        return allow;
    }

    public static void setAllowed(boolean allowed)
    {
        allow = allowed;
    }

    public static void minecraftLaunched(Account account, CompleteVersion version, ServerList.Server server)
    {
        Args args = newAction("mc_launched").add("mc_version", version.getID()).add("account", account.getDisplayName()).add("account_type", account.getType().toString());
        if (server != null) {
            args.add("server_address", server.getAddress());
        }
        submitDenunciation(args);
    }

    public static void minecraftCrashed(CompleteVersion version, Crash crash)
    {
        Args args = newAction("mc_crashed")
                .add("mc_version", version.getID());
        if ((crash == null) || (crash.getSignatures().isEmpty())) {
            args.add("mc_crash_id", "unknown");
        } else {
            args.add("mc_crash_id", crash.getSignatures().get(0).getPath());
        }
        submitDenunciation(args);
    }


    private static Args newAction(String name)
    {
        Args args = new Args()
                .add("client", "client")
                .add("lversion", String.format(Locale.US, "%.2f", Double.valueOf(SLauncher.getVersion())))
                .add("launcher_type", SLauncher.getBrand())
                .add("launcher_release", SLauncher.isBeta() ? "beta" : "release")
                .add("launcher_os", OS.CURRENT.getName())
                .add("locale", SLauncher.getInstance().getLang().getSelected().toString()).add("action", name);
        return args;
    }

    private static void submitDenunciation(Args args)
    {
        if (allow) {
            try {
                Stats.performGetRequest(new URL("http://slauncher.ru:1337/checker"), Stats.toRequest(args));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String toRequest(Args args)
    {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<String, String> arg : args.map.entrySet()) {
            b.append('&').append(Http.encode(arg.getKey())).append('=').append(Http.encode(arg.getValue()));
        }
        return b.substring(1);
    }

    private static HttpURLConnection createUrlConnection(URL url)
            throws IOException
    {
        debug("Opening connection to " + url);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(U.getConnectionTimeout());
        connection.setReadTimeout(U.getReadTimeout());
        connection.setUseCaches(false);
        return connection;
    }

    public static String performGetRequest(URL url, String request)
            throws IOException
    {
        String var7;
        url = new URL(url.toString() + '?' + request);
        HttpURLConnection connection = createUrlConnection(url);
        debug("Reading data from " + url);
        InputStream inputStream = null;
        try
        {
            inputStream = connection.getInputStream();
            String e = IOUtils.toString(inputStream, Charsets.UTF_8);
            debug("Successful read, server response was " + connection.getResponseCode());
            debug("Response: " + e);
            String string = e;
            return string;
        }
        catch (IOException var10)
        {
            IOUtils.closeQuietly(inputStream);
            inputStream = connection.getErrorStream();
            if (inputStream == null)
            {
                debug("Request failed", var10);
                throw var10;
            }
            debug("Reading error page from " + url);
            String result = IOUtils.toString(inputStream, Charsets.UTF_8);
            debug("Successful read, server response was " + connection.getResponseCode());
            debug("Response: " + result);
            var7 = result;
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }

        return var7;
    }

    private static void debug(Object... o)
    {
        if (SLauncher.isBeta()) {
            U.log("[Stats]", o);
        }
    }

    private static class Args
    {
        private final LinkedHashMap<String, String> map = new LinkedHashMap();

        public Args add(String key, String value)
        {
            if (this.map.containsKey(key)) {
                this.map.remove(key);
            }
            this.map.put(key, value);
            return this;
        }
    }
}