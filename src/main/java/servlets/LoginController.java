package servlets;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import data.User;
import datastore.UserStore;
import datastore.UserDataProvider;

@WebServlet(value = "/login")
public class LoginController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson; 
	private UserDataProvider datastore;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new GsonBuilder().create();
		datastore = new UserStore();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			
			/*
			 * for Tõnis to sort out, i have better shit to do to read how this works :D
			 * siia kirjutad koodi mis võtab vastu useri ja passwordi ning kontrollib kas see on õige
			 */
			
			
			/*
			 * genereerib session ID, see on see mille sa pead kasutajale tagasi saatma + 
			 * sa pead selle salvestama kuhugile serveri mällu, ning iga kord kui generateid siis 
			 * pead ka kontrollima et teist sellist SID-d ei ole olemas juba, lisaks pead meeles pidama milline
			 * user on seotud selle SID-ga
			 */
			String SID = generateSID();
			
			
			// praegu veel timeout counterit ei oel vist vaja :D
			
			
			
		} catch (JsonParseException ex) {
			System.err.println(ex);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		}
	}
	
	private String generateSID(){
		String temp = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		char[] lst = temp.toCharArray();
		
		String SID = "";
		Random rand = new Random();
		for(int i = 0; i < 32; i++){
			int nr = rand.nextInt(lst.length);
			SID += lst[nr];
		}
		
		return SID;
	}

}
