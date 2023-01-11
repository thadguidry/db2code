package ${(repositoryPackage)!};


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ${(domainPackage)!}.${(entity.name)!};

public interface ${(entity.name)!}Repository extends JpaRepository<${(entity.name)!}, ${(entity.name)!}>, JpaSpecificationExecutor<${(entity.name)!}> {
}
