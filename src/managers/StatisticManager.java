package managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import beans.DSStatistic;
import beans.DSStatistics;
import beans.DSUser;
import beans.DSUsers;

import utils.Constants;
import utils.TimeUtils;


public class StatisticManager extends DBManager {

	public static final File STATISTIC_FILE = new File(Constants.STATISTIC_FILE);
	
	public static boolean addStatisticIntoFile(DSStatistic dsStatistic){
		DSStatistics dsStatistics = getReportsFromFile();
		boolean addResult = false;
		if (dsStatistic != null) {
			addResult = dsStatistics.getStatistics().add(dsStatistic);
		}
		boolean emptyResult = emptyFile(STATISTIC_FILE);
		String json = gson.toJson(dsStatistics);
		boolean writeResult = writeToFile(STATISTIC_FILE, json);
		return (addResult && emptyResult && writeResult);
	}
	
	public static DSStatistics getReportsFromFile(){
		try {
			return gson.fromJson(new FileReader(STATISTIC_FILE), DSStatistics.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean writeStatstic(int writeType, String fileName, DSUser user){
		String time = TimeUtils.getCurrentTimeString();
		DSStatistic dsStatistic = new DSStatistic(user.getName(), writeType, fileName, time);
		return addStatisticIntoFile(dsStatistic);
	}
	
}
