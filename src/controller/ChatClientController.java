package controller;
import view.ChatClientUI;


public class ChatClientController
{
    ChatClientUI ui;
    
    public ChatClientController(){
        
        ui = new ChatClientUI();
        ui.showUI();
    }
    
    public static void main(String[] args)
    {
        ChatClientController controller = new ChatClientController();
    }
}
