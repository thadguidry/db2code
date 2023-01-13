package ${(repositoryPackageName)!};


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ${(domainPackageName)!}.${(entity.name)!};

public interface ${(entity.name)!}Repository extends JpaRepository<${(entity.name)!}, ${(entity.pkType)!}>, JpaSpecificationExecutor<${(entity.name)!}> {
}
