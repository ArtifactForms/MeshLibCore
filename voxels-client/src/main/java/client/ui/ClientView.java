package client.ui;

import client.ui.actionbar.ActionBarView;
import client.ui.hotbar.HotbarView;
import client.usecases.chat.ChatView;

public interface ClientView {

  ActionBarView getActionBarView();

  void setActionBarView(ActionBarView actionBarView);

  HotbarView getHotbarView();

  void setHotbarView(HotbarView hotbarView);

  ChatView getChatView();

  void setChatView(ChatView chatView);
  
  void setInventoryView(InventoryView inventoryView);
  
  InventoryView getInventoryView();
}
