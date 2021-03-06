package managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import utils.Constants;
import beans.DSUser;
import beans.DSUsers;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class UserManager extends DBManager {

	/**
	 * 
	 * The manager of "database", in this project i use json files save user and
	 * file's data
	 * 
	 */

	public static final File USERS_FILE = new File(Constants.USERS_FILE);

	// judge whether user's name and password is authorized
	public static boolean isUserAuthorized(DSUser user) {
		DSUsers dsUsers = getUsersFromFile();
		return (existedUser(dsUsers, user) != null && passwordCorrect(dsUsers,
				user));
	}

	// remove the user data from the file
	// get new data -> blank file contents -> write new data contents
	public static boolean removeUserFromFile(DSUser user) {
		DSUsers dsUsers = getUsersFromFile();
		boolean deleteResult = false;
		if (existedUser(dsUsers, user) != null) {
			removeUser(dsUsers, user);
			deleteResult = true;
		}
		boolean emptyResult = emptyFile(USERS_FILE);
		String json = gson.toJson(dsUsers);
		boolean writeResult = writeToFile(USERS_FILE, json);

		return (deleteResult && emptyResult && writeResult);
	}

	// add the user data into the file
	// get new data -> blank file contents -> write new data contents
	public static boolean addUserIntoFile(DSUser user) {
		DSUsers dsUsers = getUsersFromFile();
		boolean addResult = false;
		if (dsUsers != null) {
			addResult = dsUsers.getUsers().add(user);
		}
		boolean emptyResult = emptyFile(USERS_FILE);
		String json = gson.toJson(dsUsers);
		boolean writeResult = writeToFile(USERS_FILE, json);

		return (addResult && emptyResult && writeResult);
	}

	// get all the user content with json format string
	public static DSUsers getUsersFromFile() {
		try {
			return gson.fromJson(new FileReader(USERS_FILE), DSUsers.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// return the use with the same name as user in the dsUsers's list
	public static DSUser existedUser(DSUsers dsUsers, DSUser user) {
		List<DSUser> existedUsers = dsUsers.getUsers();
		String name = user.getName();
		for (DSUser dsUser : existedUsers) {
			if (dsUser.getName().equals(name)) {
				return dsUser;
			}
		}
		return null;
	}

	// remove the user with the same name as user in the dsUsers's list
	public static void removeUser(DSUsers dsUsers, DSUser user) {
		List<DSUser> existedUsers = dsUsers.getUsers();
		String name = user.getName();
		for (DSUser dsUser : existedUsers) {
			if (dsUser.getName().equals(name)) {
				existedUsers.remove(dsUser);
				return;
			}
		}
	}

	// judge the user match is correct password in the dsUsers's list
	public static boolean passwordCorrect(DSUsers dsUsers, DSUser user) {
		DSUser existedUser = existedUser(dsUsers, user);
		return existedUser.getPassword().equals(user.getPassword());
	}

}
