package ${targetPackage};

import com.smart4y.cloud.core.infrastructure.mapper.BaseEntity;
<#if tableClass.allFields??>
    <#list tableClass.allFields as field>
        <#if field.identity==true??>
import com.smart4y.cloud.core.infrastructure.toolkit.gen.SnowflakeId;
        </#if>
    </#list>
</#if>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
<#if tableClass.allFields??>
    <#list tableClass.allFields as field>
        <#if field.identity==true??>
import tk.mybatis.mapper.annotation.KeySql;
        </#if>
    </#list>
</#if>

import javax.persistence.Column;
<#if tableClass.allFields??>
    <#list tableClass.allFields as field>
        <#if field.identity==true??>
import javax.persistence.Id;
        </#if>
    </#list>
</#if>
import javax.persistence.Table;
<#if fullyQualifiedJavaTypes??>
    <#list fullyQualifiedJavaTypes as fullyQualifiedJavaType>
import ${fullyQualifiedJavaType};
    </#list>
</#if>

/**
 * ${tableRemarks}
 *
 * @author Youtao
 *         Created by ${author} on ${createdAt}.
 */
@Data
@Table(name = "${tableClass.tableName}")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ${tableClass.shortClassName} extends BaseEntity {

<#if tableClass.allFields??>
    <#list tableClass.allFields as field>
    /**
     * ${field.remarks}
     */
    <#if field.identity==true??>
    @Id
    @KeySql(genId = SnowflakeId.class)
    </#if>
    @Column(name = "${field.columnName}")
    private ${field.shortTypeName} ${field.fieldName};

    </#list>
</#if>
    /**
     * 构造器
     */
    public ${tableClass.shortClassName}() {
        super();
    }
}