package client.usecases.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ChatControllerTest {

  private SendChatMessageController sendController;
  private ChatController chat;

  @BeforeEach
  void setup() {
    sendController = Mockito.mock(SendChatMessageController.class);
    chat = new ChatController(sendController);
  }

  // =========================
  // Chat State
  // =========================

  @Test
  void openChat_setsOpenState() {

    chat.openChat();

    assertTrue(chat.isOpen());
  }

  @Test
  void closeChat_resetsState() {

    chat.openChat();
    chat.insert('H');
    chat.insert('i');

    chat.closeChat();

    assertFalse(chat.isOpen());
    assertEquals("", chat.getText());
    assertEquals(0, chat.getCursor());
  }

  // =========================
  // Insert
  // =========================

  @Test
  void insert_addsCharacter() {

    chat.insert('A');

    assertEquals("A", chat.getText());
    assertEquals(1, chat.getCursor());
  }

  @Test
  void insert_multipleCharacters() {

    chat.insert('H');
    chat.insert('i');

    assertEquals("Hi", chat.getText());
    assertEquals(2, chat.getCursor());
  }

  @Test
  void insert_respectsCursorPosition() {

    chat.insert('H');
    chat.insert('o');

    chat.moveLeft();
    chat.insert('i');

    assertEquals("Hio", chat.getText());
  }

  // =========================
  // Cursor Movement
  // =========================

  @Test
  void moveLeft_movesCursorLeft() {

    chat.insert('A');
    chat.insert('B');

    chat.moveLeft();

    assertEquals(1, chat.getCursor());
  }

  @Test
  void moveLeft_doesNotGoBelowZero() {

    chat.moveLeft();

    assertEquals(0, chat.getCursor());
  }

  @Test
  void moveRight_movesCursorRight() {

    chat.insert('A');

    chat.moveLeft();
    chat.moveRight();

    assertEquals(1, chat.getCursor());
  }

  // =========================
  // Backspace
  // =========================

  @Test
  void backspace_removesCharacterBeforeCursor() {

    chat.insert('A');
    chat.insert('B');

    chat.backspace();

    assertEquals("A", chat.getText());
    assertEquals(1, chat.getCursor());
  }

  @Test
  void backspace_doesNothingAtStart() {

    chat.insert('A');
    chat.moveLeft();

    chat.backspace();

    assertEquals("A", chat.getText());
    assertEquals(0, chat.getCursor());
  }

  // =========================
  // Delete
  // =========================

  @Test
  void delete_removesCharacterAtCursor() {

    chat.insert('A');
    chat.insert('B');
    chat.insert('C');

    chat.moveLeft(); // cursor before C
    chat.delete();

    assertEquals("AB", chat.getText());
  }

  @Test
  void delete_doesNothingAtEnd() {

    chat.insert('A');
    chat.insert('B');

    chat.delete();

    assertEquals("AB", chat.getText());
  }

  // =========================
  // Send
  // =========================

  @Test
  void send_sendsMessage() {

    chat.insert('H');
    chat.insert('i');

    chat.send();

    verify(sendController).onSendMessage("Hi");
  }

  @Test
  void send_doesNotSendEmptyMessage() {

    chat.insert(' ');
    chat.insert(' ');

    chat.send();

    verify(sendController, never()).onSendMessage(any());
  }

  @Test
  void send_resetsChatState() {

    chat.insert('H');

    chat.send();

    assertEquals("", chat.getText());
    assertEquals(0, chat.getCursor());
    assertFalse(chat.isOpen());
  }

}