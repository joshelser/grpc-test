import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;
import joshelser.proto.Service.SimpleRequest;
import joshelser.proto.Service.SimpleResponse;
import joshelser.proto.SimpleServiceGrpc;
import joshelser.proto.SimpleServiceGrpc.SimpleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Server {
  private static final Logger log = LoggerFactory.getLogger(Server.class);

  private static class ServiceImpl implements SimpleService {

    @Override
    public void simple(SimpleRequest request, StreamObserver<SimpleResponse> responseObserver) {
      final String msg = request.getMessage();
      log.info("Received message '{}'", msg);

      responseObserver.onValue(SimpleResponse.newBuilder().setMessage("From server: " + msg).build());
      responseObserver.onCompleted();
    }
    
  }

  public static void main(String[] args) throws Exception {
    ServerImpl server = NettyServerBuilder.forPort(8765).addService(
        SimpleServiceGrpc.bindService(new ServiceImpl())).build();

    server.start();

    server.awaitTerminated();
  }
  
}
