package com.asyncapi.generator.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.yaml.snakeyaml.Yaml;
import com.asyncapi.generator.KafkaDefaultSourceConfigGenerator;
import com.asyncapi.parser.java.model.AsyncAPI;

import java.io.File;
import java.io.FileInputStream;


@Mojo(name = "generateKafka", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class KafkaBaseMojo extends AbstractMojo {

    @Parameter(property = "generateKafka.asyncApiSchema", required = true)
    private File asyncApiSchema;

    @Parameter(property = "generateKafka.propertyDirectory", required = true)
    private File propertyDirectory;

    @Parameter(property = "generateKafka.sourceDirectory", required = true)
    private File sourceDirectory;

    @Parameter(property = "generateKafka.packageName", required = true)
    private String packageName;

    @Parameter(property = "generateKafka.channel", required = true)
    private String channel;

    @Parameter(property = "generateKafka.defaultNaming")
    private String defaultNaming;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void execute() {
        AsyncAPI model;
        model = mapper.convertValue(new Yaml().loadAs(new FileInputStream(asyncApiSchema), AsyncAPI.class), AsyncAPI.class);
        sourceDirectory.mkdirs();
        var configGenerator = new KafkaDefaultSourceConfigGenerator(model, sourceDirectory, propertyDirectory, packageName, channel, defaultNaming);
        configGenerator.generate();
        project.addCompileSourceRoot(propertyDirectory.getAbsolutePath());
    }
}
