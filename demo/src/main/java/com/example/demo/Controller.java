package com.example.demo;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.near;

import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

@RestController
public class Controller {
	
	MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	MongoDatabase database = mongoClient.getDatabase("test");
	
	/**
	 * @return
	 */
	@RequestMapping("/getAll")
	public String getAll() {
		
		MongoCollection<Document> collection = (MongoCollection<Document>) database.getCollection("location");
		MongoCursor<Document> cursor = collection.find().iterator();
		String result = "";
		try {
		    while (cursor.hasNext()) {
		    	Document t = cursor.next();
		        result += t.toString();
		    }
		} finally {
		    cursor.close();
		}
		return result;
	}
	
	/**
	 * @param locationName
	 * @return
	 */
	@RequestMapping("/getSpecific")
	public String getSpecific(@RequestParam(value="locationName")String locationName) {
		
		MongoCollection<Document> collection = (MongoCollection<Document>) database.getCollection("location");
		Document myDoc = collection.find(eq("locationName", locationName)).first();
		System.out.println(myDoc.toJson());
		return myDoc.toJson();
	}
	
	/**
	 * @param lat
	 * @param lon
	 * @param locationName
	 */
	@RequestMapping("/add")
	public void add(@RequestParam(value="lat")double lat, @RequestParam(value="lon")double lon, @RequestParam(value="locationName")String locationName) {
		
		MongoCollection<Document> collection = (MongoCollection<Document>) database.getCollection("location");
		Document doc = new Document("locationName", locationName)
				.append("location", new Document("type", "Point").append("coordinates", Arrays.asList(lat, lon)));
		collection.insertOne(doc);
	}
	
	/**
	 * @param lat
	 * @param lon
	 * @param locationName
	 */
	@RequestMapping("/update")
	public void update(@RequestParam(value="lat")double lat, @RequestParam(value="lon")double lon, @RequestParam(value="locationName")String locationName) {
		
		MongoCollection<Document> collection = (MongoCollection<Document>) database.getCollection("location");
		Document doc = new Document("locationName", locationName)
				.append("location", new Document("type", "Point").append("coordinates", Arrays.asList(lat, lon)));
		collection.updateOne(eq("locationName", locationName), new Document("$set", doc));
	}
	
	/**
	 * @param locationName
	 */
	@RequestMapping("/delete")
	public void delete(@RequestParam(value="locationName")String locationName) {
		
		MongoCollection<Document> collection = (MongoCollection<Document>) database.getCollection("location");
		collection.deleteOne(eq("locationName", locationName));
	}
	
	/**
	 * @param lat
	 * @param lon
	 * @param radius
	 * @return
	 */
	@RequestMapping("/nearBy")
	public String nearBy(@RequestParam(value="lat")double lat, @RequestParam(value="lon")double lon, @RequestParam(value="radius")double radius) {
		
		MongoCollection<Document> collection = (MongoCollection<Document>) database.getCollection("location");	
		Position position = new Position(lat,lon);
		Point point = new Point(position);
		MongoCursor<Document> cursor = collection.find(near("location", point, radius, 0.0)).iterator();
		ArrayList<Document> documents = new ArrayList<Document>();
		
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                documents.add(doc);
                System.out.print(doc);
            }
        } finally {
            cursor.close();
        }
        return documents.toString();
	}
	

}
