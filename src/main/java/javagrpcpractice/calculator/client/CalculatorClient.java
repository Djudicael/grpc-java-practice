package javagrpcpractice.calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
    public static void main(String[] args) {

        clientStream();
        //serverStream();
        //unary();

    }

    private static void serverStream() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

        Integer number = 56879;

        PrimeNumberDecompositionRequest request = PrimeNumberDecompositionRequest.newBuilder().setNumber(number).build();
        calculatorClient.primeNumberDecomposition(request).forEachRemaining(primeNumberDecompositionResponse -> {
            System.out.println(primeNumberDecompositionResponse.getPrimeFactor());
        });

        channel.shutdown();
    }

    private static void clientStream() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestObserver = asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                System.out.println("Receive a response from the server");
                System.out.println(value.getAverage());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending us data");
                latch.countDown();
            }
        });

        requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(1).build());
        requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(2).build());
        requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(3).build());
        requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(4).build());

        requestObserver.onCompleted();
        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        channel.shutdown();
    }


    private static void unary() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);


        SumRequest request = SumRequest.newBuilder().setFirstNumber(10).setSecondNumber(25).build();

        SumResponse sumResponse = calculatorClient.sum(request);

        System.out.println(request.getFirstNumber() + " + " + request.getSecondNumber() + " = " + sumResponse.getSumResult());

        channel.shutdown();
    }
}

