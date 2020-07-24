package ${tableClass.packageName};

<#--<#if tableClass.allFields??>
    <#list tableClass.allFields as field>
        <#if field.identity==true??>
import SnowflakeId;
        </#if>
    </#list>
</#if>-->
<#--<#if tableClass.allFields??>
    <#list tableClass.allFields as field>
        <#if field.identity==true??>
import tk.mybatis.mapper.annotation.KeySql;
        </#if>
    </#list>
</#if>-->
<#list tableClass.javaTypes as fullyQualifiedJavaType>
import ${fullyQualifiedJavaType};
</#list>

/**
 * ${tableClass.tableRemarks}
 *
 * @author ${tableClass.tableAuthor} on ${tableClass.createdAt}
 */
@Data
@Accessors(chain = true)
@Table(name = "${tableClass.tableName}")
@EqualsAndHashCode(callSuper = true)
public class ${tableClass.shortClassName} extends BaseEntity<${tableClass.shortClassName}> {

<#list tableClass.allFields as field>
    /**
     * ${field.remarks}
     */
    <#if field.identity==true??>
    @Id
    <#--@KeySql(genId = SnowflakeId.class)-->
    </#if>
    @Column(name = "${field.columnName}")
    private ${field.shortTypeName} ${field.fieldName};

</#list>

    /**
     * 构造器
     */
    public ${tableClass.shortClassName}() {
        super();
    }
}