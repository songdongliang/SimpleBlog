package com.lvwang.osf.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvwang.osf.pojo.BasePojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public abstract class BaseService<T extends BasePojo> {

    @Autowired
    private Mapper<T> mapper ;
    
    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    public T queryById(Integer id) {
        return mapper.selectByPrimaryKey(id) ;
    }

    public List<T> queryByIds(Class<T> clazz,List ids,String property) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property,ids);
        return mapper.selectByExample(example);
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<T> queryAll() {
        return mapper.select(null) ;
    }

    /**
     * 根据条件查询一条数据
     * @param t
     * @return
     */
    public T queryOne(T t) {
        return mapper.selectOne(t) ;
    }

    /**
     * 根据条件查询数据列表
     * @param t
     * @return
     */
    public List<T> queryListByWhere(T t) {
        return mapper.select(t) ;
    }

    /**
     * 分页查询
     * @param t
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T t, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize) ;
        List<T> list = queryListByWhere(t) ;
        return new PageInfo<T>(list) ;
    }

    /**
     * 新增数据，返回成功的条数
     * @param t
     * @return
     */
    public Integer save(T t){
        t.setCreateTime(new Date());
        t.setUpdateTime(t.getCreateTime());
        return mapper.insert(t);
    }

    /**
     * 新增数据，使用不为null的字段，返回成功的条数
     * @param t
     * @return
     */
    public Integer saveSelective(T t){
        t.setCreateTime(new Date());
        t.setUpdateTime(t.getCreateTime());
        return mapper.insertSelective(t);
    }

    /**
     * 修改数据，返回成功的条数
     * @param t
     * @return
     */
    public Integer update(T t){
        t.setUpdateTime(new Date());
        return mapper.updateByPrimaryKey(t);
    }

    /**
     * 修改数据，使用不为null的字段，返回成功的条数
     * @param t
     * @return
     */
    public Integer updateSelective(T t){
        t.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(t);
    }

    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    public Integer deleteById(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除
     * @param clazz
     * @param property
     * @param values
     * @return
     */
    public Integer deleteByIds(Class<T> clazz, String property, List<Object> values){
        Example example = new Example(clazz);
        example.createCriteria().andIn(property,values);
        return mapper.deleteByExample(example);
    }

    /**
     * 根据条件做删除
     * @param t
     * @return
     */
    public Integer deleteByWhere(T t) {
        return mapper.delete(t) ;
    }

}
