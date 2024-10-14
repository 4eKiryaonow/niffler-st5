package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractSpendExtension.class);

    protected abstract SpendJson createSpend(GenerateSpend spend);

    protected abstract void removeSpend(SpendJson spend);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        List<GenerateSpend> potentialSpends = new ArrayList<>();
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Spends.class
        ).ifPresent(
                spends -> potentialSpends.addAll((Arrays.stream(spends.value()).toList()))
        );

        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateSpend.class
        ).ifPresent(
                potentialSpends::add);

        if (!potentialSpends.isEmpty()) {
            List<SpendJson> createdSpends = new ArrayList<>();
            for (GenerateSpend spend : potentialSpends) {
                createdSpends.add(createSpend(spend));
            }
            extensionContext.getStore(NAMESPACE)
                    .put(extensionContext.getUniqueId(), createdSpends);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        List<SpendJson> spendJsonListJson = context.getStore(NAMESPACE).get(context.getUniqueId(), List.class);
        for (SpendJson spendJson : spendJsonListJson) {
            removeSpend(spendJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext
                .getParameter()
                .getType();
        return type.isAssignableFrom(SpendJson.class) || type.isAssignableFrom(SpendJson[].class);

    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext
                .getParameter()
                .getType();
        List<SpendJson> createdSpends = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class);
        return type.isAssignableFrom(SpendJson.class) ? createdSpends.getFirst() : createdSpends.toArray(SpendJson[]::new);
    }
}