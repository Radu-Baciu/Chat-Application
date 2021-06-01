import io.grpc.Server;
import io.grpc.ServerBuilder;
import services.ChatServiceImpl;

import java.io.IOException;

public class ChatServer {
    public static void main(String []args){

        try {
            Server server = ServerBuilder.forPort(8999).addService(new ChatServiceImpl()).build();

            server.start();
            System.out.println("Server started at " + server.getPort());
            server.awaitTermination();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }
    }
}

