package jp.co.medicoup.process.sample.service;

import java.util.List;

import jp.co.medicoup.process.sample.dao.SampleDao;
import jp.co.medicoup.process.sample.dto.SampleDto;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public class SampleService {
    private static final Logger logger = LoggerFactory.getLogger(SampleService.class);

    static {
        logger.info("サンプルデータを準備。");
        final SampleDao dao = new SampleDao();
        for (int i = 1; i <= 5; i++) {
            final SampleDto dto = new SampleDto();
            dto.name = "hogehoge" + StringUtils.leftPad(String.valueOf(i), 3, "0");
            dto.date = DateTime.now();
            for (int j = 0; j < i; j++) {
                dto.someValues.add(String.valueOf(i * 1000 + j));
            }
            dao.save(dto);
        }
    }

    private final SampleDao dao = new SampleDao();

    public List<SampleDto> getSampleDtos() {
        return dao.loadLimit5();
    }

    public SampleDto getSampleDto(long id) {
        return dao.load(id);
    }

    public void save(SampleDto dto) {
        dto.date = DateTime.now();
        dao.save(dto);
    }
}
