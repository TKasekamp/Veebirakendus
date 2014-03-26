package com.codepump.socket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.codepump.servlet.RecentSocketController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@WebSocket
public class RecentSocket {

	private final RecentSocketController controller;
	private Session session;

	public RecentSocket(RecentSocketController controller) {
		this.controller = controller;
	}

	public void send(String message) throws IOException {
		if (session.isOpen()) {
			session.getRemote().sendString(message, null);
		}
	}

	@OnWebSocketConnect
	public void onOpen(Session session) {
		this.session = session;
		controller.getSockets().add(this);
		session.setIdleTimeout(TimeUnit.HOURS.toMillis(10));
		try {
			send(controller.onStartLoad());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnWebSocketClose
	public void onClose(int status, String message) {
		controller.getSockets().remove(this);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) throws IOException {
		System.out.println("received on websocket: " + message);
	}

}
