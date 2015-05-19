/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sshd.common.io.mina;

import java.util.concurrent.ExecutorService;

import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.io.AbstractIoServiceFactory;
import org.apache.sshd.common.io.IoAcceptor;
import org.apache.sshd.common.io.IoConnector;
import org.apache.sshd.common.io.IoHandler;
import org.apache.sshd.common.util.threads.ThreadUtils;

/**
 */
public class MinaServiceFactory extends AbstractIoServiceFactory {

    private final IoProcessor<NioSession> ioProcessor;

    public MinaServiceFactory(FactoryManager factoryManager, ExecutorService service, boolean shutdownOnExit) {
        super(factoryManager,
              service == null ? ThreadUtils.newCachedThreadPool(factoryManager.toString() + "-mina") : service,
              service == null || shutdownOnExit);
        ioProcessor = new SimpleIoProcessorPool<>(NioProcessor.class, getExecutorService(), getNioWorkers(factoryManager), null);
    }

    @Override
    public IoConnector createConnector(IoHandler handler) {
        return new MinaConnector(getFactoryManager(), handler, ioProcessor);
    }

    @Override
    public IoAcceptor createAcceptor(IoHandler handler) {
        return new MinaAcceptor(getFactoryManager(), handler, ioProcessor);
    }
}
