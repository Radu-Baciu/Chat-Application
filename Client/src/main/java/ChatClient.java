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

    private TextField message = new TextField();
    private Button send = new Button("Send");
    private Button exit = new Button("Exit");

    public void init(Stage primaryStage) {
        messagesView.setItems(messages);
        usersView.setItems(users);

        BorderPane sendMessage = new BorderPane();
        sendMessage.setLeft(exit);
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
        System.out.println("Type your username: ");
        Scanner sc = new Scanner(System.in);
        String userName = sc.next();
        init(primaryStage);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",8999).usePlaintext().build();
        ChatServiceGrpc.ChatServiceStub chatService = ChatServiceGrpc.newStub(channel);

        StreamObserver<Chat.ChatMessage> observer = chatService.chat(new StreamObserver<Chat.ChatMessageFromServer>() {
            @Override
            public void onNext(Chat.ChatMessageFromServer value) {
                Platform.runLater(() -> {
                    if (value.getMessage().getMessage().equals("HAS CONNECTED") || value.getMessage().getMessage().equals("HAS DISCONNECTED"))
                        messages.add(value.getMessage().getFrom() + " " + value.getMessage().getMessage());
                    else
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
        observer.onNext(Chat.ChatMessage.newBuilder().setFrom(userName).setMessage("HAS CONNECTED").build());
        send.setOnAction(e -> {
            observer.onNext(Chat.ChatMessage.newBuilder().setFrom(userName).setMessage(message.getText()).build());
        });
        exit.setOnAction(e -> {
            observer.onNext(Chat.ChatMessage.newBuilder().setFrom(userName).setMessage("HAS DISCONNECTED").build());
            primaryStage.close();
        });
    }
    public static void  Main(String[] args){
        launch(args);
    }
}
