package com.ddpsc.phenofront;


import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.snapshot.SnapshotDao;

/**
 * Handles requests for tests
 */
@Controller
public class TestController {
	
	@Autowired  
	SnapshotDao sd;  
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/oldhomelol", method = RequestMethod.GET)
	public String homeAction(Locale locale, Model model) {
		LocalTime local = new LocalTime(); //just time
		DateTime date = new DateTime(); //gets datetime for NOW
		Timestamp ts = new Timestamp(date.getMillis());
		System.out.println(ts);
	//	String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", date.toString("EEEE, MMMM dd, yyyy H:mm:ss aa") );
		//name of the jsp
		return "home";
	}
	
	@RequestMapping(value = "/after", method = RequestMethod.GET)
	public String afterDateTestAction(Locale locale, Model model) {
		if (sd.equals(null)){
			System.out.println("fukin null mang");
		}
	//	Calendar c = Calendar.getInstance();
	//	c.set(2013, 9, 1);
	//	Timestamp ts = new Timestamp(c.getTimeInMillis());
		//Snapshot result = sd.findWithTileBySnapshotId(32035);
	//	List<Snapshot> results = sd.findSnapshotAfterTimestamp(ts);
		Random rand = new Random(System.currentTimeMillis());
		model.addAttribute("random", rand.nextInt());
	//	model.addAttribute("test_result", results);
		return "info";
	}
	
	/**
	 * Gets all snapshots including images after 2013-9-25
	 * Calendar starts at zero for the month only. Java...
	 * 
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/afterimage", method = RequestMethod.GET)
	public String afterDateImageTestAction(Locale locale, Model model) {
		if (sd.equals(null)){
			System.out.println("fukin null mang");
		}
	//	GregorianCalendar cal = new GregorianCalendar(2013, 8, 25); //java has downs
	//	long millis = cal.getTimeInMillis();
	//	System.out.println(millis);
	//	Timestamp ts = new Timestamp(millis);
		//Snapshot result = sd.findWithTileBySnapshotId(32035);
	//	List<Snapshot> results = sd.findWithTileAfterTimestamp(ts);
		Random rand = new Random(System.currentTimeMillis());
		model.addAttribute("random", rand.nextInt());
	//	model.addAttribute("test_result", results);
		return "info";
	}
	
	@RequestMapping(value="/betweenimage", method = RequestMethod.GET)
	public String betweenDateImageTestAction(Locale locale, Model model) {

	//	GregorianCalendar cal = new GregorianCalendar(2013, 8, 24); //java has downs
	//	long millis = cal.getTimeInMillis();
	//	Timestamp after = new Timestamp(millis);
		
	//	cal = new GregorianCalendar(2013, 8, 25); //java has downs
	//	millis = cal.getTimeInMillis();
	//	Timestamp before = new Timestamp(millis);
		//Snapshot result = sd.findWithTileBySnapshotId(32035);
	//	List<Snapshot> results = sd.findWithTileBetweenTimes(before,after);
		Random rand = new Random(System.currentTimeMillis());
		model.addAttribute("random", rand.nextInt());
	//	model.addAttribute("test_result", results);
		return "info";
	}
	
	
	@RequestMapping(value = "/id", method = RequestMethod.GET)
	public String idTestAction(Locale locale, Model model) {
		if (sd.equals(null)){
			System.out.println("fukin null mang");
		}
	//	Calendar c = Calendar.getInstance();
	//	c.set(2013, 9, 1);
	//	Timestamp ts = new Timestamp(c.getTimeInMillis());
		Snapshot result = sd.findWithTileBySnapshotId(32035);
		Random rand = new Random(System.currentTimeMillis());
		model.addAttribute("random", rand.nextInt());
		model.addAttribute("test_result", result);
		return "info";
	}
	
}
