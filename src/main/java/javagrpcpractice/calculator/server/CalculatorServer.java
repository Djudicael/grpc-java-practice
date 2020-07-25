package javagrpcpractice.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50052)
                .addService(new CalculatorServiceImpl()).build();

        server.start();

        //handle properly the shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("receive shutdown request");
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));

        //for grpc to be blocking for the main thread
        server.awaitTermination();
    }
}
