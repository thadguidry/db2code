package ${domainPackageName};

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

<#if entity.autoIncrementingFieldPresent>
import static javax.persistence.GenerationType.IDENTITY;
</#if>

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "${entity.tableName}")
public class ${entity.name} {

    <#list entity.fields as field>
    <#if field.autoIncremented>
    @Id @GeneratedValue(strategy=IDENTITY)
    </#if>
    private ${field.javaType} ${(field.name)!};
        
    </#list>
}
