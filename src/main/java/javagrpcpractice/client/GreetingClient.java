package javagrpcpractice.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("i am client");

        //unaryCall();
        //serverStreaming();
        clientStreaming();


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

    private static void clientStreaming() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        //Create an asynchronus client
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreetRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                //We get a response from the server
                System.out.println("Receive a response from the server");
                System.out.println(value.getResult());

                //on next will be called only once
            }

            @Override
            public void onError(Throwable t) {
                //we get an error

            }

            @Override
            public void onCompleted() {
                //the server is done sending us data

                System.out.println("Server has completed sending us something");
                latch.countDown();
                //on completed will be called right after on next
            }
        });

        Greeting greeting1 = Greeting.newBuilder().setFirstName("Judicaël").setLastName("DUBRAY").build();
        Greeting greeting2 = Greeting.newBuilder().setFirstName("Judicaël1").setLastName("DUBRAY2").build();

        Greeting greeting3 = Greeting.newBuilder().setFirstName("5444").setLastName("DUBRAY888").build();

        Greeting greeting4 = Greeting.newBuilder().setFirstName("Judica5555ël").setLastName("DUBRA445Y").build();

        System.out.println("send message 1");
        requestObserver.onNext(LongGreetRequest.newBuilder().setGreeting(greeting1).build());
        System.out.println("send message 2");
        requestObserver.onNext(LongGreetRequest.newBuilder().setGreeting(greeting2).build());
        System.out.println("send message 3");
        requestObserver.onNext(LongGreetRequest.newBuilder().setGreeting(greeting3).build());
        System.out.println("send message 4");
        requestObserver.onNext(LongGreetRequest.newBuilder().setGreeting(greeting4).build());

        //we tell the server that the client is done sending data
        requestObserver.onCompleted();
        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
