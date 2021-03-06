package uk.org.taverna.scufl2.rdfxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;

import uk.org.taverna.scufl2.api.annotation.Revision;

public class TestRevisionParsing {
	
	private static final String ROEVO_TEST_XML = "roevo-test.xml";
	RevisionParser parser = new RevisionParser();
	
	@Test
	public void parseTestXML() throws Exception {
		URL base = getClass().getResource(ROEVO_TEST_XML);
		assertNotNull("Could not find " + ROEVO_TEST_XML, base);
		InputStream inStream = base.openStream();
		Map<URI, Revision> revisions = parser.readRevisionChain(inStream, base.toURI());
		assertTrue(revisions.size() > 3);
		
		Revision r3 = revisions.get(URI.create("http://example.com/test/v3"));
		assertNotNull("Did not return a Revision", r3);
		assertEquals("http://example.com/test/v3", r3.getIdentifier().toASCIIString());	
		
		GregorianCalendar expectedTime = createTime(2012, 12, 24, 18, 0, 0);
		assertEquals(expectedTime.getTimeInMillis(), r3.getGeneratedAtTime().getTimeInMillis());
		
		
		
		assertEquals("http://example.com/InsertNestedWorkflow", 
				r3.getChangeSpecificationType().toASCIIString());
		
		
		assertEquals(2, r3.getWasAttributedTo().size());
		assertTrue(r3.getWasAttributedTo().contains(URI.create("http://example.com/Fred")));
		assertTrue(r3.getWasAttributedTo().contains(URI.create("http://example.net/SomeoneElse#me")));
		
		assertEquals(2, r3.getHadOriginalSources().size());
		List<Revision> originals = new ArrayList<Revision>(r3.getHadOriginalSources());
		assertEquals("http://example.org/originalSource1", originals.get(0).getIdentifier().toASCIIString());
		assertEquals("http://example.com/test/originalSource2", originals.get(1).getIdentifier().toASCIIString());

		
		assertEquals(2, r3.getAdditionOf().size());
		assertTrue(r3.getAdditionOf().contains(URI.create("http://example.com/test/nested-workflow1")));
		assertTrue(r3.getAdditionOf().contains(URI.create("http://example.net/nested-workflow2")));

		assertEquals(2, r3.getRemovalOf().size());
		assertTrue(r3.getRemovalOf().contains(URI.create("http://example.net/removed-item")));
		assertTrue(r3.getRemovalOf().contains(URI.create("http://example.com/test/removed-item2")));

		assertEquals(2, r3.getModificationsOf().size());
		assertTrue(r3.getModificationsOf().contains(URI.create("http://example.com/test/modified")));
		assertTrue(r3.getModificationsOf().contains(URI.create("http://example.net/modified2")));

		Revision r2 = r3.getPreviousRevision();
		assertEquals("http://example.com/test/v2", r2.getIdentifier().toASCIIString());	
		
		GregorianCalendar r2Time = createTime(2010, 1, 15, 11, 0, 0);
		assertEquals(r2Time.getTimeInMillis(), r2.getGeneratedAtTime().getTimeInMillis());

		Revision r1 = r2.getPreviousRevision();
		assertEquals("http://example.net/v1", r1.getIdentifier().toASCIIString());
		Revision r0 = r1.getPreviousRevision();
		assertEquals("http://example.net/v0", r0.getIdentifier().toASCIIString());
		assertEquals(null, r0.getPreviousRevision());
		
	}

	private GregorianCalendar createTime(int year, int month, int date, int hourOfDay, int minute,
            int second) {		
		// WARNING: java.util.GregorianCalendar is the most broken Calendar API I've ever come across.		
		GregorianCalendar expectedTime = new GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
		// 1) Months (but only months) start at 0, not 1!
		expectedTime.set(year, month-1, date, hourOfDay, minute, second);
		// 2) Even if you set the time explicitly, we have to set the millis as well
		// otherwise it would use the millisecond value at the point of construction (!!)
		expectedTime.set(Calendar.MILLISECOND, 0);
		// 3) Still they don't actually compare equal, even if they are at the point in time in
		// milliseconds since epochs
		return expectedTime;
	}
}
