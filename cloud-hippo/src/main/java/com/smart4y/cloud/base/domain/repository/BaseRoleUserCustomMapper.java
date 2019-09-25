package com.smart4y.cloud.base.domain.repository;

import com.smart4y.cloud.base.domain.model.BaseRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/24.
 */
@Mapper
@Repository
public interface BaseRoleUserCustomMapper {

    /**
     * 查询系统用户角色
     */
    List<BaseRole> selectRoleUserList(@Param("userId") Long userId);

    /**
     * 查询用户角色ID列表
     */
    List<Long> selectRoleUserIdList(@Param("userId") Long userId);
}