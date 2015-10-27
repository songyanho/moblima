package sg.edu.ntu.cz2002.moblima.models;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

public class Admin implements StandardData {
	protected int id;
	protected String username;
	protected String password;
	
	public Admin(int id, String username, String password){
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public boolean isUser(String username, String password){
		if(!this.username.equalsIgnoreCase(username)) return false;
		if(!this.username.equals(password)) return false;
		return true;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject o = new JSONObject();
		o.put("id", this.id);
		o.put("username", this.username);
		o.put("password", this.password);
		return o;
	}
	
	public static Admin fromJSONObject(JSONObject o) {
		return new Admin(Integer.parseInt(o.get("id").toString()), o.get("username").toString(), o.get("password").toString());
	}
	
	public static HashMap<String, JSONObject> toJSONObjects(HashMap<Integer, Admin> o){
		HashMap<String, JSONObject> a = new HashMap<String, JSONObject>();
		Set<Integer> s = o.keySet();
		for(Integer i: s){
			a.put(""+i, o.get(i).toJSONObject());
		}
		return a;
	}
	
	public static HashMap<Integer, Admin> fromJSONObjects(JSONObject o){
		HashMap<Integer, Admin> a = new HashMap<Integer, Admin>();
		Set<String> s = o.keySet();
		for(String i: s){
			JSONObject n = (JSONObject) o.get(i);
			Admin t = new Admin(Integer.parseInt(n.get("id").toString()), n.get("username").toString(), n.get("password").toString());
			a.put(t.getId(), t);
		}
		return a;
	}
}
