package fr.bakaaless.sksocket.addon.type;

import fr.bakaaless.sksocket.addon.event.EventServerReceiveData;
import fr.bakaaless.sksocket.plugin.SkSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class AdaptClient {

    private final UUID uniqueId;
    private final AdaptServerSocket server;
    private final Socket socket;
    private BufferedReader reader;
    private Thread readThread;
    private PrintWriter writer;

    public AdaptClient(final AdaptServerSocket server, final Socket socket) {
        this.uniqueId = UUID.randomUUID();
        this.server = server;
        this.socket = socket;
        this.reader = null;
        this.writer = null;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.readThread = new Thread(this::read);
            this.readThread.setDaemon(true);
            this.readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        while(this.isConnected()) {
            try {
                final String data = this.reader.readLine();
                if (data == null)
                    continue;
                final EventServerReceiveData event = new EventServerReceiveData(this.server, this, data);
                SkSocket.get().getServer().getPluginManager().callEvent(event);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return this.getSocket() != null && this.getSocket().isConnected() && !this.getSocket().isClosed();
    }

    public void disconnect() {
        if (!this.getSocket().isClosed()) {
            try {
                this.getSocket().close();
                this.getReader().close();
                this.getWriter().close();
                this.readThread.interrupt();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public AdaptServerSocket getServer() {
        return this.server;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public BufferedReader getReader() {
        return this.reader;
    }

    public PrintWriter getWriter() {
        return this.writer;
    }

}
