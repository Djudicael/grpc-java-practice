package javagrpcpractice.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("i am client");

        //unaryCall();
        serverStreaming();


    }

    private static void serverStreaming() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

        System.out.println("Creating stub");

        //create a greet service client(blocking synchronus)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        //create a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder().setFirstName("Judicaël").setLastName("DUBRAY").build();
        //preparing the request
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder().setGreeting(greeting).build();

        //we stream the response ( in the blocking manner)
        greetClient.greetManyTimes(greetManyTimesRequest).forEachRemaining(response -> {
            System.out.println(response.getResult());
        });

        System.out.println("shutting down channel");
        channel.shutdown();
    }


    private static void unaryCall() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

        System.out.println("Creating stub");

        //create a greet service client(blocking synchronus)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        //create a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder().setFirstName("Judicaël").setLastName("DUBRAY").build();
        //same for a greetrequest
        GreetRequest greetRequest = GreetRequest.newBuilder().setGreeting(greeting).build();

        //call the RPC and get back the repsonse
        GreetResponse greetResponse = greetClient.greet(greetRequest);

        System.out.println(greetResponse.getResult());
        // DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

        //in async client
//        DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);

        //do something

        System.out.println("shutting down channel");
        channel.shutdown();
    }
}
