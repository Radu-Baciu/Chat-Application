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

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };
    }
}
