package com.studentsearch.StudentSearchProject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@RestController
public class StudentSearchProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentSearchProjectApplication.class, args);
	}

	@RequestMapping({"/"})
	public String index(){
		return "Home Page";
	}

	@RequestMapping({"/student"})
	public ModelAndView student(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		Iterator<Object> full = null;
		try {
			full = parseJSON("https://hccs-advancejava.s3.amazonaws.com/student_course.json");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("Filter by name or course number.");
		Scanner studentscan = new Scanner(System.in);
		String search = studentscan.nextLine();
		Student[] filtered = filter(search, full);
		printStudents(filtered);
		return mav;
	}

	public static Iterator parseJSON(String url) throws ParseException {

		Client client = Client.create();
		WebResource webResource = client.resource(url);

		ClientResponse clientResponse = webResource.accept("application/json").get(ClientResponse.class);
		if (clientResponse.getStatus() !=200) {
			throw new RuntimeException("Failed" + clientResponse.toString());
		}

		JSONArray jsonArray = (JSONArray) new JSONParser().parse(clientResponse.getEntity(String.class));

		Iterator<Object> it = jsonArray.iterator();

		return it;
	}

	//Method to change JSON to Array of Students
	public static Student[] filter(String uInput, Iterator list) {

		//Converting JSON to List filteredList
		List<Student> filteredList = new ArrayList<>();


		while (list.hasNext()) {
			JSONObject jsonObject = (JSONObject) list.next();
			if ((JSONArray) jsonObject.get("course") != null) {
				JSONArray sc = (JSONArray) jsonObject.get("course");

				boolean check = false;
				for(int i = 0; i < sc.size(); i++){
					JSONObject jo = (JSONObject) sc.get(i);
					if(jo.get("courseNo").equals(uInput)){
						check = true;
					}
				}
				if (jsonObject.get("first_name").equals(uInput) || check) {
					Student s = new Student(
							String.valueOf(jsonObject.get("first_name")),
							String.valueOf(jsonObject.get("gender")),
							String.valueOf(jsonObject.get("email")),
							(JSONArray) jsonObject.get("course")
					);
					filteredList.add(s);
				}
			} else {
				if (jsonObject.get("first_name").equals(uInput)) {
					Student s = new Student(
							String.valueOf(jsonObject.get("first_name")),
							String.valueOf(jsonObject.get("gender")),
							String.valueOf(jsonObject.get("email"))
					);
					filteredList.add(s);
				}
			}
		}

		//Converting filteredList to Array studentFilter
		Student[] studentFilter = new Student[filteredList.size()];
		for(int u = 0; u<filteredList.size(); u++){
			studentFilter[u] = filteredList.get(u);
		}

		return studentFilter;
	}

	//Prints Array
	public static void printStudents(Student[] array){
		for(int i = 0; i < array.length; i++){
			array[i].PrintStudent();
		}
	}
}