package javagrpcpractice.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        //extract the field we need
        Greeting greeting = request.getGreeting();
        String firstname = greeting.getFirstName();
        String lastname = greeting.getLastName();

        String result = "Hello "+ firstname;

        GreetResponse response = GreetResponse.newBuilder().setResult(result).build();

        //send the response
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();
    }
}
