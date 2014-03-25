package com.codepump.servlet;

import org.eclipse.jetty.websocket.servlet.*;

import com.codepump.controller.ServerController;
import com.codepump.serializer.RecentItemSerializer;
import com.codepump.service.CodeService;
import com.codepump.socket.RecentSocket;
import com.codepump.tempobject.RecentItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Controller that listens for incoming websocket connections and handles
 * all the new clients. For each ws connection a RecentSocket object
 * is created. The sockets are later used for broadcasting events.
 *
 * This uses org.eclipse.jetty.websocket instead of javax.websocket because
 * javax.websocket does not give access to the ServletContext object
 * which is used to share data between servlets.
 *
 * You may need to enable websockets at heroku:
 * https://devcenter.heroku.com/articles/heroku-labs-websockets
 */
@WebServlet(value = "/feed")
public class RecentSocketController extends WebSocketServlet implements WebSocketCreator {

	private static final long serialVersionUID = -4811669486714140907L;
	private List<RecentSocket> sockets;
    private ServletContext context;
    private CodeService codeServ;
	private Gson gson;

    public void loadMostRecent() {
		RecentItem r = codeServ.getLastRecentItem();
		System.out.println(r.toString());
		// TODO clean up this hack
		List<RecentItem> list= new ArrayList<RecentItem>();
		list.add(r);
        for (RecentSocket socket : sockets) {
            try {
                socket.send(gson.toJson(list));
            } catch (IOException e) {
                System.out.println("failed to broadcast to " + socket);
            }
        }
    }

    public List<RecentSocket> getSockets() {
        return sockets;
    }

    public ServletContext getContext() {
        return context;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sockets = new CopyOnWriteArrayList<>(); // thread-safe impl
        context = config.getServletContext(); // shared between ALL servlets
        publish(this, context); // so that other servlets could find us
		gson = new GsonBuilder().registerTypeAdapter(RecentItem.class,
				new RecentItemSerializer()).create();
		codeServ = ServerController.codeServer;
		
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        return new RecentSocket(this); // socket instance created per client
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(this);
    }

    private static void publish(RecentSocketController controller, ServletContext context) {
        // see @WebListener and @WebFilter for details about servlet init
        context.setAttribute(RecentSocketController.class.getName(), controller);
    }

    public static RecentSocketController find(ServletContext context) {
        return (RecentSocketController) context.getAttribute(RecentSocketController.class.getName());
    }
    
    public void onStartLoad() {
		List<RecentItem> allContent = codeServ.getRecentItems();
		
        for (RecentSocket socket : sockets) {
            try {
                socket.send(gson.toJson(allContent));
            } catch (IOException e) {
                System.out.println("failed to broadcast to " + socket);
            }
        }    	
    }

}
