package com.homihq.generator;

import com.homihq.model.Entity;
import com.homihq.model.MetaData;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JpaEntityGenerator {

    public void generate(MetaData metaData) throws Exception{
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "/"));

        if(Objects.nonNull(metaData.getEntityList()) && !metaData.getEntityList().isEmpty()) {
            for(Entity entity : metaData.getEntityList()) {
                Map<String, Object> data = new HashMap<>();
                data.put("domainPackageName", metaData.getDomainPackageName());
                data.put("entity", entity);

                Template template = cfg.getTemplate("entity.ftl");
                Writer writer = new FileWriter(new File( "/Users/dhrubo/Downloads/generator/" + entity.getName() +".java"));
                template.process(data, writer);
                writer.close();
            }
        }

    }
}
