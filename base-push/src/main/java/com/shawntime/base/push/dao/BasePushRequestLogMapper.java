package com.shawntime.base.push.dao;

import java.util.List;

import com.shawntime.base.push.entity.BasePushRequestLog;
import org.apache.ibatis.annotations.Param;

/**
 * 基础推送请
 *
 * @author LemonGenerator
 */
public interface BasePushRequestLogMapper {

    /**
     * 增加
     */
    int add(BasePushRequestLog basePushRequestLog);

    /**
     * 修改
     */
    void update(BasePushRequestLog basePushRequestLog);

    /**
     * 选择更新
     */
    void updateSelective(BasePushRequestLog basePushRequestLog);

    /**
     * 删除
     */
    void remove(@Param("id") Integer id);

    /**
     * 根据ID查询
     */
    BasePushRequestLog getById(@Param("id") Integer id);

    /**
     * 根据IDS查询
     */
    List<BasePushRequestLog> getByIds(@Param("ids") List<Integer> ids);

    BasePushRequestLog getBasePushRequestLogByReqId(String reqId);

    int updateCallBackResult(@Param("callBackResult") String callBackResult,
                             @Param("pushStatus") Integer pushStatus,
                             @Param("id") Integer id);

    int batchDeleteLog(@Param("ids") List<Integer> ids);

    List<Integer> getDeleteIds();
}
