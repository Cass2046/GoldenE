package rpc;

import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import external.TicketMasterAPI;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
//		String userId = request.getParameter("user_id");
		
		
		// Term can be empty or null.
		String term = request.getParameter("term");
		String userId = request.getParameter("user_id");
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			List<Item> items = connection.searchItems(lat, lon, term);
			Set<String> favoriteItems = connection.getFavoriteItemIds(userId);

			JSONArray array = new JSONArray();
			for (Item item : items) {
				JSONObject obj = item.toJSONObject();
				obj.put("favorite", favoriteItems.contains(item.getItemId()));
				array.put(obj);
			}
			RpcHelper.writeJsonArray(response, array);
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

//		double lat = Double.parseDouble(request.getParameter("lat"));
//		double lon = Double.parseDouble(request.getParameter("lon"));
//		HttpSession session = request.getSession(false);
//		if (session == null) {
//			response.setStatus(403);
//			return;
//		}
//
//
//		// term can be empty
//		String term= request.getParameter("term");
//		TicketMasterAPI tmAPI = new TicketMasterAPI();
//		List<Item> items = tmAPI.search(lat, lon, term);
//		
//		JSONArray array = new JSONArray();
//		try {
//			for (Item item : items) {
//				JSONObject obj = item.toJSONObject();
//				array.put(obj);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		RpcHelper.writeJsonArray(response, array);
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);

	}

}