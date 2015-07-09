/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package joshelser;

import java.util.concurrent.TimeUnit;

import io.grpc.ChannelImpl;
import io.grpc.transport.netty.NegotiationType;
import io.grpc.transport.netty.NettyChannelBuilder;
import joshelser.proto.Service.SimpleRequest;
import joshelser.proto.Service.SimpleResponse;
import joshelser.proto.SimpleServiceGrpc;
import joshelser.proto.SimpleServiceGrpc.SimpleServiceBlockingStub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
  private static final Logger log = LoggerFactory.getLogger(Client.class);
  
  public static void main(String[] args) {
    ChannelImpl channel = NettyChannelBuilder.forAddress("localhost", 8765).negotiationType(NegotiationType.PLAINTEXT).build();
    SimpleServiceBlockingStub stub = SimpleServiceGrpc.newBlockingStub(channel);

    SimpleResponse response = stub.simple(SimpleRequest.newBuilder().setMessage("hello!").build());

    log.info("Got response '{}'", response.getMessage());

    channel.shutdown();
    try {
      channel.awaitTerminated(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.error("Failed to shutdown channel");
    }
  }
  
}
