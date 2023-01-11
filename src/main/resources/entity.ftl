package ${domainPackageName};

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

<#if entity.autoIncrementingFieldPresent>
import static javax.persistence.GenerationType.IDENTITY;
</#if>

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "${entity.tableName}")
public class ${entity.name} {

<#list entity.fields as field>
    <#if field.partOfPk>
    @Id
    </#if>
    <#if field.autoIncremented>
    @GeneratedValue(strategy=IDENTITY)
    </#if>
    <#if field.one2One.association><#-- Handle 1:1 -->
    @OneToOne(fetch = FetchType.LAZY)
        <#if field.one2One.shared && field.one2One.owningSide>
    @MapsId
    @JoinColumn(name = "${field.columnName}" referencedColumnName = "${field.one2One.referencedField.columnName}")
        </#if>
        <#if field.one2One.owningSide>
    @JoinColumn(name = "${field.columnName}" referencedColumnName = "${field.one2One.referencedField.columnName}")
        </#if>
    private ${field.one2One.referencedField.entityName} ${field.one2One.referencedField.entityName?uncap_first};
    <#else>
    private ${field.javaType} ${(field.name)!};
    </#if>

</#list>
}
