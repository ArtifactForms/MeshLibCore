package demos.voxels.client;

import java.io.*;
import java.net.*;

import demos.voxels.client.event.Event;
import demos.voxels.client.event.EventListener;
import demos.voxels.client.event.MessageSentEvent;

public class Client implements EventListener {
  private static final String SERVER_ADDRESS = "localhost";
  private static final int SERVER_PORT = 23456;

  private PrintWriter out;
  private BufferedReader in;
  private BufferedReader userInput;

  public Client() {
    userInput = new BufferedReader(new InputStreamReader(System.in));
  }

  public void connect() {
    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
      System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);

      // Start a new thread to listen for incoming server messages
      new Thread(
              new Runnable() {
                @Override
                public void run() {
                  listenForServerMessages();
                }
              })
          .start();

      // Main thread for sending messages to the server
      listenForUserInput();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void listenForServerMessages() {
    try {
      String serverMessage;
      while ((serverMessage = in.readLine()) != null) {
        // Print out any server message received
        System.out.println("Server says: " + serverMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void listenForUserInput() {
    try {
      String input;
      while (true) {
        System.out.print("Enter message: ");
        input = userInput.readLine();
        if (input.equalsIgnoreCase("exit")) break;

        // Trigger the MessageSentEvent when the user types a message
        MessageSentEvent event = new MessageSentEvent(input);
        onEvent(event); // Process the event and send the message
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onEvent(Event event) {
    if (event.getEventType().equals("MESSAGE_SENT")) {
      MessageSentEvent messageEvent = (MessageSentEvent) event;
      sendMessageToServer(messageEvent.getMessage());
    }
  }

  private void sendMessageToServer(String message) {
    if (out != null) {
      out.println(message);
      System.out.println("Sent to server: " + message);
    }
  }
}
