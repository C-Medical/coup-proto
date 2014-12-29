package jp.co.medicoup.process.sample.dao;

import static jp.co.medicoup.base.datastore.Objectifys.ofy;
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
        logger.debug("保存完了！ {}", key);
    }

    public SampleDto load(long id) {
        Result<SampleDto> result = ofy().load().key(Key.create(SampleDto.class, id));
        return result.now();
    }
}
