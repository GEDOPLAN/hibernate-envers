package de.gedoplan.demo.envers.test;

import java.io.File;
import java.util.UUID;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;

/**
 * Basis-Klass für alle unsere Tests die das Deployment bereit stellt. In diesem
 * Projekt verwenden wir das komplette war-File. Alterantiv könnte hier mit der
 * ShrinkWrap-Api auch ein individuelles Deployment zusammen gestellt werden
 *
 * @author Dominik Mathmann
 */
public class TestBaseClass {

    /**
     * Erzeugt Deyployment aus packetiertem WAR.
     *
     * @return WebArchiv-Deployment
     */
    @Deployment()
    public static WebArchive createDeployment() {
        File pomFile = new File("pom.xml");
        WebArchive deployment = ShrinkWrap.create(MavenImporter.class, UUID.randomUUID().toString() + "_junit-demo-test.war")
                .loadPomFromFile(pomFile)
                .importBuildOutput().as(WebArchive.class);

        deployment
                .addPackage("de.gedoplan.demo.envers.test");
        return deployment;
    }

}
