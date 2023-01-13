package ${domainPackageName};

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ${entity.name}Id implements Serializable{

<#list entity.fields as field>
    <#if field.partOfPk>
    private ${field.javaType} ${(field.name)!};
    </#if>


</#list>
}
