package com.xiaoshu.dao;

import com.xiaoshu.entity.Itemcat;
import com.xiaoshu.entity.ItemcatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ItemcatMapper {
    int countByExample(ItemcatExample example);

    int deleteByExample(ItemcatExample example);

    int deleteByPrimaryKey(Long itemcatid);

    int insert(Itemcat record);

    int insertSelective(Itemcat record);

    List<Itemcat> selectByExample(ItemcatExample example);

    Itemcat selectByPrimaryKey(Long itemcatid);

    int updateByExampleSelective(@Param("record") Itemcat record, @Param("example") ItemcatExample example);

    int updateByExample(@Param("record") Itemcat record, @Param("example") ItemcatExample example);

    int updateByPrimaryKeySelective(Itemcat record);

    int updateByPrimaryKey(Itemcat record);

    
}