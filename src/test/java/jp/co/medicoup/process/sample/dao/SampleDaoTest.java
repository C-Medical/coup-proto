package jp.co.medicoup.process.sample.dao;

import static org.junit.Assert.assertEquals;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;

import jp.co.medicoup.process.sample.dto.SampleDto;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

public class SampleDaoTest {
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    private Closeable ofy;

    @Before
    public void setUp() {
        helper.setUp();
        ofy = ObjectifyService.begin();
    }

    @After
    public void tearDown() throws IOException {
        ofy.close();
        helper.tearDown();
    }

    @Test
    public void test() {
        final SampleDao dao = new SampleDao();

        SampleDto dto1 = new SampleDto();
        dto1.name = "hoge";
        dto1.someValues.add(new BigDecimal("123.456"));
        dto1.date = new DateTime(0);

        dao.save(dto1);

        SampleDto dto2 = dao.load(dto1.id);
        assertEquals(dto1.name, dto2.name);
    }
}
