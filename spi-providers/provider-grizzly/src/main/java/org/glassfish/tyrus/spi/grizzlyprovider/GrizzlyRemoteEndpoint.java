/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.tyrus.spi.grizzlyprovider;

import org.glassfish.grizzly.websockets.WebSocket;

import javax.net.websocket.EncodeException;
import javax.net.websocket.RemoteEndpoint;
import javax.net.websocket.SendHandler;
import javax.net.websocket.SendResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 *
 * @author dannycoward
 */
public class GrizzlyRemoteEndpoint implements RemoteEndpoint {
    private WebSocket socket;
    private static ConcurrentHashMap<WebSocket, GrizzlyRemoteEndpoint> sockets = new ConcurrentHashMap<WebSocket, GrizzlyRemoteEndpoint>();

    public GrizzlyRemoteEndpoint(WebSocket socket) {
        this.socket = socket;
    }

    public static GrizzlyRemoteEndpoint get(WebSocket socket) {
        GrizzlyRemoteEndpoint s = sockets.get(socket);
        if (s == null) {
            s = new GrizzlyRemoteEndpoint(socket);
            sockets.put(socket, s);
        }
        return s;
    }

    public static void remove(WebSocket socket) {
        sockets.remove(socket);
    }

    public boolean isConnected() {
        return this.socket.isConnected();
    }

    public String getUri() {
        if(socket instanceof GrizzlySocket){
            return ((GrizzlySocket)socket).getRequest().getRequestURI();
        }
        return null;
    }

    @Override
    public void sendString(String text) throws IOException {
        this.socket.send(text);
    }

    @Override
    public void sendBytes(ByteBuffer byteBuffer) throws IOException {

    }

    @Override
    public void sendPartialString(String fragment, boolean isLast) throws IOException {
        //System.out.println("GrizzlyRemoteEndpoint.sendPartialString");
        socket.stream(isLast, fragment);
        //System.out.println("GrizzlyRemoteEndpoint.sendPartialString done ");
    }

    @Override
    public void sendPartialBytes(ByteBuffer byteBuffer, boolean b) throws IOException {

    }

    @Override
    public OutputStream getSendStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writer getSendWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendObject(Object o) throws IOException, EncodeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future<SendResult> sendString(String text, SendHandler completion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future<SendResult> sendBytes(ByteBuffer byteBuffer, SendHandler sendHandler) {
        return null;
    }

    @Override
    public Future<SendResult> sendObject(Object o, SendHandler handler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendPing(ByteBuffer byteBuffer) {

    }

    @Override
    public void sendPong(ByteBuffer byteBuffer) {

    }
}
