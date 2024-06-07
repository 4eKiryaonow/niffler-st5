package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;
import static guru.qa.niffler.model.UserJson.simpleUser;

public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static Map<User.UserType, Queue<UserJson>> USERS = new ConcurrentHashMap<>();


    static {
        Queue<UserJson> queueWithFriends = new ConcurrentLinkedQueue<>();
        Queue<UserJson> queueInvitationSend = new ConcurrentLinkedQueue<>();
        Queue<UserJson> queueInvitationReceived = new ConcurrentLinkedQueue<>();

        queueWithFriends.add(simpleUser("dima", "12345", WITH_FRIENDS));
        queueWithFriends.add(simpleUser("Kirill", "12345", WITH_FRIENDS));
        queueInvitationSend.add(simpleUser("Kirill", "12345", INVITATION_SEND));
        queueInvitationSend.add(simpleUser("duck", "12345", INVITATION_SEND));
        queueInvitationReceived.add(simpleUser("duck", "12345", INVITATION_RECEIVED));

        USERS.put(WITH_FRIENDS, queueWithFriends);
        USERS.put(INVITATION_SEND, queueInvitationSend);
        USERS.put(INVITATION_RECEIVED, queueInvitationReceived);
    }


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Method> methodList = getMethods(context);
        List<Parameter> params = getParameters(methodList);

        Map<User.UserType, List<UserJson>> usersForTest = new HashMap<>();
        List<UserJson> listUsers = new ArrayList<>();
        for (Parameter parameter : params) {
            User.UserType userType = parameter.getAnnotation(User.class).value();
            Queue<UserJson> queue = USERS.get(userType);

            UserJson userForTest = null;
            while (userForTest == null) {
                userForTest = queue.poll();
            }
            listUsers.add(userForTest);
            usersForTest.put(userType, listUsers);
        }
        Allure.getLifecycle().updateTestCase(testCase -> {
            testCase.setStart(new Date().getTime());
        });
        context.getStore(NAMESPACE).put(context.getUniqueId(), usersForTest);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<User.UserType, List<UserJson>> usersFromTest = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class);

        for (User.UserType userType : usersFromTest.keySet()) {
            usersFromTest.get(userType).forEach(userJson -> USERS.get(userType).add(userJson));
            System.out.println(USERS);
        }

    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserJson.class) &&
                parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Optional<User> annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), User.class);
        User.UserType userType = annotation.get().value();
        Map<User.UserType, List<UserJson>> mapUsers = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        List<UserJson> userJsonList = mapUsers.get(userType);

        return userJsonList.get(parameterContext.getIndex());
    }

    private static List<Parameter> getParameters(List<Method> methodList) {
        return methodList
                .stream()
                .map(Executable::getParameters)
                .flatMap(Arrays::stream)
                .filter(parameter -> parameter.isAnnotationPresent(User.class))
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
                .toList();
    }

    private static List<Method> getMethods(ExtensionContext context) {
        List<Method> methodList = new ArrayList<>();
        methodList.add(context.getRequiredTestMethod());
        Arrays.stream(context.getRequiredTestClass()
                        .getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .forEach(methodList::add);
        return methodList;
    }
}
