package javagrpcpractice.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);


        SumRequest request = SumRequest.newBuilder().setFirstNumber(10).setSecondNumber(25).build();

        SumResponse sumResponse = calculatorClient.sum(request);

        System.out.println(request.getFirstNumber() + " + "+ request.getSecondNumber()+" = "+ sumResponse.getSumResult());

        channel.shutdown();

    }
}
