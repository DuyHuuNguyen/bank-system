package com.example.server.wallet;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class WalletServiceGrpc {

  private WalletServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.example.wallet.WalletService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.server.wallet.WalletRequest,
      com.example.server.wallet.WalletResponse> getFindWalletByRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findWalletByRequest",
      requestType = com.example.server.wallet.WalletRequest.class,
      responseType = com.example.server.wallet.WalletResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.server.wallet.WalletRequest,
      com.example.server.wallet.WalletResponse> getFindWalletByRequestMethod() {
    io.grpc.MethodDescriptor<com.example.server.wallet.WalletRequest, com.example.server.wallet.WalletResponse> getFindWalletByRequestMethod;
    if ((getFindWalletByRequestMethod = WalletServiceGrpc.getFindWalletByRequestMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getFindWalletByRequestMethod = WalletServiceGrpc.getFindWalletByRequestMethod) == null) {
          WalletServiceGrpc.getFindWalletByRequestMethod = getFindWalletByRequestMethod =
              io.grpc.MethodDescriptor.<com.example.server.wallet.WalletRequest, com.example.server.wallet.WalletResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findWalletByRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.server.wallet.WalletRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.server.wallet.WalletResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("findWalletByRequest"))
              .build();
        }
      }
    }
    return getFindWalletByRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.server.wallet.WalletOwnerRequest,
      com.example.server.wallet.WalletResponse> getFindWalletOwnerByRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findWalletOwnerByRequest",
      requestType = com.example.server.wallet.WalletOwnerRequest.class,
      responseType = com.example.server.wallet.WalletResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.server.wallet.WalletOwnerRequest,
      com.example.server.wallet.WalletResponse> getFindWalletOwnerByRequestMethod() {
    io.grpc.MethodDescriptor<com.example.server.wallet.WalletOwnerRequest, com.example.server.wallet.WalletResponse> getFindWalletOwnerByRequestMethod;
    if ((getFindWalletOwnerByRequestMethod = WalletServiceGrpc.getFindWalletOwnerByRequestMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getFindWalletOwnerByRequestMethod = WalletServiceGrpc.getFindWalletOwnerByRequestMethod) == null) {
          WalletServiceGrpc.getFindWalletOwnerByRequestMethod = getFindWalletOwnerByRequestMethod =
              io.grpc.MethodDescriptor.<com.example.server.wallet.WalletOwnerRequest, com.example.server.wallet.WalletResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findWalletOwnerByRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.server.wallet.WalletOwnerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.server.wallet.WalletResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("findWalletOwnerByRequest"))
              .build();
        }
      }
    }
    return getFindWalletOwnerByRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.server.wallet.WalletProfileRequest,
      com.example.server.wallet.WalletProfileResponse> getFindWalletProfileByRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findWalletProfileByRequest",
      requestType = com.example.server.wallet.WalletProfileRequest.class,
      responseType = com.example.server.wallet.WalletProfileResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.server.wallet.WalletProfileRequest,
      com.example.server.wallet.WalletProfileResponse> getFindWalletProfileByRequestMethod() {
    io.grpc.MethodDescriptor<com.example.server.wallet.WalletProfileRequest, com.example.server.wallet.WalletProfileResponse> getFindWalletProfileByRequestMethod;
    if ((getFindWalletProfileByRequestMethod = WalletServiceGrpc.getFindWalletProfileByRequestMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getFindWalletProfileByRequestMethod = WalletServiceGrpc.getFindWalletProfileByRequestMethod) == null) {
          WalletServiceGrpc.getFindWalletProfileByRequestMethod = getFindWalletProfileByRequestMethod =
              io.grpc.MethodDescriptor.<com.example.server.wallet.WalletProfileRequest, com.example.server.wallet.WalletProfileResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findWalletProfileByRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.server.wallet.WalletProfileRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.server.wallet.WalletProfileResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("findWalletProfileByRequest"))
              .build();
        }
      }
    }
    return getFindWalletProfileByRequestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WalletServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletServiceStub>() {
        @java.lang.Override
        public WalletServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletServiceStub(channel, callOptions);
        }
      };
    return WalletServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static WalletServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletServiceBlockingV2Stub>() {
        @java.lang.Override
        public WalletServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return WalletServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WalletServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletServiceBlockingStub>() {
        @java.lang.Override
        public WalletServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletServiceBlockingStub(channel, callOptions);
        }
      };
    return WalletServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WalletServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletServiceFutureStub>() {
        @java.lang.Override
        public WalletServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletServiceFutureStub(channel, callOptions);
        }
      };
    return WalletServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void findWalletByRequest(com.example.server.wallet.WalletRequest request,
        io.grpc.stub.StreamObserver<com.example.server.wallet.WalletResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindWalletByRequestMethod(), responseObserver);
    }

    /**
     */
    default void findWalletOwnerByRequest(com.example.server.wallet.WalletOwnerRequest request,
        io.grpc.stub.StreamObserver<com.example.server.wallet.WalletResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindWalletOwnerByRequestMethod(), responseObserver);
    }

    /**
     */
    default void findWalletProfileByRequest(com.example.server.wallet.WalletProfileRequest request,
        io.grpc.stub.StreamObserver<com.example.server.wallet.WalletProfileResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindWalletProfileByRequestMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service WalletService.
   */
  public static abstract class WalletServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return WalletServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service WalletService.
   */
  public static final class WalletServiceStub
      extends io.grpc.stub.AbstractAsyncStub<WalletServiceStub> {
    private WalletServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletServiceStub(channel, callOptions);
    }

    /**
     */
    public void findWalletByRequest(com.example.server.wallet.WalletRequest request,
        io.grpc.stub.StreamObserver<com.example.server.wallet.WalletResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindWalletByRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void findWalletOwnerByRequest(com.example.server.wallet.WalletOwnerRequest request,
        io.grpc.stub.StreamObserver<com.example.server.wallet.WalletResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindWalletOwnerByRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void findWalletProfileByRequest(com.example.server.wallet.WalletProfileRequest request,
        io.grpc.stub.StreamObserver<com.example.server.wallet.WalletProfileResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindWalletProfileByRequestMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service WalletService.
   */
  public static final class WalletServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<WalletServiceBlockingV2Stub> {
    private WalletServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.example.server.wallet.WalletResponse findWalletByRequest(com.example.server.wallet.WalletRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindWalletByRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.server.wallet.WalletResponse findWalletOwnerByRequest(com.example.server.wallet.WalletOwnerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindWalletOwnerByRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.server.wallet.WalletProfileResponse findWalletProfileByRequest(com.example.server.wallet.WalletProfileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindWalletProfileByRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service WalletService.
   */
  public static final class WalletServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<WalletServiceBlockingStub> {
    private WalletServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.server.wallet.WalletResponse findWalletByRequest(com.example.server.wallet.WalletRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindWalletByRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.server.wallet.WalletResponse findWalletOwnerByRequest(com.example.server.wallet.WalletOwnerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindWalletOwnerByRequestMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.example.server.wallet.WalletProfileResponse findWalletProfileByRequest(com.example.server.wallet.WalletProfileRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindWalletProfileByRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service WalletService.
   */
  public static final class WalletServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<WalletServiceFutureStub> {
    private WalletServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.server.wallet.WalletResponse> findWalletByRequest(
        com.example.server.wallet.WalletRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindWalletByRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.server.wallet.WalletResponse> findWalletOwnerByRequest(
        com.example.server.wallet.WalletOwnerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindWalletOwnerByRequestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.server.wallet.WalletProfileResponse> findWalletProfileByRequest(
        com.example.server.wallet.WalletProfileRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindWalletProfileByRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FIND_WALLET_BY_REQUEST = 0;
  private static final int METHODID_FIND_WALLET_OWNER_BY_REQUEST = 1;
  private static final int METHODID_FIND_WALLET_PROFILE_BY_REQUEST = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FIND_WALLET_BY_REQUEST:
          serviceImpl.findWalletByRequest((com.example.server.wallet.WalletRequest) request,
              (io.grpc.stub.StreamObserver<com.example.server.wallet.WalletResponse>) responseObserver);
          break;
        case METHODID_FIND_WALLET_OWNER_BY_REQUEST:
          serviceImpl.findWalletOwnerByRequest((com.example.server.wallet.WalletOwnerRequest) request,
              (io.grpc.stub.StreamObserver<com.example.server.wallet.WalletResponse>) responseObserver);
          break;
        case METHODID_FIND_WALLET_PROFILE_BY_REQUEST:
          serviceImpl.findWalletProfileByRequest((com.example.server.wallet.WalletProfileRequest) request,
              (io.grpc.stub.StreamObserver<com.example.server.wallet.WalletProfileResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getFindWalletByRequestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.server.wallet.WalletRequest,
              com.example.server.wallet.WalletResponse>(
                service, METHODID_FIND_WALLET_BY_REQUEST)))
        .addMethod(
          getFindWalletOwnerByRequestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.server.wallet.WalletOwnerRequest,
              com.example.server.wallet.WalletResponse>(
                service, METHODID_FIND_WALLET_OWNER_BY_REQUEST)))
        .addMethod(
          getFindWalletProfileByRequestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.server.wallet.WalletProfileRequest,
              com.example.server.wallet.WalletProfileResponse>(
                service, METHODID_FIND_WALLET_PROFILE_BY_REQUEST)))
        .build();
  }

  private static abstract class WalletServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WalletServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.server.wallet.WalletProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WalletService");
    }
  }

  private static final class WalletServiceFileDescriptorSupplier
      extends WalletServiceBaseDescriptorSupplier {
    WalletServiceFileDescriptorSupplier() {}
  }

  private static final class WalletServiceMethodDescriptorSupplier
      extends WalletServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    WalletServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (WalletServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WalletServiceFileDescriptorSupplier())
              .addMethod(getFindWalletByRequestMethod())
              .addMethod(getFindWalletOwnerByRequestMethod())
              .addMethod(getFindWalletProfileByRequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
