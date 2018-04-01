package ru.spark.slauncher.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Platform;
import net.minecraft.launcher.updater.VersionSyncInfo;
import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.configuration.Configuration;
import ru.spark.slauncher.managers.ProfileManager;
import ru.spark.slauncher.managers.ServerList;
import ru.spark.slauncher.managers.VersionManager;
import ru.spark.slauncher.minecraft.auth.Account;
import ru.spark.slauncher.minecraft.auth.Account.AccountType;
import ru.spark.slauncher.minecraft.auth.Authenticator;
import ru.spark.slauncher.minecraft.auth.AuthenticatorListener;
import ru.spark.slauncher.ui.SLauncherFrame;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.login.LoginException;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.login.LoginWaitException;
import ru.spark.slauncher.ui.login.VersionComboBox;
import ru.spark.slauncher.ui.scenes.SwingFXWebViewNew;
import ru.spark.util.MinecraftUtil;
import ru.spark.util.OS;
import ru.spark.util.U;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalServer {
    public static int downloaderProgress;
    public static int files;
    public static boolean downloaderComplete;
    public static boolean downloaderStart;
    public static boolean downloaderAbort;
    public static boolean gameStarted;
    public static boolean authentication;
    public static boolean authenticationError;
    public static final AuthenticatorListener authListener = new AuthenticatorListener() {
        @Override
        public void onAuthPassing(final Authenticator var1) {
            U.log("onAuthPassing");
            LocalServer.authentication = false;
            LocalServer.authenticationError = false;
        }

        @Override
        public void onAuthPassingError(final Authenticator var1, final Throwable var2) {
            U.log("onAuthPassingError");
            LocalServer.authentication = false;
            LocalServer.authenticationError = true;
        }

        @Override
        public void onAuthPassed(final Authenticator var1) {
            U.log("onAuthPassed");
            try {
                SLauncher.getInstance().getProfileManager().saveProfiles();

                LocalServer.authentication = true;
                LocalServer.authenticationError = false;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    public static boolean gameVersion;
    private static Configuration settings;

    public static void init() throws Exception {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8000), 2);
                server.createContext("/start_game", new StartGameHandler());
                server.createContext("/create_account", new CreateAccountHandler());
                server.createContext("/set_default_settings", new SetDefaultSettingsHandler());
                server.createContext("/all_accounts", new AccountsHandler());
                server.createContext("/set_account", new SetAccountHandler());
                server.createContext("/save_setting", new SaveSettingHandler());
                server.createContext("/get_setting", new getSettingHandler());
                server.createContext("/get_local_version", new getLocalVersions());
                server.createContext("/install_game", new InstallGame());
                server.createContext("/install_game_status", new InstallGameStatus());
                server.createContext("/set_version_game", new setVersionGame());
                server.createContext("/remove_version_game", new removeLocalVersion());
                server.createContext("/reinstal_version_game", new reInstallVersionGame());
                server.createContext("/tray_start", new TrayStart());
                server.createContext("/auth_acc_status", new AuthAccStatus());
                server.createContext("/get_acc_type", new GetAccType());
                server.createContext("/set_link", new SetLink());
                server.setExecutor(null);
                server.start();
            } catch (BindException e) {
                System.exit(0);
            }
    }

    public static class StartGameHandler implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange t) throws IOException {
            final Map<String, String> params = this.queryToMap(t.getRequestURI().getQuery());
            final String ip = params.get("ip");
            final String name = params.get("name");
            final String version = params.get("version");
            final String context = params.get("context");
            final String callback = params.get("jsoncollback");
            if (LocalServer.gameStarted) {
                final String response = callback + "('{\"response\":{\"game_started\": true, \"status\":true}}')";
                t.sendResponseHeaders(200, response.getBytes().length);
                final OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                Runtime.getRuntime().gc();
                return;
            }
            final VersionManager manager2 = SLauncher.getInstance().getVersionManager();
            final List<VersionSyncInfo> list = manager2.getVersions();
            Boolean isInstalled = false;
            for (final VersionSyncInfo verSyncInfo : list) {
                if (verSyncInfo.getID().equals(version)) {
                    isInstalled = verSyncInfo.isInstalled();
                    break;
                }
            }
            if (!isInstalled) {
                SLauncherFrame.ref.setVisible(true);
                SLauncherFrame.ref.toFront();
                SLauncherFrame.ref.requestFocus();
            }
            LocalServer.downloaderComplete = false;
            LocalServer.downloaderAbort = false;
            LocalServer.downloaderProgress = 0;
            LocalServer.downloaderStart = false;
            final ServerList.Server server = new ServerList.Server();
            server.setName(name);
            server.setVersion(version);
            server.setAddress(ip);
            final LoginForm lf = SLauncher.getInstance().getFrame().mp.defaultScene.loginForm;
            final Configuration global = SLauncher.getInstance().getSettings();
            final String selectedVersion = global.get("login.version");
            global.set("login.version", version);
            global.store();
            if (!LocalServer.gameStarted) {
                InstallGameStatus.currentVersion = version;
                lf.startLauncher(server);
                final String response2 = callback + "('{\"response\":{\"status\":true, \"ip\":\"" + ip + "\", \"context\":\"" + context + "\", \"installed\":" + isInstalled + "}}')";
                t.sendResponseHeaders(200, response2.length());
                final OutputStream os2 = t.getResponseBody();
                os2.write(response2.getBytes());
                os2.close();
                Runtime.getRuntime().gc();
            }
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    public static class GetAccType implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange acc) throws IOException {
            final Map<String, String> params = this.queryToMap(acc.getRequestURI().getQuery());
            final String username = params.get("account");
            final String callback = params.get("jsoncollback");
            final ProfileManager manager = SLauncher.getInstance().getProfileManager();
            final Collection<Account> accounts = manager.getAuthDatabase().getAccounts();
            Account.AccountType type = null;
            for (final Account ac : accounts) {
                if (ac.getDisplayName().equals(username)) {
                    type = ac.getType();
                }
            }
            final String response = callback + "('{\"response\":{\"username\":\"" + username + "\", \"account_type\":\"" + type + "\"}}')";
            U.log(response);
            acc.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = acc.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class TrayStart implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange httpExchange) throws IOException {
            final Map<String, String> params = this.queryToMap(httpExchange.getRequestURI().getQuery());
            final String t = params.get("param");
            SLauncherFrame.ref.setVisible(true);
            SLauncherFrame.ref.toFront();
            SLauncherFrame.ref.requestFocus();
            final String response = "('{\"response\":{\"stop\":\"stop\" }}')";
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class CreateAccountHandler implements HttpHandler
    {
        private Account tempAccount;
        private ProfileManager manager;
        private String selectedAccount;

        @Override
        public void handle(final HttpExchange acc) throws IOException {
            this.manager = SLauncher.getInstance().getProfileManager();
            final Map<String, String> params = this.queryToMap(acc.getRequestURI().getQuery());
            final String username = params.get("username");
            final String account_type = params.get("account_type");
            final String password = params.get("password");
            final String context = params.get("context");
            final String callback = params.get("jsoncollback");
            final String acc_contol = params.get("acc_contol");
            this.manager = SLauncher.getInstance().getProfileManager();
            final Configuration global = SLauncher.getInstance().getSettings();
            if (acc_contol.equals("delete")) {
                Account.AccountType accType = account_type.equals("mojang") ? Account.AccountType.PREMIUM : Account.AccountType.FREE;
                String nick_name = "";
                String viewName = username;
                final Collection<Account> accounts = this.manager.getAuthDatabase().getAccounts();
                for (final Account ac : accounts) {
                    if (ac.getDisplayName().equals(viewName)) {
                        nick_name = ac.getUsername();
                    }
                }
                final Account account = this.manager.getAuthDatabase().getByUsername(nick_name);
                this.manager.getAuthDatabase().unregisterAccount(account);
                this.manager.saveProfiles();
                final String response = callback + "('{\"response\":{\"status\":true }}')";
                acc.sendResponseHeaders(200, response.getBytes().length);
                final OutputStream os = acc.getResponseBody();
                os.write(response.getBytes());
                os.close();
                Runtime.getRuntime().gc();
            } else if (acc_contol.equals("create")) {
                Collection<Account> accounts2 = this.manager.getAuthDatabase().getAccounts();
                boolean editAcctount = false;
                Account account2 = null;
                for (Account ac2 : accounts2) {
                    if (ac2.getDisplayName().equals(username)) {
                        Account.AccountType accType = account_type.equals("mojang") ? Account.AccountType.PREMIUM : Account.AccountType.FREE;
                        ac2.setType(accType);

                        if (accType == AccountType.PREMIUM) {
                            ac2.setPassword(password.toCharArray());
                        }
                        editAcctount = true;
                        account2 = ac2;
                        break;
                    }
                }
                if (!editAcctount) {
                    this.tempAccount = new Account();
                    U.log("createaccount edit1");
                    Account.AccountType accType = Account.AccountType.FREE;
                    if (account_type.contains("mojang")) accType = AccountType.PREMIUM;
                    this.tempAccount.setType(accType);
                    if (accType == Account.AccountType.PREMIUM ) {
                        this.tempAccount.setPassword(password.toCharArray());
                    }
                    this.tempAccount.setUsername(username);
                    this.manager.getAuthDatabase().registerAccount(this.tempAccount);
                    account2 = this.tempAccount;
                    U.log("createaccount edit2");
                }
                if (account2 != null) {
                    global.set("login.account.type", account2.getType().toString());
                    global.save();
                    global.store();
                    if (account2.isPremium()) {
                        account2.getAuthenticator().pass(LocalServer.authListener);
                    }
                }
                SLauncher.getInstance().getProfileManager().saveProfiles();
                Account selected = account2;
                if (selected == null) {
                    return;
                }
                this.selectedAccount = selected.getUsername();
                global.set("login.account", account2.getUsername());
                global.save();
                global.store();
                this.selectedAccount = global.get("login.account");
                final String response2 = callback + "('{\"response\":{\"username\":\"" + username + "\", \"account_type\":\"" + account_type + "\",\"password\":\"" + password + "\", \"context\":\"" + context + "\",\"authentication\":" + LocalServer.authentication + "}}')";
                acc.sendResponseHeaders(200, response2.getBytes().length);
                final OutputStream os2 = acc.getResponseBody();
                os2.write(response2.getBytes());
                os2.close();
                Runtime.getRuntime().gc();
            }
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class AuthAccStatus implements HttpHandler
    {
        public static String currentVersion;

        @Override
        public void handle(final HttpExchange vers_game) throws IOException {
            final Map<String, String> params = this.queryToMap(vers_game.getRequestURI().getQuery());
            final String callback = params.get("jsoncollback");
            final Boolean completed = LocalServer.authentication;
            final Boolean error = LocalServer.authenticationError;
            final JsonObject resultObject = new JsonObject();
            final JsonObject responseObject = new JsonObject();
            responseObject.addProperty("completed", completed);
            responseObject.addProperty("error", error);
            resultObject.add("response", responseObject);
            final String response = callback + "('" + resultObject.toString() + "')";
            vers_game.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = vers_game.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }

        static {
            AuthAccStatus.currentVersion = "";
        }
    }

    static class SetDefaultSettingsHandler implements HttpHandler
    {
        public SLauncher tlauncher;
        public Configuration global;

        @Override
        public void handle(final HttpExchange settings) throws IOException {
            final Map<String, String> params = this.queryToMap(settings.getRequestURI().getQuery());
            final String callback = params.get("jsoncollback");
            this.tlauncher = SLauncher.getInstance();
            this.global = this.tlauncher.getSettings();
            final String[] a = { String.valueOf(925), String.valueOf(530) };
            this.global.set("minecraft.gamedir", MinecraftUtil.getDefaultWorkingDirectory().getAbsolutePath());
            this.global.set("minecraft.size.height", a);
            this.global.set("minecraft.fullscreen", false);
            this.global.set("minecraft.versions.snapshot", true);
            this.global.set("minecraft.versions.old_beta", true);
            this.global.set("minecraft.versions.old_alpha", true);
            this.global.set("minecraft.versions.old", true);
            this.global.set("minecraft.versions.modified", true);
            this.global.get("settings.java.args.jvm");
            this.global.set("settings.java.args.jvm", "");
            this.global.set("settings.java.args.minecraft", "");
            this.global.set("settings.java.path.prompt", "");
            this.global.set("minecraft.memory", 1024);
            this.global.set("gui.console", "none");
            this.global.set("connection", "good");
            this.global.set("minecraft.onlaunch", "hide");
            this.global.set("locale", "ru_RU");
            this.global.set("gui.console.fullcommand", false);
            this.global.set("minecraft.autostart", true);
            final boolean autorun = true;

            String directory = MinecraftUtil.getDefaultWorkingDirectory().getAbsolutePath();
            if (OS.is(OS.WINDOWS)) {
                directory = directory.replace(File.separator, File.separator + File.separator + File.separator + File.separator).replace("+", " ");
            }
            final String response = callback + "('{\"response\":" + "{\"directory\":\"" + directory + "\"," + "\"weight\":\"" + a[0] + "\"," + "\"height\":\"" + a[1] + "\"," + "\"fullscreen\":\"" + false + "\"," + "\"show_snapshots\":\"" + true + "\"," + "\"show_beta\":\"" + true + "\"," + "\"show_alpha\":\"" + true + "\"," + "\"show_old_version\":\"" + true + "\"," + "\"show_mod_version\":\"" + true + "\"," + "\"jvm_arg\":\"" + "" + "\"," + "\"minecraft_arg\":\"" + "" + "\"," + "\"java_path\":\"" + "" + "\"," + "\"memory_alloc\":\"" + 1024 + "\"," + "\"developer_console\":\"" + "none" + "\"," + "\"connection_quality\":\"" + "good" + "\"," + "\"action_on_launch\":\"" + "hide" + "\"," + "\"full_comand\":\"" + false + "\"," + "\"auto_start\":\"" + true + "\"," + "\"language\":\"" + "en_US" + "\"}}')";
            settings.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = settings.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class AccountsHandler implements HttpHandler
    {
        private ProfileManager manager;
        public Configuration global;

        @Override
        public void handle(final HttpExchange httpExchange) throws IOException {
            final Map<String, String> params = this.queryToMap(httpExchange.getRequestURI().getQuery());
            final Random ran = new Random();
            final int rand = ran.nextInt(100000);
            final String callback = params.get("jsoncollback");
            final String defAccount = "SLauncher_";
            try {
                (this.manager = SLauncher.getInstance().getProfileManager()).recreate();
                this.global = SLauncher.getInstance().getSettings();
                final Collection<Account> accounts = this.manager.getAuthDatabase().getAccounts();
                String selected = "";
                if (accounts.size() == 0) {
                    final Account defAcc = new Account();
                    defAcc.setUsername(defAccount + rand);
                    this.manager.getAuthDatabase().registerAccount(defAcc);
                    this.manager.saveProfiles();
                    this.global.set("login.account", defAcc.getUsername());
                    this.global.save();
                    this.global.store();
                }
                else {
                    Boolean existDefAccount = false;
                    for (final Account Acc : accounts) {
                        if (Acc.getUsername().startsWith(defAccount)) {
                            existDefAccount = true;
                            break;
                        }
                    }
                    if (!existDefAccount) {
                        final Account defAcc2 = new Account();
                        defAcc2.setUsername(defAccount + rand);
                        this.manager.getAuthDatabase().registerAccount(defAcc2);
                        SLauncher.getInstance().getProfileManager().saveProfiles();
                        this.global.set("login.account", defAcc2.getUsername());
                        this.global.save();
                        this.global.store();
                    }
                }
                Boolean existSelected = false;
                Account lastAccount = null;
                Account selectedAccount = null;
                for (final Account ac : accounts) {
                    if (ac.getUsername().equals(this.global.get("login.account"))) {
                        existSelected = true;
                        selectedAccount = ac;
                    }
                    lastAccount = ac;
                }
                if (!existSelected && lastAccount != null) {
                    selectedAccount = lastAccount;
                    this.global.set("login.account", lastAccount.getUsername());
                    this.global.save();
                    this.global.store();
                }
                if (selectedAccount != null) {
                    this.global.set("login.account.type", selectedAccount.getType().toString());
                    this.global.save();
                    this.global.store();
                    if (!selectedAccount.isPremium()) {
                        new Authenticator(selectedAccount).asyncPass(LocalServer.authListener);
                    }
                }
                final Gson gson = new Gson();
                final JsonArray list = new JsonArray();
                for (final Account ac2 : accounts) {
                    if (ac2.getUsername().equals(this.global.get("login.account"))) {
                        selected = "active";
                    }
                    else {
                        selected = "";
                    }
                    final JsonElement jelement = new JsonParser().parse("{\"username\":\"" + ac2.getDisplayName() + "\",\"selected\":\"" + selected + "\",\"type\":\"" + ac2.getType() + "\"}");
                    list.add(jelement);
                }
                final JsonObject result = new JsonObject();
                final JsonObject response = new JsonObject();
                response.add("accounts", list);
                final JsonElement jelem = gson.fromJson("true", JsonElement.class);
                response.add("status", jelem);
                result.add("response", response);
                final Headers responseHeaders = httpExchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/plain;charset=UTF-8");
                final String res = callback + "('" + result.toString() + "')";
                httpExchange.sendResponseHeaders(200, res.getBytes().length);
                final OutputStream os = httpExchange.getResponseBody();
                os.write(res.getBytes());
                os.close();
                Runtime.getRuntime().gc();
            }
            catch (IOException ex) {}
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class SetAccountHandler implements HttpHandler
    {
        private ProfileManager manager;
        private String selectedAccount;

        @Override
        public void handle(final HttpExchange setAccount) throws IOException {
            final Map<String, String> params = this.queryToMap(setAccount.getRequestURI().getQuery());
            final String callback = params.get("jsoncollback");
            final String viewName = params.get("account");
            final String context = params.get("context");
            String account = "";
            final Configuration global = SLauncher.getInstance().getSettings();
            this.manager = SLauncher.getInstance().getProfileManager();
            final Collection<Account> accounts = this.manager.getAuthDatabase().getAccounts();
            for (final Account acc : accounts) {
                if (acc.getDisplayName().equals(viewName)) {
                    account = acc.getUsername();
                    U.log(account);
                }
            }
            final Account selected = this.manager.getAuthDatabase().getByUsername(account);
            if (selected != null) {
                global.set("login.account.type", selected.getType().toString());
                global.save();
                global.store();
                if (!selected.isPremium()) {
                    new Authenticator(selected).asyncPass(LocalServer.authListener);
                }
            }
            if (selected == null) {
                return;
            }
            this.selectedAccount = selected.getUsername();
            global.set("login.account", account);
            global.store();
            this.selectedAccount = global.get("login.account");
            final String response = callback + "('{\"response\":{\"username\":\"" + viewName + "\",\"context\":\"" + context + "\",\"selected\":\" active \",\"type\":\"" + selected.getType() + "\"}}')";
            setAccount.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = setAccount.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
            U.log("Selected: " + viewName + " / " + account + " / " + global.get("login.account"));
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class SaveSettingHandler implements HttpHandler
    {
        private SLauncher tlauncher;
        public Configuration global;

        @Override
        public void handle(final HttpExchange setting) throws IOException {
            final Map<String, String> params = this.queryToMap(setting.getRequestURI().getQuery());
            this.tlauncher = SLauncher.getInstance();
            this.global = this.tlauncher.getSettings();
            String directory = params.get("directory");
            final String weight = params.get("weight");
            final String height = params.get("height");
            final String fullscreen = params.get("fullscreen");
            final String show_snapshots = params.get("show_snapshots");
            final String show_beta = params.get("show_beta");
            final String show_alpha = params.get("show_alpha");
            final String show_old_version = params.get("show_old_version");
            final String show_mod_version = params.get("show_mod_version");
            final String full_comand = params.get("full_comand");
            final String autostart = params.get("autostart");
            final boolean autorun = Boolean.parseBoolean(autostart);
            final String jvm_arg = params.get("jvm_arg");
            final String minecraft_arg = params.get("minecraft_arg");
            String java_path = params.get("java_path");
            final String memory_alloc = params.get("memory_alloc");
            final String developer_console = params.get("developer_console").replace("+", " ");
            final String connection_quality = params.get("connection_quality");
            final String action_on_launch = params.get("action_on_launch");
            final String language = params.get("language");
            final String callback = params.get("jsoncollback");
            final String a = weight + ";" + height;
            this.global.set("minecraft.size", a);
            this.global.set("minecraft.fullscreen", fullscreen);
            this.global.set("minecraft.versions.snapshot", show_snapshots);
            this.global.set("minecraft.versions.old_beta", show_beta);
            this.global.set("minecraft.versions.old_alpha", show_alpha);
            this.global.set("minecraft.versions.old", show_old_version);
            this.global.set("minecraft.versions.modified", show_mod_version);
            this.global.get("settings.java.args.jvm");
            this.global.set("settings.java.args.jvm", jvm_arg);
            this.global.set("settings.java.args.minecraft", minecraft_arg);
            this.global.set("settings.java.path.prompt", java_path);
            this.global.set("minecraft.memory", memory_alloc);
            this.global.set("gui.console", developer_console);
            this.global.set("connection", connection_quality);
            this.global.set("minecraft.onlaunch", action_on_launch);
            this.global.set("gui.console.fullcommand", full_comand);
            this.global.set("locale", language);
            this.global.set("minecraft.autostart", autostart);
            this.global.save();
            if (OS.is(OS.WINDOWS)) {
                if (java_path != null) {
                    java_path = java_path.replace(File.separator, File.separator + File.separator + File.separator + File.separator).replace("+", " ");
                }
                if (directory != null) {
                    directory = directory.replace(File.separator, File.separator + File.separator + File.separator + File.separator).replace("+", " ");
                }
            }
            final String response = callback + "('{\"response\":" + "{\"directory\":\"" + directory + "\"," + "\"weight\":\"" + weight + "\"," + "\"height\":\"" + height + "\"," + "\"fullscreen\":\"" + fullscreen + "\"," + "\"show_snapshots\":\"" + show_snapshots + "\"," + "\"show_beta\":\"" + show_beta + "\"," + "\"show_alpha\":\"" + show_alpha + "\"," + "\"show_old_version\":\"" + show_old_version + "\"," + "\"show_mod_version\":\"" + show_mod_version + "\"," + "\"jvm_arg\":\"" + jvm_arg + "\"," + "\"minecraft_arg\":\"" + minecraft_arg + "\"," + "\"java_path\":\"" + java_path + "\"," + "\"memory_alloc\":\"" + memory_alloc + "\"," + "\"developer_console\":\"" + developer_console + "\"," + "\"connection_quality\":\"" + connection_quality + "\"," + "\"action_on_launch\":\"" + action_on_launch + "\"," + "\"auto_start\":\"" + autostart + "\"," + "\"full_comand\":\"" + full_comand + "\"," + "\"language\":\"" + language + "\"}}')";
            setting.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = setting.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
            Alert.showMessage("Настройки", "Все настройки сохранены!");
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class getSettingHandler implements HttpHandler
    {
        private SLauncher tlauncher;
        public Configuration global;

        @Override
        public void handle(final HttpExchange setting) throws IOException {
            final Map<String, String> params = this.queryToMap(setting.getRequestURI().getQuery());
            final String callback = params.get("jsoncollback");
            this.tlauncher = SLauncher.getInstance();
            this.global = this.tlauncher.getSettings();
            String directory = this.global.get("minecraft.gamedir");
            final String height = this.global.get("minecraft.size.height");
            final String size = this.global.get("minecraft.size");
            final String[] array = size.split("\\;");
            final String fullscreen = this.global.get("minecraft.fullscreen");
            final String snapshot = this.global.get("minecraft.versions.snapshot");
            final String old_beta = this.global.get("minecraft.versions.old_beta");
            final String old_alpha = this.global.get("minecraft.versions.old_alpha");
            final String old = this.global.get("minecraft.versions.old");
            final String modified = this.global.get("minecraft.versions.modified");
            final String args_jvm = this.global.get("settings.java.args.jvm");
            final String args_minecraft = this.global.get("settings.java.args.minecraft");
            String java_path = this.global.get("settings.java.path.prompt");
            final String memory = this.global.get("minecraft.memory");
            final String console = this.global.get("gui.console");
            final String connection = this.global.get("connection");
            final String onlaunch = this.global.get("minecraft.onlaunch");
            final String full_comand = this.global.get("gui.console.fullcommand");
            final String locale = this.global.get("locale");
            final String autostart = this.global.get("minecraft.autostart");
            if (OS.is(OS.WINDOWS)) {
                if (java_path != null) {
                    java_path = java_path.replace(File.separator, File.separator + File.separator + File.separator + File.separator).replace("+", " ");
                }
                if (directory != null) {
                    directory = directory.replace(File.separator, File.separator + File.separator + File.separator + File.separator).replace("+", " ");
                }
            }
            final String response = callback + "('{\"response\":" + "{\"directory\":\"" + directory + "\"," + "\"weight\":\"" + array[0] + "\"," + "\"height\":\"" + array[1] + "\"," + "\"fullscreen\":\"" + fullscreen + "\"," + "\"show_snapshots\":\"" + snapshot + "\"," + "\"show_beta\":\"" + old_beta + "\"," + "\"show_alpha\":\"" + old_alpha + "\"," + "\"show_old_version\":\"" + old + "\"," + "\"show_mod_version\":\"" + modified + "\"," + "\"jvm_arg\":\"" + args_jvm + "\"," + "\"minecraft_arg\":\"" + args_minecraft + "\"," + "\"java_path\":\"" + java_path + "\"," + "\"memory_alloc\":\"" + memory + "\"," + "\"auto_start\":\"" + autostart + "\"," + "\"developer_console\":\"" + console + "\"," + "\"connection_quality\":\"" + connection + "\"," + "\"action_on_launch\":\"" + onlaunch + "\"," + "\"full_comand\":\"" + full_comand + "\"," + "\"language\":\"" + locale + "\"}}')";
            setting.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = setting.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class getLocalVersions implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange version) throws IOException {
            final Map<String, String> params = this.queryToMap(version.getRequestURI().getQuery());
            final String callback = params.get("jsoncollback");
            final VersionManager manager2 = SLauncher.getInstance().getVersionManager();
            final List<VersionSyncInfo> list = manager2.getVersions();
            final Gson gson = new Gson();
            final JsonArray list2 = new JsonArray();
            final Configuration global = SLauncher.getInstance().getSettings();
            Boolean existSelected = false;
            VersionSyncInfo lastSyncInfo = null;
            for (final VersionSyncInfo verSyncInfo : list) {
                if (verSyncInfo.getID().equals(global.get("login.version").replace("+", " "))) {
                    existSelected = true;
                    break;
                }
                lastSyncInfo = verSyncInfo;
            }
            if (!existSelected && lastSyncInfo != null) {
                global.set("login.version", lastSyncInfo.getID().replace("+", " "));
                global.save();
                global.store();
            }
            String selected = "";
            for (final VersionSyncInfo verSyncInfo2 : list) {
                if (verSyncInfo2.getID().equals(global.get("login.version").replace("+", " "))) {
                    selected = "active";
                }
                else {
                    selected = "";
                }
                final JsonElement jelement = new JsonParser().parse("{\"id\":\"" + verSyncInfo2.getID() + "\",\"installed\":\"" + verSyncInfo2.isInstalled() + "\",\"selected\":\"" + selected + "\"}");
                list2.add(jelement);
            }
            final JsonObject response = new JsonObject();
            response.add("response", list2);
            final String res = callback + "('" + response.toString() + "')";
            version.sendResponseHeaders(200, res.getBytes().length);
            final OutputStream os = version.getResponseBody();
            os.write(res.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class InstallGameStatus implements HttpHandler
    {
        public static String currentVersion;

        @Override
        public void handle(final HttpExchange vers_game) throws IOException {
            final Map<String, String> params = this.queryToMap(vers_game.getRequestURI().getQuery());
            final String callback = params.get("jsoncollback");
            final Boolean completed = LocalServer.downloaderComplete;
            final Boolean started = LocalServer.downloaderStart;
            final Boolean error = LocalServer.downloaderAbort;
            final Integer progress = LocalServer.downloaderProgress;
            final Integer files = LocalServer.files;
            final JsonObject resultObject = new JsonObject();
            final JsonObject responseObject = new JsonObject();
            responseObject.addProperty("status", Boolean.valueOf(true));
            responseObject.addProperty("completed", completed);
            responseObject.addProperty("started", started);
            responseObject.addProperty("error", error);
            responseObject.addProperty("progress", progress);
            responseObject.addProperty("files", files);
            if (completed) {
                final VersionManager manager2 = SLauncher.getInstance().getVersionManager();
                manager2.asyncRefresh();
            }
            resultObject.add("response", responseObject);
            final String response = callback + "('" + resultObject.toString() + "')";
            vers_game.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = vers_game.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }

        static {
            InstallGameStatus.currentVersion = "";
        }
    }

    static class InstallGame implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange vers_game) throws IOException {
            final Map<String, String> params = this.queryToMap(vers_game.getRequestURI().getQuery());
            final String version = params.get("version").replace("+", " ");
            final String callback = params.get("jsoncollback");
            final VersionManager manager2 = SLauncher.getInstance().getVersionManager();
            final List<VersionSyncInfo> list = manager2.getVersions();
            LoginForm lf = SLauncher.getInstance().getFrame().mp.defaultScene.loginForm;
            Boolean isInstalled = false;
            for (final VersionSyncInfo verSyncInfo : list) {
                if (verSyncInfo.getID().equals(version)) {
                    isInstalled = verSyncInfo.isInstalled();
                    lf.versions.setSelectedValue(verSyncInfo);

                    if (!isInstalled) {
                        manager2.downloadVersion(verSyncInfo, true);
                    }
                    break;
                }
            }
            LocalServer.files = 0;
            LocalServer.downloaderComplete = false;
            LocalServer.downloaderAbort = false;
            LocalServer.downloaderProgress = 0;
            LocalServer.downloaderStart = false;
            InstallGameStatus.currentVersion = version;

            Configuration global = SLauncher.getInstance().getSettings();
            global.set("login.version", version);
            global.save();
            global.store();
            global.get("login.version");

            lf.startLauncher();
            final String response = callback + "('{\"response\":{\"status\":true, \"version\":\"" + version + "\", \"selected\":\"" + "active" + "\",\"installed\":" + isInstalled + " }}')";
            vers_game.sendResponseHeaders(200, response.getBytes().length);
            final OutputStream os = vers_game.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();

        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class setVersionGame implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange vers_game) throws IOException {
            final Map<String, String> params = this.queryToMap(vers_game.getRequestURI().getQuery());
            final String version = params.get("version").replace("+", " ");
            final String callback = params.get("jsoncollback");
            final VersionManager manager2 = SLauncher.getInstance().getVersionManager();
            manager2.getVersionSyncInfo(version);
            final Configuration global = SLauncher.getInstance().getSettings();
            global.set("login.version", version);
            final String response = callback + "('{\"response\":{\"status\":true, \"id\":\"" + version + "\", \"selected\":\"" + "active" + "\",\"installed\":\"" + "true" + "\"}}')";
            vers_game.sendResponseHeaders(200, response.toString().getBytes().length);
            final OutputStream os = vers_game.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class removeLocalVersion implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange remove_version) throws IOException {
            final Map<String, String> params = this.queryToMap(remove_version.getRequestURI().getQuery());
            final String version = params.get("version").replace("+", " ");
            final String callback = params.get("jsoncollback");
            final VersionManager manager2 = SLauncher.getInstance().getVersionManager();
            final List<VersionSyncInfo> list = manager2.getVersions();
            final Gson gson = new Gson();
            final JsonArray list2 = new JsonArray();
            final Configuration global = SLauncher.getInstance().getSettings();
            SLauncher.getInstance().getDownloader().stopDownload();
            manager2.getLocalList().refreshVersions();
            manager2.getLocalList().deleteVersion(version, true);
            manager2.getLocalList().refreshVersions();
            final String response = callback + "('{\"response\":{\"status\":true, \"id\":\"" + version + "\", \"selected\":\"" + "" + "\",\"installed\":\"" + "false" + "\"}}')";
            remove_version.sendResponseHeaders(200, response.toString().getBytes().length);
            final OutputStream os = remove_version.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }

    static class reInstallVersionGame implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange version_game) throws IOException {
            final Map<String, String> params = this.queryToMap(version_game.getRequestURI().getQuery());
            final String version = params.get("version").replace("+", " ");
            final String callback = params.get("jsoncollback");
            final VersionManager manager2 = SLauncher.getInstance().getVersionManager();
            manager2.getLocalList().deleteVersion(version, true);
            LocalServer.files = 0;
            LocalServer.downloaderComplete = false;
            LocalServer.downloaderAbort = false;
            LocalServer.downloaderProgress = 0;
            LocalServer.downloaderStart = false;
            InstallGameStatus.currentVersion = version;
            final LoginForm lf = SLauncher.getInstance().getFrame().mp.defaultScene.loginForm;
            final Configuration global = SLauncher.getInstance().getSettings();
            global.set("login.version", version);
            global.store();
            global.get("login.version");
            lf.startLauncher();
            manager2.getLocalList().refreshVersions();
            final String response = callback + "('{\"response\":{\"status\":true, \"id\":\"" + version + "\",\"installed\":\"" + LocalServer.downloaderComplete + "\"}}')";
            version_game.sendResponseHeaders(200, response.toString().getBytes().length);
            final OutputStream os = version_game.getResponseBody();
            os.write(response.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }


    static class SetLink implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange link) throws IOException {
            final Map<String, String> params = this.queryToMap(link.getRequestURI().getQuery());
            final String callback = params.get("jsoncollback");
            final String type = params.get("type");
            final String id = params.get("id");
            if (type.equals("cancelDownload")) {
                SwingFXWebViewNew.JavaApplication.cancelDownload();
            }
            if (type.equals("onVkButton")) {
                SwingFXWebViewNew.JavaApplication.vkButton();
            }
            if (type.equals("openFolder")) {
                SwingFXWebViewNew.JavaApplication.openFolder();
            }
            if (type.equals("toMaincraftSite")) {
                SwingFXWebViewNew.JavaApplication.toMaincraftSite();
            }
            if (type.equals("TechSupport")) {
                SwingFXWebViewNew.JavaApplication.TechSupport();
            }
            if (type.equals("onChangeDirectory")) {
                SwingFXWebViewNew.JavaApplication.onChangeDirectory(id);
            }
            if (type.equals("onChangeJavaPath")) {
                SwingFXWebViewNew.JavaApplication.onChangeJavaPath(id);
            }
            if (type.equals("StartGameLocal")) {
                SwingFXWebViewNew.JavaApplication.StartGameLocal(id);
            }
            if (type.equals("AlertBadPassword")) {
                SwingFXWebViewNew.JavaApplication.AlertBadPassword();
            }
            final String result = callback + "('{\"response\":{\"status\":true, \"id\":\"" + id + "\"}}')";
            final Headers responseHeaders = link.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/javascript; charset=UTF-8");
            link.sendResponseHeaders(200, result.getBytes().length);
            final OutputStream os = link.getResponseBody();
            os.write(result.getBytes());
            os.close();
            Runtime.getRuntime().gc();
        }

        public Map<String, String> queryToMap(final String query) {
            final Map<String, String> result = new HashMap<String, String>();
            for (final String param : query.split("&")) {
                final String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                }
                else {
                    result.put(pair[0], "");
                }
            }
            return result;
        }
    }
}
