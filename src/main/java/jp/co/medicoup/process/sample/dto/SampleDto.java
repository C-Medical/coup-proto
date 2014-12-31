package jp.co.medicoup.process.sample.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 *
 * @author izumi_j
 *
 */
@Entity
public class SampleDto {
    @Id
    public Long id;
    public String name;
    public DateTime date;
    public List<String> someValues = new ArrayList<>();

    @Override
    public String toString() {
        return "SampleDto [id=" + id + ", name=" + name + ", date=" + date + ", someValues=" + someValues + "]";
    }
}
