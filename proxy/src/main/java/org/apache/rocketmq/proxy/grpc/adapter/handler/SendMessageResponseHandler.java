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

package org.apache.rocketmq.proxy.grpc.adapter.handler;

import apache.rocketmq.v1.SendMessageRequest;
import apache.rocketmq.v1.SendMessageResponse;
import org.apache.rocketmq.proxy.grpc.adapter.InvocationContext;
import org.apache.rocketmq.proxy.grpc.common.ResponseBuilder;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;

public class SendMessageResponseHandler implements ResponseHandler<SendMessageRequest, SendMessageResponse> {
    private final String messageId;

    public SendMessageResponseHandler(String messageId) {
        this.messageId = messageId;
    }

    @Override public void handle(RemotingCommand responseCommand,
        InvocationContext<SendMessageRequest, SendMessageResponse> context) {
        // If responseCommand equals to null, then the response has been written to channel.
        // org.apache.rocketmq.broker.processor.SendMessageProcessor#handlePutMessageResult
        // org.apache.rocketmq.broker.processor.AbstractSendMessageProcessor#doResponse
        if (null != responseCommand) {
            SendMessageResponse response = ResponseBuilder.buildSendMessageResponse(responseCommand);
            response = response.toBuilder()
                .setMessageId(messageId)
                .build();
            context.getResponse().complete(response);
        }
    }
}