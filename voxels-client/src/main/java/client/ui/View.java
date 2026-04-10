package client.ui;

import client.ui.actionbar.ActionBarView;
import client.ui.hotbar.HotbarView;
import client.ui.title.TitleView;
import client.usecases.chat.ChatView;

public class View implements ClientView {

  private ActionBarView actionBarView;

  private HotbarView hotbarView;

  private ChatView chatView;

  private InventoryView inventoryView;

  private TitleView titleView;

  @Override
  public ActionBarView getActionBarView() {
    return actionBarView;
  }

  @Override
  public void setActionBarView(ActionBarView actionBarView) {
    this.actionBarView = actionBarView;
  }

  @Override
  public HotbarView getHotbarView() {
    return hotbarView;
  }

  @Override
  public void setHotbarView(HotbarView hotbarView) {
    this.hotbarView = hotbarView;
  }

  @Override
  public ChatView getChatView() {
    return chatView;
  }

  @Override
  public void setChatView(ChatView chatView) {
    this.chatView = chatView;
  }

  @Override
  public void setInventoryView(InventoryView inventoryView) {
    this.inventoryView = inventoryView;
  }

  @Override
  public InventoryView getInventoryView() {
    return inventoryView;
  }

  @Override
  public TitleView getTitleView() {
    return titleView;
  }

  @Override
  public void setTitleView(TitleView titleView) {
    this.titleView = titleView;
  }
}
