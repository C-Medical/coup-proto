package jp.co.medicoup.process.sample.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.co.medicoup.process.sample.dto.SampleDto;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 *
 * @author izumi_j
 *
 */
public class SampleService {

	public List<SampleDto> getSampleDtos() {
		final List<SampleDto> result = new ArrayList<>();

		for (int i = 1; i <= 5; i++) {
			final SampleDto dto = new SampleDto();
			dto.id = i;
			dto.name = "hogehoge" + StringUtils.leftPad(String.valueOf(i), 3, "0");
			dto.date = new DateTime(2015, 4, i, 0, 0, 0);
			for (int j = 0; j <= i; j++) {
				dto.someValues.add(new BigDecimal(String.valueOf(i * 1000 + j)));
			}
			result.add(dto);
		}

		return result;
	}
}
