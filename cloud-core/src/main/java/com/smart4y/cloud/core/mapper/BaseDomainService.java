package com.smart4y.cloud.core.mapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.core.message.page.OpenThreadContext;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.spring.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.weekend.Weekend;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 领域服务基类
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class BaseDomainService<T extends BaseEntity> {

    /**
     * 批量大小
     */
    private static int batchSize = 128;

    /**
     * 当前处理 Mapper
     */
    @Autowired
    protected CloudMapper<T> mapper;

    /**
     * 获取 当前批量 Mapper
     *
     * @return 当前批量 Mapper
     */
    private CloudMapper<T> batchMapper(SqlSession sqlSession) {
        Class<? extends CloudMapper> mapperClass = mapper.getClass();
        Class<?>[] interfaces = mapperClass.getInterfaces();
        return (CloudMapper<T>) sqlSession.getMapper(interfaces[0]);
    }

    /**
     * 获取 当前批量 Session
     *
     * @return 当前批量 Session
     */
    private SqlSession batchSqlSession() {
        SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
        return sqlSessionFactory.openSession(ExecutorType.BATCH);
    }

    /**
     * 获取 当前 Model
     *
     * @return 当前 Model
     */
    private Class<T> modelClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        return (Class<T>) typeArguments[0];
    }

    /**
     * 设置新增实体记录属性
     *
     * @param entity 实体信息
     */
    protected void setSaveInfo(T entity) {
    }

    /**
     * 设置更新实体记录属性
     *
     * @param entity 实体信息
     */
    protected void setUpdateInfo(T entity) {
    }

    /**
     * 设置新增或更新实体记录属性
     *
     * @param entity 实体信息
     */
    protected void setSaveOrUpdateInfo(T entity) {
    }

    /**
     * 设置逻辑删除实体记录属性
     *
     * @param entity 实体信息
     */
    protected void setRemoveLogicInfo(T entity) {
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 实体记录
     */
    public T getById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询一条记录
     *
     * @param weekend 查询类
     * @return 实体记录
     */
    public T getOne(Weekend<T> weekend) {
        return mapper.selectOneByExample(weekend);
    }

    /**
     * 插入一条记录
     * <p>
     * 字段策略插入
     * </p>
     *
     * @param entity 实体对象
     */
    public void save(T entity) {
        setSaveInfo(entity);
        mapper.insertSelective(entity);
    }

    /**
     * 批量插入
     * <p>
     * 字段策略插入
     * </p>
     *
     * @param entityList 实体对象集合
     */
    public void saveBatch(Collection<T> entityList) {
        saveBatch(entityList, batchSize);
    }

    /**
     * 批量插入
     * <p>
     * 字段策略插入
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     */
    public void saveBatch(Collection<T> entityList, int batchSize) {
        SqlSession sqlSession = batchSqlSession();
        CloudMapper<T> batchMapper = batchMapper(sqlSession);
        int i = 0;
        for (T entity : entityList) {
            setSaveInfo(entity);
            batchMapper.insertSelective(entity);
            if (i >= 1 && i % batchSize == 0) {
                sqlSession.flushStatements();
            }
            i++;
        }
        sqlSession.flushStatements();
    }

    /**
     * 根据Id 更新
     * <p>
     * 字段策略更新
     * </p>
     *
     * @param entity 实体对象
     */
    public void updateSelectiveById(T entity) {
        setUpdateInfo(entity);
        mapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 根据查询类，更新记录字段
     * <p>
     * 字段策略更新
     * </p>
     *
     * @param entity  实体对象
     * @param weekend 查询类
     */
    public void updateSelective(T entity, Weekend<T> weekend) {
        mapper.updateByExampleSelective(entity, weekend);
    }

    /**
     * 批量更新
     * <p>
     * 字段策略更新
     * </p>
     *
     * @param entityList 实体对象集合
     */
    public void updateSelectiveBatchById(Collection<T> entityList) {
        updateSelectiveBatchById(entityList, batchSize);
    }

    /**
     * 批量更新
     * <p>
     * 字段策略更新
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     */
    public void updateSelectiveBatchById(Collection<T> entityList, int batchSize) {
        SqlSession sqlSession = batchSqlSession();
        CloudMapper<T> batchMapper = batchMapper(sqlSession);
        int i = 0;
        for (T entity : entityList) {
            setUpdateInfo(entity);
            batchMapper.updateByPrimaryKeySelective(entity);
            if (i >= 1 && i % batchSize == 0) {
                sqlSession.flushStatements();
            }
            i++;
        }
        sqlSession.flushStatements();
    }

    /**
     * 新增或更新记录
     * <p>
     * 表必须设置唯一索引字段且唯一索引字段不能为空（Id不为空也可以）
     * </p>
     * <p>
     * 字段策略更新
     * </p>
     *
     * @param entity 实体对象
     */
    public int saveOrUpdate(T entity) {
        setSaveOrUpdateInfo(entity);
        return mapper.saveOrUpdate(entity);
    }

    /**
     * 新增或更新记录
     * <p>
     * 表必须设置唯一索引字段且唯一索引字段不能为空（Id不为空也可以）
     * </p>
     * <p>
     * 字段策略更新
     * </p>
     *
     * @param entityList 实体对象集合
     */
    public void saveOrUpdateBatch(Collection<T> entityList) {
        saveOrUpdateBatch(entityList, batchSize);
    }

    /**
     * 新增或更新记录
     * <p>
     * 表必须设置唯一索引字段且唯一索引字段不能为空（Id不为空也可以）
     * </p>
     * <p>
     * 字段策略更新
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     */
    public void saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        SqlSession sqlSession = batchSqlSession();
        CloudMapper<T> batchMapper = batchMapper(sqlSession);
        int i = 0;
        for (T entity : entityList) {
            setSaveOrUpdateInfo(entity);
            batchMapper.saveOrUpdate(entity);
            if (i >= 1 && i % batchSize == 0) {
                sqlSession.flushStatements();
            }
            i++;
        }
        sqlSession.flushStatements();
    }

    /**
     * 根据Id 逻辑删除
     *
     * @param id 主键ID
     */
    public void removeLogicById(long id) {
        T entity = getById(id);
        removeLogicById(entity);
    }

    /**
     * 根据Id 逻辑删除
     *
     * @param entity 实体对象
     */
    public void removeLogicById(T entity) {
        setRemoveLogicInfo(entity);
        mapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 根据Id 删除
     *
     * @param id 主键ID
     */
    public void removeById(long id) {
        mapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据Id 删除
     *
     * @param entity 实体对象
     */
    public void removeById(T entity) {
        mapper.delete(entity);
    }

    /**
     * 删除记录
     *
     * @param weekend 查询类
     */
    public void remove(Weekend<T> weekend) {
        mapper.deleteByExample(weekend);
    }

    /**
     * 查询总记录数
     *
     * @param weekend 查询类
     * @return 总记录数
     */
    public int count(Weekend<T> weekend) {
        return mapper.selectCountByExample(weekend);
    }

    /**
     * 查询所有数据
     *
     * @return 列表数据
     */
    public List<T> list() {
        Weekend<T> weekend = Weekend.of(modelClass());
        weekend
                .weekendCriteria();
        //.andEqualTo(BaseEntity::getArchived, Boolean.TRUE);
        return list(weekend);
    }

    /**
     * 查询列表
     *
     * @param weekend 查询类
     * @return 列表数据
     */
    public List<T> list(Weekend<T> weekend) {
        return mapper.selectByExample(weekend);
    }

    /**
     * 分页查询
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param weekend  查询类
     * @return 分页信息
     */
    public PageInfo<T> page(Weekend<T> weekend) {
        int page = OpenThreadContext.getPage();
        int limit = OpenThreadContext.getLimit();
        PageHelper.startPage(page, limit, Boolean.TRUE);
        List<T> list = list(weekend);
        PageInfo pageInfo = new PageInfo<>(list);
        OpenThreadContext.removePageLimit();
        return pageInfo;
    }

    public Page<T> findPage(Weekend<T> weekend) {
        PageInfo<T> pageInfo = this.page(weekend);
        Page<T> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }
}