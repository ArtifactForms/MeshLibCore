package demos.voxels.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

  private static final int PORT = 23456;

  private final List<ClientHandler> clients = new ArrayList<>();

  public static void main(String[] args) {
    new Server().start();
  }

  public void start() {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Server started on port " + PORT);

      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("Client connected: " + socket);

        ClientHandler handler = new ClientHandler(socket);
        clients.add(handler);
        new Thread(handler).start();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private class ClientHandler implements Runnable {

    private final Socket socket;

    private PrintWriter out;

    private BufferedReader in;

    public ClientHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String message;

        while ((message = in.readLine()) != null) {
          System.out.println("Received: " + message);
          broadcast(message);
        }

      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        clients.remove(this);
        try {
          socket.close();
        } catch (IOException ignored) {
        }
      }
    }

    public void send(String message) {
      out.println(message);
    }
  }

  private void broadcast(String message) {
    for (ClientHandler client : clients) {
      client.send(message);
    }
  }
}
