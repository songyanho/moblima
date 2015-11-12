package sg.edu.ntu.cz2002.moblima;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sg.edu.ntu.cz2002.moblima.dao.AdminDao;
import sg.edu.ntu.cz2002.moblima.models.Admin;

public class Data {

	protected int adminId;

	/**
	 * Get current admin
	 * @return Logged Admin
	 */
	public Admin getAdmin(){
		return AdminDao.findById(adminId);
	}

	public int getAdminId() {
		return adminId;
	}

	/**
	 * Set only after admin has successfully logged in
	 * @param adminId Unique id of admin in database record
	 */
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

}