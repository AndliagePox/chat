import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            String msg;
            Scanner scanner = new Scanner(System.in);
            System.out.println("输入服务端地址：");
            Socket socket = new Socket(scanner.nextLine(),7523);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);
            new Thread(()-> {
                String mess;
                try {
                    while (true) {
                        mess = dis.readUTF();
                        System.out.println(mess);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                msg = scanner.nextLine();
                dos.writeUTF(msg);
                dos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
