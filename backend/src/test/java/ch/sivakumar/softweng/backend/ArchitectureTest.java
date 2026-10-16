package ch.sivakumar.softweng.backend;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

public class ArchitectureTest {

    private static final String BASE_PACKAGE = "ch.sivakumar.softweng.backend";
    private static final String DOMAIN_PACKAGE = BASE_PACKAGE + ".domain..";
    private static final String APPLICATION_PACKAGE = BASE_PACKAGE + ".application..";
    private static final String ADAPTER_PACKAGE = BASE_PACKAGE + ".adapter..";

    @Test
    void shouldEnsureOnionArchitecture() {
        Architectures.OnionArchitecture architectureRule = onionArchitecture()
                .withOptionalLayers(true)
                .domainModels(DOMAIN_PACKAGE)
                .applicationServices(APPLICATION_PACKAGE)
                .adapter("adapter", ADAPTER_PACKAGE);

        JavaClasses classesToTest = new ClassFileImporter()
                .withImportOption(DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);

        architectureRule.check(classesToTest);
    }

    @Test
    void shouldEnsureAllDomainTypesAreRecords() {
        ArchRule rule = classes()
                .that().resideInAPackage(DOMAIN_PACKAGE)
                .and().resideOutsideOfPackage(BASE_PACKAGE + ".domain.exception..")
                .should().beRecords()
                .orShould().beEnums()
                .allowEmptyShould(true);

        JavaClasses domainClasses = new ClassFileImporter()
                .withImportOption(DO_NOT_INCLUDE_TESTS)
                .importPackages(DOMAIN_PACKAGE);

        rule.check(domainClasses);
    }

}
