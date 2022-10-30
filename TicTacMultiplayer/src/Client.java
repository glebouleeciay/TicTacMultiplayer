import java.io.*;
import java.net.Socket;

class Client {

    public static String ipAddr = "localhost";
    public static int port = 8080;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String addr;

    public Client(String addr, int port) {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            Client.this.downService();
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                System.out.println("Server broke connection");
            }
        } catch (IOException ignored) {
        }
    }


    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str == null) {
                        Client.this.downService();
                        break;
                    }
                    System.out.println(str);
                }
            } catch (IOException e) {
                Client.this.downService();
            }
        }
    }


    public class WriteMsg extends Thread {
        @Override
        public void run() {
            while (!socket.isClosed()) {
                String userWord;
                try {
                    userWord = inputUser.readLine();
                    out.write(userWord + "\n");
                    out.flush();
                } catch (IOException e) {
                    Client.this.downService();
                }
            }
        }
    }


    public static void main(String[] args) {
        try {
            new Client(ipAddr, port);
            System.out.println("The connection was successful");
        } catch (NullPointerException e) {
            System.out.println("Connection error, make sure that the server is running and functioning properly, then restart the class");
        }


    }

}