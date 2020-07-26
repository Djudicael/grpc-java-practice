package javagrpcpractice.server;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        //extract the field we need
        Greeting greeting = request.getGreeting();
        String firstname = greeting.getFirstName();
        String lastname = greeting.getLastName();

        String result = "Hello " + firstname;

        GreetResponse response = GreetResponse.newBuilder().setResult(result).build();

        //send the response
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {

        Greeting greeting = request.getGreeting();
        String firstname = greeting.getFirstName();

        try {
            for (int i = 0; i < 10; i++) {
                String result = "Hello " + firstname + ", response number" + i;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder().setResult(result).build();
                //send the response
                responseObserver.onNext(response);

                Thread.sleep(1000L);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {

        StreamObserver<LongGreetRequest> streamObserver = new StreamObserver<LongGreetRequest>() {

            String result="";

            @Override
            public void onNext(LongGreetRequest value) {
                //client sends a message

                result += "Hello " + value.getGreeting().getFirstName() + " !";
            }

            @Override
            public void onError(Throwable t) {
                //client send a error

            }

            @Override
            public void onCompleted() {
                //client is done

                responseObserver.onNext(LongGreetResponse.newBuilder().setResult(result).build());

                responseObserver.onCompleted();

                //this this when we want to return a response(responseObserver)
            }
        };
        return streamObserver;
    }
}
