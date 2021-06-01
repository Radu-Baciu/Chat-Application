package services;

import io.grpc.stub.StreamObserver;
import proto.Chat;
import proto.ChatServiceGrpc;

import java.util.HashSet;

public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
    private HashSet<StreamObserver<Chat.ChatMessageFromServer>> observers = new HashSet<>();

    @Override
    public StreamObserver<Chat.ChatMessage> chat(StreamObserver<Chat.ChatMessageFromServer> responseObserver) {
        observers.add(responseObserver);
        return new StreamObserver<Chat.ChatMessage>() {
            @Override
            public void onNext(Chat.ChatMessage value) {
                System.out.println(value);
                for (StreamObserver<Chat.ChatMessageFromServer> observer : observers) {
                    observer.onNext(Chat.ChatMessageFromServer.newBuilder().setMessage(value).build());
                }

            }

            @Override
            public void onError(Throwable t) {
                observers.remove(responseObserver);
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
            }
        };
    }
}
