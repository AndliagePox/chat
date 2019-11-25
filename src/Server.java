import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ServerThread> threads = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Server server = new Server();
            ServerSocket serverSocket = new ServerSocket(7523);
            System.out.println("服务运行中……");
            while (true) {
                Socket socket = serverSocket.accept();
                server.addThread(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendToAll(String msg) {
        for (ServerThread t: threads) {
            t.sendMsg(msg);
        }
    }

    private void addThread(Socket socket) {
        ServerThread st = new ServerThread(this, socket);
        threads.add(st);
        st.start();
    }
}

class ServerThread {
    private DataInputStream dis;
    private DataOutputStream dos;
    private Thread thread;

    ServerThread(Server server, Socket socket) {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.thread = new Thread(() -> {
            try {
                String msg;
                while (true) {
                    msg = "[" + socket.getInetAddress() + ":" + socket.getPort() + "]: " + dis.readUTF();
                    System.out.println(msg);
                    server.sendToAll(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "] 已连接");
    }

    void sendMsg(String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void start() {
        this.thread.start();
    }
}