package ru.spark.slauncher.managers;

import net.minecraft.common.CompressedStreamTools;
import net.minecraft.common.NBTTagCompound;
import net.minecraft.common.NBTTagList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerList
{
    private static ArrayList<Server> list;

    public ServerList() {
        ServerList.list = new ArrayList<Server>();
    }

    public static void add(final Server server) {
        if (server == null) {
            throw new NullPointerException();
        }
        ServerList.list.add(server);
    }

    public boolean remove(final Server server) {
        if (server == null) {
            throw new NullPointerException();
        }
        return ServerList.list.remove(server);
    }

    public boolean contains(final Server server) {
        return ServerList.list.contains(server);
    }

    public boolean isEmpty() {
        return ServerList.list.isEmpty();
    }

    public ArrayList<ServerList.Server> getList() {
        return list;
    }

    public static void save(File file) throws IOException {
        final NBTTagList servers = new NBTTagList();
        for (final Server server : ServerList.list) {
            servers.appendTag(server.getNBT());
        }
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("servers", servers);
        CompressedStreamTools.safeWrite(compound, file);
    }

    @Override
    public String toString() {
        return "ServerList{" + ServerList.list.toString() + "}";
    }

    public static ServerList loadFromFile(final File file) throws IOException {
        final ServerList serverList = new ServerList();
        final List<Server> list = ServerList.list;
        final NBTTagCompound compound = CompressedStreamTools.read(file);
        if (compound == null) {
            return serverList;
        }
        final NBTTagList servers = compound.getTagList("servers");
        for (int i = 0; i < servers.tagCount(); ++i) {
            list.add(Server.loadFromNBT((NBTTagCompound)servers.tagAt(i)));
        }
        return serverList;
    }
    public static ServerList mergeServerList(final ServerList pref, final ServerList add) {
        final ServerList serverList = new ServerList();
        final List<Server> list = serverList.getList();
        final List<Server> prefList = pref.getList();
        final List<Server> addList = add.getList();
        serverList.getList().addAll(prefList);
        for (final Server server : addList) {
            if (list.contains(server)) {
                continue;
            }
            list.add(server);
        }
        return serverList;
    }

    public static ServerList sortLists(final ServerList pref, final ServerList add) {
        final ServerList serverList = new ServerList();
        final List<Server> list = ServerList.list;
        final List<Server> prefList = ServerList.list;
        final List<Server> addList = ServerList.list;
        serverList.list.addAll(prefList);
        for (final Server server : addList) {
            if (!list.contains(server)) {
                list.add(server);
            }
        }
        return serverList;
    }

    public static class Server
    {
        private String name;
        private String version;
        private String address;
        private boolean hideAddress;
        private int acceptTextures;

        public String getName() {
            return this.name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(final String version) {
            this.version = version;
        }

        public String getAddress() {
            return this.address;
        }

        public void setAddress(final String address) {
            this.address = address;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Server)) {
                return false;
            }
            final Server server = (Server)obj;
            return server.address.equals(this.address);
        }

        @Override
        public String toString() {
            return "{'" + this.name + "', '" + this.address + "', '" + this.version + "'}";
        }

        public static Server loadFromNBT(final NBTTagCompound nbt) {
            final Server server = new Server();
            server.name = nbt.getString("name");
            server.address = nbt.getString("ip");
            server.hideAddress = nbt.getBoolean("hideAddress");
            if (nbt.hasKey("acceptTextures")) {
                server.acceptTextures = (nbt.getBoolean("acceptTextures") ? 1 : -1);
            }
            return server;
        }

        public NBTTagCompound getNBT() {
            final NBTTagCompound compound = new NBTTagCompound();
            compound.setString("name", this.name);
            compound.setString("ip", this.address);
            compound.setBoolean("hideAddress", this.hideAddress);
            if (this.acceptTextures != 0) {
                compound.setBoolean("acceptTextures", this.acceptTextures == 1);
            }
            return compound;
        }
    }
}
