package sg.edu.ntu.cz2002.moblima;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Data {
	
	protected int panel;
	protected int adminId;
	
	public Data() throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser();
		URL url = getClass().getResource("Data.json");
		Object obj = parser.parse(new FileReader(url.toString()));
        JSONObject jsonObject = (JSONObject) obj;
	}
	
	public boolean saveData(){
		
		return true;
	}

	public int getPanel() {
		return panel;
	}

	public void setPanel(int panel) {
		this.panel = panel;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	
	
	
}
