package com.wolfking.jeesite.modules.sys.service;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.service.LongIDCrudService;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.sys.dao.SequenceDao;
import com.wolfking.jeesite.modules.sys.entity.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 单据编号Service
 * @author
 * @version
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class SequenceService extends LongIDCrudService<SequenceDao, Sequence> {
	@Autowired
	private RedisUtilsLocal redisUtilsLocal;

	/**
	 * 更新当前序号
	 * @param sequence
	 */
	@Transactional()
	public void updatePreviousDigit(Sequence sequence){
		dao.updatePreviousDigit(sequence);
	}

	/**
	 * 获取MYCAT全局ID
	 * @param sequence
	 * @return
	 */
	public long NextIDValue(Sequence sequence){
		return dao.NextIDValue(sequence);
	}

	@Override
	@Transactional()
	public void delete(Sequence sequence){
		super.delete(sequence);
		//清除缓存
		delSequenceCache(sequence.getCode());
	}

	@Override
	@Transactional()
	public void save(Sequence sequence){
		super.save(sequence);
		//清除缓存
		delSequenceCache(sequence.getCode());
	}

	/****************************************************************************
	 * redis操作
	 ****************************************************************************/
	/**
	 * 根据code获取
	 * @param code
	 * @param forceLoadFromDB 强制从DB读取
	 * @return
	 */
	public Sequence getByCode(String code, boolean forceLoadFromDB){
		if (forceLoadFromDB){
			return dao.getByCode(code);
		}
		Sequence sequence = (Sequence) redisUtilsLocal.get(RedisConstant.RedisDBType.REDIS_SEQ_DB, String.format(RedisConstant.SEQ_RULE, code), Sequence.class);
		if (sequence != null){
			return sequence;
		}
		sequence = dao.getByCode(code);
		redisUtilsLocal.set(RedisConstant.RedisDBType.REDIS_SEQ_DB, String.format(RedisConstant.SEQ_RULE, code), sequence, 0);
		return sequence;
	}

	/**
	 * 删除序号规则缓存
	 * @param code
	 */
	public void delSequenceCache(String code){
		redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_SEQ_DB, String.format(RedisConstant.SEQ_RULE, code));
	}
}
