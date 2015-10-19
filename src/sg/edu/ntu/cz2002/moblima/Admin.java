package sg.edu.ntu.cz2002.moblima;

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
}
