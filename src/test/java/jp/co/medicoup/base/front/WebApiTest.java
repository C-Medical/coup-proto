package jp.co.medicoup.base.front;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.medicoup.base.front.provide.annotation.GET;
import jp.co.medicoup.base.front.provide.annotation.Json;
import jp.co.medicoup.base.front.provide.annotation.Param;
import jp.co.medicoup.base.json.ObjectMapperHolder;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 *
 * @author izumi_j
 *
 */
public class WebApiTest {
	private static final Logger logger = LoggerFactory.getLogger(WebApiTest.class);

	public static final class SampleController {

		@GET
		public void single(@Param("id") long id, @Param("code") String code, @Param("date") DateTime date) {
			logger.debug("id = {}, code = {}, date = {}", id, code, date);
			assertEquals(123L, id);
			assertEquals("hoge", code);
			assertEquals(new DateTime(1982, 2, 21, 12, 34, 56), date);
		}

		@GET
		public void collection(@Param("ids") Collection<Long> ids, @Param("codes") String[] codes) {
			logger.debug("ids = {}, codes = {}", ids, codes);
			assertTrue(CollectionUtils.isEqualCollection(Arrays.asList(123L, 456L, 789L), ids));
			assertTrue(Arrays.equals(new String[] { "hoge" }, codes));
		}

		@GET
		public void javaType(@Param SampleParameterDto dto) {
			logger.debug("dto = {}", dto);
			assertEquals(123L, dto.id);
			assertEquals("hoge", dto.code);
			assertEquals(new DateTime(1982, 2, 21, 12, 34, 56), dto.date);
			assertTrue(CollectionUtils.isEqualCollection(Arrays.asList(123L, 456L, 789L), dto.ids));
			assertTrue(Arrays.equals(new String[] { "hoge" }, dto.codes));
		}

		@GET
		public void javaTypes(@Param("json") @Json List<SampleParameterDto> dtos) {
			logger.debug("dtos = {}", dtos);
			for (int i = 0; i < 3; i++) {
				assertEquals(i, dtos.get(i).id);
				assertEquals("hoge", dtos.get(i).code);
			}
		}
	}

	public static final class SampleParameterDto {
		public long id;
		public String code;
		public DateTime date;
		public List<Long> ids;
		public String[] codes;

		@Override
		public String toString() {
			return "SampleParameterDto [id="
					+ id
					+ ", code="
					+ code
					+ ", date="
					+ date
					+ ", ids="
					+ ids
					+ ", codes="
					+ Arrays.toString(codes)
					+ "]";
		}
	}

	@Test
	public void test_resolveParameters() throws NoSuchMethodException, SecurityException, JsonProcessingException {
		final Map<String, String[]> param = new HashMap<>();

		final WebApi apiSingle = WebApi.of(SampleController.class,
				SampleController.class.getMethod("single", long.class, String.class, DateTime.class));
		param.put("id", new String[] { "123" });
		param.put("code", new String[] { "hoge" });
		param.put("date", new String[] { "1982-02-21T12:34:56" });
		apiSingle.execute(param);

		final WebApi apiCollection = WebApi.of(SampleController.class,
				SampleController.class.getMethod("collection", Collection.class, String[].class));
		param.put("ids", new String[] { "123", "456", "789" });
		param.put("codes", new String[] { "hoge" });
		apiCollection.execute(param);

		final WebApi apiJavaType = WebApi.of(SampleController.class,
				SampleController.class.getMethod("javaType", SampleParameterDto.class));
		apiJavaType.execute(param);

		final WebApi apiJavaTypes = WebApi.of(SampleController.class,
				SampleController.class.getMethod("javaTypes", List.class));
		param.clear();
		final List<SampleParameterDto> dtos = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			final SampleParameterDto dto = new SampleParameterDto();
			dto.id = i;
			dto.code = "hoge";
			dtos.add(dto);
		}
		final String json = ObjectMapperHolder.get().writeValueAsString(dtos);
		logger.debug("Source json = {}", json);
		param.put("json", new String[] { json });
		apiJavaTypes.execute(param);
	}

}
