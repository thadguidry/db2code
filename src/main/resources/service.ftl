package ${(servicePackageName)!};

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ${(exceptionPackageName)!}.NoDataFoundProblem;

import ${(dtoPackageName)!}.${(entit.name)!}Dto;
import ${(repositoryPackageName)!}.${(entity.name)!}Repository;
import ${(domainPackageName)!}.${(entity.name)!};

@Service
@Slf4j
@RequiredArgsConstructor
public class ${(entity.name)!}Service  {

    private final ${(entity.name)!}Repository ${entity.name?uncap_first}Repository;

    @Transactional(readOnly = true)
    public ${(entity.name)!}Dto findById(${(keyJavaType)!} id) {
        return ${entity.name?uncap_first}Repository.findById(id)
        .map(${entity.name?uncap_first} -> toDto(${entity.name?uncap_first} , new ${(entity.name)!}Dto()))
        .orElseThrow(() -> new NoDataFoundProblem());
    }

    @Transactional(readOnly = true)
    public Page<${(entity.name)!}Dto> search(Specification<${(entity.name)!}> spec, Pageable page) {
        return ${entity.name?uncap_first}Repository.findAll(spec, page).map(
        ${entity.name?uncap_first} -> toDto(${entity.name?uncap_first} , new ${(entity.name)!}Dto())
        );
    }

    @Transactional(readOnly = true)
    public Slice<${(entity.name)!}Dto> searchBySlice(Specification<${(entity.name)!}> spec, Pageable page) {
        return ${entity.name?uncap_first}Repository.findAll(spec, page).map(
        ${entity.name?uncap_first} -> toDto(${entity.name?uncap_first}, new ${(entity.name)!}Dto())
        );
    }

    @Transactional
    public ${(entity.name)!}Dto save(final ${(entity.name)!}Dto ${entity.name?uncap_first}Dto) {
        final ${(entity.name)!} ${entity.name?uncap_first} = new ${(entity.name)!}();
        toEntity(${entity.name?uncap_first}Dto, ${entity.name?uncap_first});
        return toDto(${entity.name?uncap_first}Repository.save(${entity.name?uncap_first}), new ${(entity.name)!}Dto());
    }

    @Transactional
    public void update(final ${(entity.pkType)!} id, final ${(entity.name)!}Dto ${entity.name?uncap_first}Dto) {
        final ${(entity.name)!} ${entity.name?uncap_first} = ${entity.name?uncap_first}Repository.findById(id)
        .orElseThrow(() -> new NoDataFoundProblem());
        toEntity( ${entity.name?uncap_first}Dto,  ${entity.name?uncap_first});
        ${entity.name?uncap_first}Repository.save(${entity.name?uncap_first});
    }

    @Transactional
    public void delete(final ${(entity.pkType)!} id) {
        ${entity.name?uncap_first}Repository.deleteById(id);
    }


    private ${(entity.name)!}Dto toDto(final ${(entity.name)!} ${entity.name?uncap_first} , final ${(entity.name)!}Dto ${entity.name?uncap_first}Dto) {
    <#list entity.fields as item>
        ${entity.name?uncap_first}Dto.set${(item.name?cap_first)!}(${entity.name?uncap_first}.get${(item.name?cap_first)!}());
    </#list>
        return ${entity.name?uncap_first}Dto;
    }

    private ${(entity.name)!} toEntity(final ${(entity.name)!}Dto ${entity.name?uncap_first}Dto, final ${(entity.name)!} ${entity.name?uncap_first}){
    <#list entity.fields as item>
        ${entity.name?uncap_first}.set${(item.name?cap_first)!}(${entity.name?uncap_first}Dto.get${(item.name?cap_first)!}());
    </#list>
        return ${entity.name?uncap_first};
    }

}
