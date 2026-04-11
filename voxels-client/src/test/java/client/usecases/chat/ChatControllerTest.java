package client.usecases.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
  void openChatSetsOpenState() {

    chat.openChat();

    assertTrue(chat.isOpen());
  }

  @Test
  void closeChatResetsState() {

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
  void insertAddsCharacter() {

    chat.insert('A');

    assertEquals("A", chat.getText());
    assertEquals(1, chat.getCursor());
  }

  @Test
  void insertMultipleCharacters() {

    chat.insert('H');
    chat.insert('i');

    assertEquals("Hi", chat.getText());
    assertEquals(2, chat.getCursor());
  }

  @Test
  void insertRespectsCursorPosition() {

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
  void moveLeftMovesCursorLeft() {

    chat.insert('A');
    chat.insert('B');

    chat.moveLeft();

    assertEquals(1, chat.getCursor());
  }

  @Test
  void moveLeftDoesNotGoBelowZero() {

    chat.moveLeft();

    assertEquals(0, chat.getCursor());
  }

  @Test
  void moveRightMovesCursorRight() {

    chat.insert('A');

    chat.moveLeft();
    chat.moveRight();

    assertEquals(1, chat.getCursor());
  }

  // =========================
  // Backspace
  // =========================

  @Test
  void backspaceRemovesCharacterBeforeCursor() {

    chat.insert('A');
    chat.insert('B');

    chat.backspace();

    assertEquals("A", chat.getText());
    assertEquals(1, chat.getCursor());
  }

  @Test
  void backspaceDoesNothingAtStart() {

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
  void deleteRemovesCharacterAtCursor() {

    chat.insert('A');
    chat.insert('B');
    chat.insert('C');

    chat.moveLeft(); // cursor before C
    chat.delete();

    assertEquals("AB", chat.getText());
  }

  @Test
  void deleteDoesNothingAtEnd() {

    chat.insert('A');
    chat.insert('B');

    chat.delete();

    assertEquals("AB", chat.getText());
  }

  // =========================
  // Send
  // =========================

  @Test
  void sendSendsMessage() {

    chat.insert('H');
    chat.insert('i');

    chat.send();

    verify(sendController).onSendMessage("Hi");
  }

  @Test
  void sendDoesNotSendEmptyMessage() {

    chat.insert(' ');
    chat.insert(' ');

    chat.send();

    verify(sendController, never()).onSendMessage(any());
  }

  @Test
  void sendResetsChatState() {

    chat.insert('H');

    chat.send();

    assertEquals("", chat.getText());
    assertEquals(0, chat.getCursor());
    assertFalse(chat.isOpen());
  }
}
