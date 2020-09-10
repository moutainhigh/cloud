package com.smart4y.cloud.hippo.domain.entity;

import javax.persistence.Column;
import com.smart4y.cloud.core.BaseEntity;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限页面元素关联表
 *
 * @author Youtao on 2020/09/10 17:53
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_privilege_element")
@EqualsAndHashCode(callSuper = true)
public class RbacPrivilegeElement extends BaseEntity<RbacPrivilegeElement> {

    /**
     * 权限ID
     */
    @Column(name = "privilege_id")
    private Long privilegeId;

    /**
     * 页面元素ID
     */
    @Column(name = "element_id")
    private Long elementId;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;


    /**
     * 构造器
     */
    public RbacPrivilegeElement() {
        super();
    }
}
