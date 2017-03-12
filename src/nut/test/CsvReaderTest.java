package nut.test;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

import nut.csv.CsvReader;

public class CsvReaderTest {
	public class TestObject{
		public int Id;
		public String Text;
	}
	
	private final String fileName = "test.csv";
	private final String separator = ";";
	
	private void makeFile() throws IOException{
		try (Writer writer = new BufferedWriter(new FileWriter(fileName))){
			writer.write("TestHeader");
			writer.write("10" + separator + "Test");
		}
	}
	
	@Test
	public void testConstructor() throws Exception {
		makeFile();
		try (CsvReader<TestObject> csvReader = new CsvReader<TestObject>(new FileInputStream(fileName),
											separator,
											TestObject.class,
											true)){
			assertNotNull(csvReader);	
		}
	}

}
