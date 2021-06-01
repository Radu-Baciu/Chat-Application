import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatClient extends Application {
    private ListView<String> messages = new ListView<>();
    private TextField name = new TextField("Name");
    private TextField message = new TextField();
    private Button send = new Button("Send");

    public void init(Stage primaryStage) {
        BorderPane sendMessage = new BorderPane();
        sendMessage.setLeft(name);
        sendMessage.setCenter(message);
        sendMessage.setRight(send);

        BorderPane root = new BorderPane();
        root.setCenter(messages);
        root.setBottom(sendMessage);

        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root,800,600));
    }
    public void start(Stage primaryStage) {
        //TODO
    }
    public static void  Main(String[] args){
        //TODO
    }
}
