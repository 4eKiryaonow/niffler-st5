package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractSpendExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractSpendExtension.class);

    protected abstract SpendJson createSpend(GenerateSpend spend);

    protected abstract void removeSpend(SpendJson spend);

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateSpend.class
        ).ifPresent(
                spend -> extensionContext
                        .getStore(NAMESPACE)
                        .put(extensionContext.getUniqueId(), createSpend(spend))
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        SpendJson spendJson = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        removeSpend(spendJson);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }
}