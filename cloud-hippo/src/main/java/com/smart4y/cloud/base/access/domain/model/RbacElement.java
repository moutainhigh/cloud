package com.smart4y.cloud.base.access.domain.model;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import java.time.LocalDateTime;
import lombok.Data;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeId;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;
import javax.persistence.Id;

/**
 * 页面元素表（对应页面按钮）
 *
 * @author Youtao on 2020/08/07 14:58
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
    @Column(name = "element_no")
    private String elementNo;

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
