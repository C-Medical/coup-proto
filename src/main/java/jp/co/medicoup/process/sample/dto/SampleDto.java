package jp.co.medicoup.process.sample.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

/**
 *
 * @author izumi_j
 *
 */
public class SampleDto {
	public long id;
	public String name;
	public DateTime date;
	public final List<BigDecimal> someValues = new ArrayList<>();

	@Override
	public String toString() {
		return "SampleDto [id=" + id + ", name=" + name + ", date=" + date + ", someValues=" + someValues + "]";
	}
}
