package jp.co.medicoup.process.sample.dao;

import static jp.co.medicoup.base.datastore.Objectifys.ofy;

import java.util.List;

import jp.co.medicoup.process.sample.dto.SampleDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;

/**
 * Objectifyというライブラリを利用して、BigTableとやりとりします！
 *
 * @author izumi_j
 *
 */
public final class SampleDao {
    private static final Logger logger = LoggerFactory.getLogger(SampleDao.class);

    public void save(SampleDto dto) {
        Key<SampleDto> key = ofy().save().entity(dto).now();
        logger.debug("保存完了！ {} : {}", key, dto);
    }

    public SampleDto load(long id) {
        final Result<SampleDto> result = ofy().load().key(Key.create(SampleDto.class, id));
        final SampleDto dto = result.now();
        logger.debug("キー読込完了！ {}", dto);
        return dto;
    }

    public List<SampleDto> loadLimit5() {
        final List<SampleDto> dtos = ofy().load().type(SampleDto.class).limit(5).list();
        logger.debug("リスト読込完了！ {}", dtos);
        return dtos;
    }
}
