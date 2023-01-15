package ${(controllerPackageName)!};

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;

import com.turkraft.springfilter.boot.Filter;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import javax.validation.Valid;


import ${(domainPackageName)!}.${(entity.name)!};
import ${(dtoPackageName)!}.${(entity.name)!}Dto;
import ${(servicePackageName)!}.${(entity.name)!}Service;

@RestController
@RequestMapping(value = "/${entity.name?uncap_first}s", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
class ${(entity.name)!}Controller  {

    private final ${(entity.name)!}Service ${entity.name?uncap_first}Service;

    <#if entity.compositePk>
    @GetMapping("${(entity.compositePath)!}")
    public ${(entity.name)!}Dto findById(${(entity.compositeParameters)!}) {
        return ${entity.name?uncap_first}Service.findById(${(entity.compositeArguments)!});
    }
    <#else>
    @GetMapping("/{id}")
        public ${(entity.name)!}Dto findById(@PathVariable final ${(entity.pkType)!} id) {
        return ${entity.name?uncap_first}Service.findById(id);
    }
    </#if>

    @GetMapping("/search")
    public Page<${(entity.name)!}Dto> search(@Filter Specification<${(entity.name)!}> spec, Pageable page) {
        return ${entity.name?uncap_first}Service.search(spec, page);
    }

    @GetMapping("/search/sliced")
    public Slice<${(entity.name)!}Dto> searchBySlice(@Filter Specification<${(entity.name)!}> spec, Pageable page) {
        return ${entity.name?uncap_first}Service.searchBySlice(spec, page);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ${(entity.name)!}Dto save(@RequestBody @Valid final ${(entity.name)!}Dto ${entity.name?uncap_first}Dto) {
        return ${entity.name?uncap_first}Service.save(${entity.name?uncap_first}Dto);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable final ${(entity.pkType)!} id,
        @RequestBody @Valid final ${(entity.name)!}Dto ${entity.name?uncap_first}Dto) {
        ${entity.name?uncap_first}Service.update(id,  ${entity.name?uncap_first}Dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.MOVED_PERMANENTLY)
    public void delete(@PathVariable final ${(entity.pkType)!} id) {
        ${entity.name?uncap_first}Service.delete(id);
    }

}
