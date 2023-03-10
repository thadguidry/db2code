package ${(dtoPackageName)!};

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${entity.name}Dto  {

<#list entity.fields as item>
    private ${item.javaType} ${(item.name)!};
</#list>

}
