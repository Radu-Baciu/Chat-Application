import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Scanner;

public class ChatClient extends Application {
    private ObservableList<String> messages = FXCollections.observableArrayList();
    private ListView<String> messagesView = new ListView<>();
    private ObservableList<String> users = FXCollections.observableArrayList();
    private ListView<String> usersView = new ListView<>();
    Scanner sc = new Scanner(System.in);
    String userName = sc.next();
    private Label name = new Label(userName);
    private TextField message = new TextField();
    private Button send = new Button("Send");

    public void init(Stage primaryStage) {
        messagesView.setItems(messages);
        usersView.setItems(users);

        BorderPane sendMessage = new BorderPane();
        sendMessage.setLeft(name);
        sendMessage.setCenter(message);
        sendMessage.setRight(send);

        BorderPane root = new BorderPane();
        root.setLeft(usersView);
        root.setCenter(messagesView);
        root.setBottom(sendMessage);

        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root,800,600));

        primaryStage.show();
    }
    public void start(Stage primaryStage) {
        init(primaryStage);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",8999).usePlaintext().build();
        ChatServiceGrpc.ChatServiceStub chatService = ChatServiceGrpc.newStub(channel);

        StreamObserver<Chat.ChatMessage> observer = chatService.chat(new StreamObserver<Chat.ChatMessageFromServer>() {
            @Override
            public void onNext(Chat.ChatMessageFromServer value) {
                Platform.runLater(() -> {
                    messages.add(value.getMessage().getFrom() + ": " + value.getMessage().getMessage());
                    if (!users.contains(value.getMessage().getFrom()))
                        users.add(value.getMessage().getFrom());
                });
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        });
        send.setOnAction(e -> {
            observer.onNext(Chat.ChatMessage.newBuilder().setFrom(name.getText()).setMessage(message.getText()).build());
        });
    }
    public static void  Main(String[] args){
        launch(args);
    }
}
