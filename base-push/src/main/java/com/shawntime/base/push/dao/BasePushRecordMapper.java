package com.shawntime.base.push.dao;

import java.util.List;

import com.shawntime.base.push.entity.BasePushRecord;
import org.apache.ibatis.annotations.Param;

public interface BasePushRecordMapper {

    List<BasePushRecord> getOnlineBasePushRecordList(@Param("businessId") int businessId,
                                                     @Param("subBusinessId") int subBusinessId,
                                                     @Param("needTimestamp") boolean needTimestamp);


    /**
     * 增加
     */
    int add(BasePushRecord basePushRecord);

    /**
     * 修改
     */
    int update(BasePushRecord basePushRecord);

    /**
     * 选择更新
     */
    void updateSelective(BasePushRecord basePushRecord);

    /**
     * 删除
     */
    void remove(@Param("id") Integer id);

    /**
     * 根据ID查询
     */
    BasePushRecord getById(@Param("id") Integer id);

    /**
     * 根据IDS查询
     */
    List<BasePushRecord> getByIds(@Param("ids") List<Integer> ids);
}
