package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;

@WebFluxTest(Controller.class)
@ContextConfiguration
public class DemoApplicationTests {
	
	Controller tester = new Controller();
	
	@Test
    public void testgetSpecific() {
        assertEquals(tester.getSpecific("Central Park"), "{\"_id\": {\"$oid\": \"5cc5cae16c594de674cf220d\"}, \"locationName\": \"Central Park\", \"location\": {\"type\": \"Point\", \"coordinates\": [-73.97, 40.77]}}");
    }
	
	@Test
    public void testUpdateAndGet() {
		tester.update(-75.0, 41.09, "Brooklyn");
		assertEquals(tester.getSpecific("Brooklyn"), "{\"_id\": {\"$oid\": \"5cc5d4da630b6373e43b26be\"}, \"locationName\": \"Brooklyn\", \"location\": {\"type\": \"Point\", \"coordinates\": [-75.0, 41.09]}}");
    }
	
	@Test
    public void testgetAll() {
        assertEquals(tester.getAll(), "Document{{_id=5cc5cae16c594de674cf220d, locationName=Central Park, location=Document{{type=Point, coordinates=[-73.97, 40.77]}}}}Document{{_id=5cc5d4da630b6373e43b26be, locationName=Brooklyn, location=Document{{type=Point, coordinates=[-75.0, 41.09]}}}}");
    }
	
	@Test
    public void testnearBy() {
        assertEquals(tester.nearBy(-73.9667,40.78, 5000), "[Document{{_id=5cc5cae16c594de674cf220d, locationName=Central Park, location=Document{{type=Point, coordinates=[-73.97, 40.77]}}}}]");
    }
	
	@Test
    public void testaddAndDelete() {
		tester.add(-87, 26, "flush");
		assertNotEquals(tester.getAll(), "Document{{_id=5cc5cae16c594de674cf220d, locationName=Central Park, location=Document{{type=Point, coordinates=[-73.97, 40.77]}}}}Document{{_id=5cc5d4da630b6373e43b26be, locationName=Brooklyn, location=Document{{type=Point, coordinates=[-75.0, 41.09]}}}}");
		tester.delete("flush");
		assertEquals(tester.getAll(), "Document{{_id=5cc5cae16c594de674cf220d, locationName=Central Park, location=Document{{type=Point, coordinates=[-73.97, 40.77]}}}}Document{{_id=5cc5d4da630b6373e43b26be, locationName=Brooklyn, location=Document{{type=Point, coordinates=[-75.0, 41.09]}}}}");
    }

}
