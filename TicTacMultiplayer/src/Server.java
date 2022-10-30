import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;


class Server extends Thread {

    static String[] board;
    static String turn;
    public static Socket socket;
    public static Socket socket2;
    private static BufferedReader in;
    private static BufferedReader in2;
    private static BufferedWriter out;
    private static BufferedWriter out2;
    public static final int PORT = 8080;
    public static LinkedList<Socket> sockets = new LinkedList<>();

    static String checkWinner() {
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> board[0] + board[1] + board[2];
                case 1 -> board[3] + board[4] + board[5];
                case 2 -> board[6] + board[7] + board[8];
                case 3 -> board[0] + board[3] + board[6];
                case 4 -> board[1] + board[4] + board[7];
                case 5 -> board[2] + board[5] + board[8];
                case 6 -> board[0] + board[4] + board[8];
                case 7 -> board[2] + board[4] + board[6];
                default -> null;
            };

            if (line.equals("XXX")) {
                return "X";
            } else if (line.equals("OOO")) {
                return "O";
            }
        }

        for (int a = 0; a < 9; a++) {
            if (Arrays.asList(board).contains(
                    String.valueOf(a + 1))) {
                break;
            } else if (a == 8) {
                return "draw";
            }
        }
        return null;
    }

    static void printBoard() {
        try {
            out.write("|---|---|---|" + "\n" +
                    "| " + board[0] + " | " + board[1] + " | " + board[2] + " |" + "\n" +
                    "|-----------|" + "\n" +
                    "| " + board[3] + " | " + board[4] + " | " + board[5] + " |" + "\n" +
                    "|-----------|" + "\n" +
                    "| " + board[6] + " | " + board[7] + " | " + board[8] + " |" + "\n" +
                    "|---|---|---|" + "\n");
            out.flush();
            out2.write("|---|---|---|" + "\n" +
                    "| " + board[0] + " | " + board[1] + " | " + board[2] + " |" + "\n" +
                    "|-----------|" + "\n" +
                    "| " + board[3] + " | " + board[4] + " | " + board[5] + " |" + "\n" +
                    "|-----------|" + "\n" +
                    "| " + board[6] + " | " + board[7] + " | " + board[8] + " |" + "\n" +
                    "|---|---|---|" + "\n");
            out2.flush();
        } catch (IOException e) {
            System.out.println("An unexpected error occurred on the server side.");
        }
    }

    static void gameStarter() {
        String winner = null;
        while (winner == null) {
            int numInput;
            if (turn.equals("X")) {
                try {
                    String turn = in.readLine();
                    numInput = Integer.parseInt(turn);
                    if (!(numInput > 0 && numInput <= 9)) {
                        out.write(
                                "Incorrect input, try again" + "\n");
                        out.flush();
                        out2.write("Incorrect input, try again" + "\n");
                        out2.flush();
                        continue;
                    }
                } catch (IOException e) {
                    System.out.println(
                            "Incorrect input, try again");
                    continue;
                }
                if (board[numInput - 1].equals(
                        String.valueOf(numInput))) {
                    board[numInput - 1] = turn;

                    if (turn.equals("X")) {
                        turn = "O";
                    } else {
                        turn = "X";
                    }
                    printBoard();
                    try {
                        out.write(turn + " turn. Select a cell for the next move." + "\n");
                        out.flush();
                        out2.write(turn + " turn. Select a cell for the next move." + "\n");
                        out2.flush();
                    } catch (IOException e) {
                        System.out.println("An unexpected error occurred on the server side.");
                    }
                    winner = checkWinner();
                } else {
                    try {
                        out.write("The cell is already occupied, choose another one" + "\n");
                        out.flush();
                        out2.write("The cell is already occupied, choose another one" + "\n");
                        out2.flush();
                    } catch (IOException e) {
                        System.out.println("An unexpected error occurred on the server side.");
                    }
                }
                if (winner != null) {
                    if (winner.equalsIgnoreCase("draw")) {
                        try {
                            out.write("A draw! Thanks for playing!" +
                                    "\n Restart the server and reconnect the two players to play again" + "\n");
                            out.flush();
                            out2.write("A draw! Thanks for playing!" +
                                    "\n Restart the server and reconnect the two players to play again" + "\n");
                            out2.flush();
                        } catch (IOException e) {
                            System.out.println("An unexpected error occurred on the server side.");
                        }
                    } else {
                        try {
                            out.write(winner + " won! Thanks for playing!" +
                                    "\n Restart the server and reconnect the two players to play again" + "\n");
                            out.flush();
                            out2.write(winner + " won! Thanks for playing!" +
                                    "\n Restart the server and reconnect the two players to play again" + "\n");
                            out2.flush();
                        } catch (IOException e) {
                            System.out.println("An unexpected error occurred on the server side.");
                        }
                    }
                }
            } else {
                try {
                    String turn = in2.readLine();
                    numInput = Integer.parseInt(turn);
                    if (!(numInput > 0 && numInput <= 9)) {
                        out.write("Incorrect input, try again" + "\n");
                        out.flush();
                        out2.write("Incorrect input, try again" + "\n");
                        out2.flush();
                        continue;
                    }
                } catch (IOException e) {
                    System.out.println(
                            "Incorrect input, try again");
                    continue;
                }
                if (board[numInput - 1].equals(
                        String.valueOf(numInput))) {
                    board[numInput - 1] = turn;

                    if (turn.equals("X")) {
                        turn = "O";
                    } else {
                        turn = "X";
                    }

                    printBoard();
                    try {
                        out.write(turn + " turn. Select a cell for the next move." + "\n");
                        out.flush();
                        out2.write(turn + " turn. Select a cell for the next move." + "\n");
                        out2.flush();
                    } catch (IOException e) {
                        System.out.println("An unexpected error occurred on the server side.");
                    }
                    winner = checkWinner();
                } else {
                    try {
                        out.write("The cell is already occupied, choose another one" + "\n");
                        out.flush();
                        out2.write("The cell is already occupied, choose another one" + "\n");
                        out2.flush();
                    } catch (IOException e) {
                        System.out.println("An unexpected error occurred on the server side.");
                    }
                }
                if (winner != null) {
                    if (winner.equalsIgnoreCase("draw")) {
                        try {
                            out.write("A draw! Thanks for playing!" +
                                    "\n Restart the server and reconnect the two players to play again" + "\n");
                            out.flush();
                            out2.write("A draw! Thanks for playing!" +
                                    "\n Restart the server and reconnect the two players to play again" + "\n");
                            out2.flush();
                        } catch (IOException e) {
                            System.out.println("An unexpected error occurred on the server side.");
                        }
                    } else {
                        try {
                            out.write(winner + " won! Thanks for playing!" +
                                    "\n Restart the server and reconnect the two players to play again" + "\n");
                            out.flush();
                            out2.write(winner + " won! Thanks for playing!" +
                                    "\n Restart the server and reconnect the two players to play again" + "\n");
                            out2.flush();
                        } catch (IOException e) {
                            System.out.println("An unexpected error occurred on the server side.");
                        }
                    }
                }
            }
        }
    }

    public Server(Socket socket, Socket socket2) throws IOException {
        Server.socket = socket;
        Server.socket2 = socket2;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        out2 = new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream()));
        board = new String[9];
        turn = "X";
        for (int a = 0; a < 9; a++) {
            board[a] = String.valueOf(a + 1);
        }
        try {
            out.write("Welcome! The game is about to start!" + "\n");
            out.flush();
            out2.write("Welcome! The game is about to start!" + "\n");
            out2.flush();
        } catch (IOException e) {
            System.out.println("An unexpected error occurred on the server side.");
        }
        printBoard();
        try {
            out.write("The first connected player goes first and plays with crosses. Please select the cell for the first move." + "\n");
            out.flush();
            out2.write("The first connected player goes first and plays with crosses. Please select the cell for the first move." + "\n");
            out2.flush();
        } catch (IOException e) {
            System.out.println("An unexpected error occurred on the server side.");
        }
        gameStarter();
        try {
            socket.close();
            in.close();
            out.close();
            System.out.println("Connection with player 1 is broken");
            socket2.close();
            in2.close();
            out2.close();
            System.out.println("Connection with player 2 is broken");
        } catch (IOException e) {
            System.out.println("An unexpected error occurred on the server side.");
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("The server is running successfully!");
        try {
            while (sockets.size() < 2) {
                Socket socket = server.accept();
                sockets.add(socket);
                Socket socket2 = server.accept();
                sockets.add(socket2);
                try {
                    new Server(socket, socket2);
                } catch (IOException e) {
                    socket.close();
                    socket2.close();
                }
            }
        } finally {
            server.close();
        }
    }
}