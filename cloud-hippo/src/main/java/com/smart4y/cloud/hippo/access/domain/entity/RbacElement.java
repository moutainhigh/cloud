package com.smart4y.cloud.hippo.access.domain.entity;

import com.smart4y.cloud.mapper.SnowflakeId;
import com.smart4y.cloud.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 页面元素表（对应页面按钮）
 *
 * @author Youtao on 2020/08/26 16:31
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_element")
@EqualsAndHashCode(callSuper = true)
public class RbacElement extends BaseEntity<RbacElement> {

    /**
     * 页面元素ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "element_id")
    private Long elementId;

    /**
     * 页面元素名
     */
    @Column(name = "element_name")
    private String elementName;

    /**
     * 页面元素编码
     */
    @Column(name = "element_code")
    private String elementCode;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * 更新时间
     */
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;


    /**
     * 构造器
     */
    public RbacElement() {
        super();
    }
}
